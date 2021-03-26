package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceContains;
import static chopchop.commons.util.Enforce.enforceNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chopchop.commons.util.Result;
import chopchop.logic.edit.EditOperationType;
import chopchop.logic.edit.IngredientEditDescriptor;
import chopchop.logic.edit.TagEditDescriptor;
import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.ItemReference;
import chopchop.model.Model;
import chopchop.model.attributes.Tag;
import chopchop.model.ingredient.Ingredient;

/**
 * Edits an ingredient identified using it's displayed index or name from the ingredient book.
 */
public class EditIngredientCommand extends Command implements Undoable {
    private final ItemReference item;
    private final IngredientEditDescriptor ingredientEditDescriptor;
    private Ingredient ingredient;
    private Ingredient editedIngredient;

    /**
     * Constructs a command that edits the given ingredient item.
     */
    public EditIngredientCommand(ItemReference item, IngredientEditDescriptor ingredientEditDescriptor) {
        enforceNonNull(item, ingredientEditDescriptor);

        this.item = item;
        this.ingredientEditDescriptor = ingredientEditDescriptor;
    }


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        var res = resolveIngredientReference(this.item, model);
        if (res.isError()) {
            return CommandResult.error(res.getError());
        }

        this.ingredient = res.getValue();

        var ied = this.ingredientEditDescriptor;
        if (ied.getTagEdits().isEmpty()) {
            return CommandResult.message("No edits provided; ingredient '%s' was not modified",
                this.ingredient.getName());
        }

        var newTags = performTagEdits(ied.getTagEdits());

        var foo = Result.firstError(newTags);
        if (foo.isPresent()) {
            return CommandResult.error(foo.get());
        }

        this.editedIngredient = new Ingredient(this.ingredient.getName(),
            this.ingredient.getIngredientSets(),
            newTags.getValue());

        model.setIngredient(this.ingredient, this.editedIngredient);
        return CommandResult.message("Edited ingredient '%s'", this.ingredient.getName())
            .showingIngredientList();
    }

    private Result<Set<Tag>> performTagEdits(List<TagEditDescriptor> edits) {

        var tags = new HashSet<>(this.ingredient.getTags());

        for (var edit : edits) {

            var tagName = edit.getTagName();

            enforceContains(edit.getEditType(), EditOperationType.ADD, EditOperationType.DELETE);

            if (edit.getEditType() == EditOperationType.ADD) {

                if (!tags.add(new Tag(tagName))) {
                    return Result.error("Ingredient '%s' already has tag '%s'", this.ingredient.getName(),
                            tagName);
                }

            } else if (edit.getEditType() == EditOperationType.DELETE) {

                var opt = tags.stream().filter(t -> t.equals(tagName)).findFirst();
                if (opt.isEmpty()) {
                    return Result.error("Ingredient '%s' does not have tag '%s'",
                        this.ingredient.getName(), tagName);
                }

                tags.remove(opt.get());

            }
        }

        return Result.of(tags);
    }


    @Override
    public CommandResult undo(Model model) {
        enforceNonNull(model);

        model.setIngredient(this.editedIngredient, this.ingredient);
        return CommandResult.message("Undo: un-edited ingredient '%s'", this.ingredient.getName())
            .showingIngredientList();
    }

    @Override
    public String toString() {
        return String.format("EditIngredientCommand(%s)", this.item);
    }

    public static String getCommandString() {
        return "edit ingredient";
    }

    public static String getCommandHelp() {
        return "Edits an existing ingredient";
    }
}
