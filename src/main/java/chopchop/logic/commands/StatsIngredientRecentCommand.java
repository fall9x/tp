package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

public class StatsIngredientRecentCommand extends Command {
    private static final int N_MOST_RECENT = 10;

    /**
     * Executes the command and returns the result message.
     *
     * @param model          {@code Model} which the command should operate on.
     * @param historyManager {@code History} which the command should record to.
     * @return feedback message of the operation result for display
     */
    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        var output = model.getRecentlyUsedIngredients(N_MOST_RECENT);
        return CommandResult.statsMessage(output, output.isEmpty()
            ? "No ingredients were used recently"
            : "Here are your recently used ingredients");
    }

    @Override
    public String toString() {
        return "StatsIngredientRecentCommand";
    }

    public static String getCommandString() {
        return "stats ingredient recent";
    }

    public static String getCommandHelp() {
        return "Shows the ingredients that were recently used by recipes that were made";
    }
}
