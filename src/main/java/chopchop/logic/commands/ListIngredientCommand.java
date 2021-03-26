package chopchop.logic.commands;

import static chopchop.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

/**
 * Lists all ingredients in the ingredient book to the user.
 */
public class ListIngredientCommand extends Command {

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredIngredientList(PREDICATE_SHOW_ALL_ENTRIES);

        return CommandResult.message("Listed ingredients")
            .showingIngredientList();
    }

    @Override
    public String toString() {
        return String.format("ListIngredientCommand");
    }

    public static String getCommandString() {
        return "list ingredient";
    }

    public static String getCommandHelp() {
        return "Lists all ingredients, clearing any search filters";
    }
}
