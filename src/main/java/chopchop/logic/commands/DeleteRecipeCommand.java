package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import chopchop.commons.core.Messages;
import chopchop.logic.commands.exceptions.CommandException;
import chopchop.logic.parser.ItemReference;
import chopchop.model.Model;
import chopchop.model.recipe.Recipe;

/**
 * Deletes a recipe identified using it's displayed index from the recipe book.
 */
public class DeleteRecipeCommand extends Command {

    public static final String MESSAGE_USAGE = "delete recipe"
            + ": Deletes the recipe identified by the index number used in the displayed recipe list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + "delete recipe" + " 1";

    public static final String MESSAGE_DELETE_RECIPE_SUCCESS = "Deleted Recipe: %1$s";

    private final ItemReference item;

    /**
     * Constructs a command that deletes the given recipe item.
     */
    public DeleteRecipeCommand(ItemReference item) {
        requireNonNull(item);
        this.item = item;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Recipe recipeToDelete = null;

        if (this.item.isIndexed()) {
            var lastShownList = model.getFilteredRecipeList();

            if (item.getZeroIndex() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
            }

            recipeToDelete = lastShownList.get(this.item.getZeroIndex());
        } else {

            recipeToDelete = model
                .findRecipeWithName(this.item.getName())
                .orElseThrow(() -> new CommandException(String.format("no recipe named '%s'", this.item.getName())));
        }

        model.deleteRecipe(recipeToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_RECIPE_SUCCESS, recipeToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteRecipeCommand // instanceof handles nulls
                && item.equals(((DeleteRecipeCommand) other).item)); // state check
    }

}
