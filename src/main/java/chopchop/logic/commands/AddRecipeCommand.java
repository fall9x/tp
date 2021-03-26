package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.attributes.Step;
import chopchop.model.attributes.Tag;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.recipe.Recipe;

/**
 * Adds a recipe to the recipe book.
 */
public class AddRecipeCommand extends Command implements Undoable {

    private Recipe recipe;

    private final String name;
    private final List<IngredientReference> ingredients;
    private final List<Step> steps;
    private final Set<Tag> tags;

    /**
     * Creates a command to add a recipe with the following name, steps, ingredients, and tags.
     */
    public AddRecipeCommand(String name, List<IngredientReference> ingredients, List<Step> steps, Set<Tag> tags) {

        this.recipe = null;

        this.name = name;
        this.ingredients = new ArrayList<>(ingredients);
        this.steps = new ArrayList<>(steps);
        this.tags = new HashSet<>(tags);
    }


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        if (model.findRecipeWithName(this.name).isPresent()) {
            return CommandResult.error("Recipe '%s' already exists", this.name);
        }

        // first, ensure that the ingredients are unique.
        {
            var seenIngredients = new HashSet<String>();
            for (var ingr : this.ingredients) {
                if (seenIngredients.contains(ingr.getName())) {
                    return CommandResult.error("Ingredient '%s' was specified twice", ingr.getName());
                }

                if (ingr.getQuantity().isZero()) {
                    return CommandResult.error("Quantity should not be zero (for ingredient '%s')", ingr.getName());
                }

                seenIngredients.add(ingr.getName());
            }
        }

        // then make the recipe.
        this.recipe = new Recipe(this.name, this.ingredients, this.steps, this.tags);

        model.addRecipe(this.recipe);

        return CommandResult.message("Added recipe '%s'", this.recipe.getName())
            .showingRecipe(this.recipe);
    }




    @Override
    public CommandResult undo(Model model) {

        enforceNonNull(model);
        enforceNonNull(this.recipe);

        model.deleteRecipe(this.recipe);
        return CommandResult.message("Undo: removed recipe '%s'", this.recipe.getName())
            .showingRecipeList();
    }

    @Override
    public String toString() {
        return String.format("AddRecipeCommand(%s, ingr: [%s], steps: [%s])", this.name,
            String.join(", ", this.ingredients.stream().map(x -> x.toString()).collect(Collectors.toList())),
            String.join(", ", this.steps.stream().map(x -> x.toString()).collect(Collectors.toList())));
    }


    public static String getCommandString() {
        return "add recipe";
    }

    public static String getCommandHelp() {
        return "Adds a new recipe";
    }
}


