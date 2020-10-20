package chopchop.logic.commands;

import static chopchop.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static java.util.Objects.requireNonNull;

import chopchop.model.Model;

/**
 * Lists all ingredients in the ingredient book to the user.
 */
public class ListIngredientCommand extends Command {

    public static final String MESSAGE_SUCCESS = "Listed all ingredients";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredIngredientList(PREDICATE_SHOW_ALL_ENTRIES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
