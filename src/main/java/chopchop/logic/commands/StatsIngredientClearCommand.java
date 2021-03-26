package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;
import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.UsageList;
import chopchop.model.usage.IngredientUsage;

public class StatsIngredientClearCommand extends Command implements Undoable {

    private UsageList<IngredientUsage> usages = new UsageList<>();

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

        this.usages = model.getIngredientUsageList();
        model.setIngredientUsageList(new UsageList<>());
        return CommandResult.message("Cleared ingredient usage history");
    }

    /**
     * Undo the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     */
    @Override
    public CommandResult undo(Model model) {
        requireNonNull(model);

        model.setIngredientUsageList(this.usages);
        this.usages.setAll(new UsageList<>());

        return CommandResult.message("Undo: restored history of ingredients used");
    }

    @Override
    public String toString() {
        return "StatsIngredientClearCommand";
    }

    public static String getCommandString() {
        return "stats ingredient clear";
    }

    public static String getCommandHelp() {
        return "Clears all records of ingredients used";
    }
}
