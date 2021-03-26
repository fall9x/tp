package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.ItemReference;
import chopchop.model.Model;
import chopchop.model.recipe.Recipe;

/**
 * Deletes a recipe identified using it's displayed index or name from the recipe book.
 */
public class DeleteRecipeCommand extends Command implements Undoable {

    private final ItemReference item;
    private Recipe recipe;

    /**
     * Constructs a command that deletes the given recipe item.
     */
    public DeleteRecipeCommand(ItemReference item) {
        requireNonNull(item);
        this.item = item;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);

        var res = resolveRecipeReference(this.item, model);
        if (res.isError()) {
            return CommandResult.error(res.getError());
        }

        this.recipe = res.getValue();

        model.deleteRecipe(this.recipe);
        return CommandResult.message("Deleted recipe '%s'", this.recipe.getName())
            .showingRecipeList();
    }

    @Override
    public CommandResult undo(Model model) {
        requireNonNull(model);

        model.addRecipe(this.recipe);
        return CommandResult.message("Undo: re-added recipe '%s'", this.recipe.getName())
            .showingRecipe(this.recipe);
    }

    @Override
    public String toString() {
        return String.format("DeleteRecipeCommand(%s)", this.item);
    }

    public static String getCommandString() {
        return "delete recipe";
    }

    public static String getCommandHelp() {
        return "Deletes a recipe";
    }
}
