package chopchop.logic.commands;

import static chopchop.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

/**
 * Lists all recipes in the recipe book to the user.
 */
public class ListRecipeCommand extends Command {

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredRecipeList(PREDICATE_SHOW_ALL_ENTRIES);

        return CommandResult.message("Listed recipes")
            .showingRecipeList();
    }

    @Override
    public String toString() {
        return String.format("ListRecipeCommand");
    }

    public static String getCommandString() {
        return "list recipe";
    }

    public static String getCommandHelp() {
        return "Lists all recipes, clearing any search filters";
    }
}
