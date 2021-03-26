package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforce;
import static chopchop.commons.util.Enforce.enforceContains;
import static chopchop.commons.util.Enforce.enforceNonNull;
import static chopchop.commons.util.Enforce.enforcePresent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import chopchop.commons.util.Result;
import chopchop.logic.edit.EditOperationType;
import chopchop.logic.edit.IngredientRefEditDescriptor;
import chopchop.logic.edit.RecipeEditDescriptor;
import chopchop.logic.edit.StepEditDescriptor;
import chopchop.logic.edit.TagEditDescriptor;
import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.ItemReference;
import chopchop.model.Model;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.Step;
import chopchop.model.attributes.Tag;
import chopchop.model.attributes.units.Count;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.recipe.Recipe;

/**
 * Edits a recipe identified using it's displayed index or name from the recipe book.
 */
public class EditRecipeCommand extends Command implements Undoable {

    private final ItemReference item;
    private final RecipeEditDescriptor recipeEditDescriptor;
    private Recipe recipe;
    private Recipe editedRecipe;

    /**
     * Constructs a command that edits the given recipe item.
     */
    public EditRecipeCommand(ItemReference item, RecipeEditDescriptor recipeEditDescriptor) {
        enforceNonNull(item, recipeEditDescriptor);

        this.item = item;
        this.recipeEditDescriptor = recipeEditDescriptor;
    }


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        var res = resolveRecipeReference(this.item, model);
        if (res.isError()) {
            return CommandResult.error(res.getError());
        }

        this.recipe = res.getValue();

        var red = this.recipeEditDescriptor;
        if (red.getNameEdit().isEmpty() && red.getIngredientEdits().isEmpty()
            && red.getTagEdits().isEmpty() && red.getStepEdits().isEmpty()) {

            return CommandResult.message("No edits provided; recipe '%s' was not modified",
                this.recipe.getName());
        }

        var newName = red.getNameEdit().orElse(this.recipe.getName());
        var newIngredients = performIngredientEdits(red.getIngredientEdits());
        var newSteps = performStepEdits(red.getStepEdits());
        var newTags = performTagEdits(red.getTagEdits());

        var foo = Result.firstError(newIngredients, newSteps, newTags);
        if (foo.isPresent()) {
            return CommandResult.error(foo.get());
        }

        this.editedRecipe = new Recipe(newName,
            newIngredients.getValue(),
            newSteps.getValue(),
            newTags.getValue());


        var editedName = !this.recipe.getName().equalsIgnoreCase(newName);
        if (editedName && model.findRecipeWithName(newName).isPresent()) {
            return CommandResult.error("Cannot rename recipe '%s' to '%s'; the latter already exists",
                this.recipe.getName(), newName);
        }

        model.setRecipe(this.recipe, this.editedRecipe);
        return CommandResult.message("Edited recipe '%s'%s", this.recipe.getName(),
            editedName ? String.format(" (renamed to '%s')", newName) : ""
        ).showingRecipe(this.editedRecipe);
    }




    private Result<List<IngredientReference>> performIngredientEdits(List<IngredientRefEditDescriptor> edits) {

        // this is a reference.
        var ingredients = new ArrayList<>(this.recipe.getIngredients());

        for (var edit : edits) {

            var type = edit.getEditType();
            var name = edit.getIngredientName();
            var qtyOpt = edit.getIngredientQuantity();

            if (qtyOpt.map(Quantity::isZero).orElse(false)) {
                return Result.error("Quantity should not be zero (for ingredient '%s')", name);
            }

            enforceContains(type, EditOperationType.ADD, EditOperationType.EDIT, EditOperationType.DELETE);
            if (type == EditOperationType.ADD) {

                for (var ingr : ingredients) {
                    if (ingr.getName().equalsIgnoreCase(name)) {
                        return Result.error("Recipe '%s' already contains ingredient '%s'",
                            this.recipe.getName(), name);
                    }
                }

                ingredients.add(new IngredientReference(name, qtyOpt.orElse(Count.of(1))));

            } else {

                var opt = ingredients.stream()
                    .filter(ingr -> ingr.getName().equalsIgnoreCase(name))
                    .findFirst();

                if (opt.isEmpty()) {
                    return Result.error("Recipe doesn't contain an ingredient named '%s'", name);
                }

                var existing = opt.get();

                if (type == EditOperationType.EDIT) {

                    enforcePresent(qtyOpt);

                    var idx = ingredients.indexOf(existing);
                    ingredients.set(idx, new IngredientReference(name, qtyOpt.get()));

                } else if (type == EditOperationType.DELETE) {

                    enforce(qtyOpt.isEmpty());
                    ingredients.remove(existing);
                }
            }
        }

        return Result.of(ingredients);
    }


    private Result<List<Step>> performStepEdits(List<StepEditDescriptor> edits) {

        var steps = new ArrayList<>(this.recipe.getSteps());

        var outOfRangeMsg = (BiFunction<Integer, Integer, String>) (idx, sz) -> {
            return String.format("Step number '%d' is out of range%s", idx,
                steps.isEmpty() ? " (recipe has no steps)" : String.format(" (should be between 1 and %d)", sz)
            );
        };


        for (var edit : edits) {
            var type = edit.getEditType();
            var step = edit.getStepText();
            var numOpt = edit.getStepNumber().map(x -> x - 1);

            enforceContains(type, EditOperationType.ADD, EditOperationType.EDIT, EditOperationType.DELETE);

            if (type == EditOperationType.ADD) {
                int idx = steps.size();


                if (numOpt.isPresent()) {
                    // numOpt has already been converted to 0-index by this point.
                    if (numOpt.get() <= steps.size()) {
                        idx = numOpt.get();
                    } else {
                        // when adding steps, the "should be between" part needs to have the higher
                        // bound raised by 1, since you should be allowed to /step:add:3 to a recipe
                        // with 2 steps, for example.
                        return Result.error(outOfRangeMsg.apply(1 + numOpt.get(), steps.size() + 1));
                    }
                }

                steps.add(idx, new Step(step));

            } else {
                enforcePresent(numOpt);

                int index = numOpt.get();
                enforce(index >= 0);

                if (index >= steps.size()) {
                    return Result.error(outOfRangeMsg.apply(1 + index, steps.size()));
                }

                if (type == EditOperationType.EDIT) {

                    steps.set(index, new Step(step));

                } else if (type == EditOperationType.DELETE) {

                    steps.remove(index);

                } else {
                    return Result.error("Cannot %s steps", type);
                }
            }
        }

        return Result.of(steps);
    }

    private Result<Set<Tag>> performTagEdits(List<TagEditDescriptor> edits) {

        var tags = new HashSet<>(this.recipe.getTags());

        for (var edit : edits) {

            var tagName = edit.getTagName();

            enforceContains(edit.getEditType(), EditOperationType.ADD, EditOperationType.DELETE);

            if (edit.getEditType() == EditOperationType.ADD) {

                if (!tags.add(new Tag(tagName))) {
                    return Result.error("Recipe '%s' already has tag '%s'", this.recipe.getName(),
                            tagName);
                }

            } else if (edit.getEditType() == EditOperationType.DELETE) {

                var opt = tags.stream().filter(t -> t.equals(tagName)).findFirst();
                if (opt.isEmpty()) {
                    return Result.error("Recipe '%s' does not have tag '%s'",
                        this.recipe.getName(), tagName);
                }

                tags.remove(opt.get());

            }
        }

        return Result.of(tags);
    }


    @Override
    public CommandResult undo(Model model) {
        enforceNonNull(model);

        model.setRecipe(this.editedRecipe, this.recipe);
        return CommandResult.message("Undo: un-edited recipe '%s'", this.recipe.getName())
            .showingRecipe(this.recipe);
    }

    @Override
    public String toString() {
        return String.format("EditRecipeCommand(%s)", this.item);
    }

    public static String getCommandString() {
        return "edit recipe";
    }

    public static String getCommandHelp() {
        return "Edits an existing recipe";
    }
}
