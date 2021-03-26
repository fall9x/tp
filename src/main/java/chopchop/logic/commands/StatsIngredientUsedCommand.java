package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;
import static chopchop.logic.commands.StatsRecipeMadeCommand.checkValidDateRange;
import static chopchop.logic.commands.StatsRecipeMadeCommand.formatSubtitle;

import java.time.LocalDateTime;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

public class StatsIngredientUsedCommand extends Command {

    private final LocalDateTime before;
    private final LocalDateTime after;

    /**
     * Creates an StatsIngredientCommand to add the specified {@code Command}.
     * If both before and after are not specified. It is assumed that the time frame is today.
     */
    public StatsIngredientUsedCommand(LocalDateTime after, LocalDateTime before) {
        if (before == null && after == null) {
            var now = LocalDateTime.now();
            this.after = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
            this.before = this.after.plusDays(1);
        } else {
            this.before = before;
            this.after = after;
        }
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        var output = model.getIngredientsUsedBetween(after, before);

        if (!checkValidDateRange(this.after, this.before)) {
            return CommandResult.error("'after' date cannot be later than 'before' date");
        }

        return CommandResult.statsMessage(output,
            formatSubtitle(output.isEmpty(), "ingredients", "used", this.after, this.before)
        );
    }

    @Override
    public String toString() {
        return String.format("StatsIngredientUsedCommand");
    }

    public static String getCommandString() {
        return "stats ingredient used";
    }

    public static String getCommandHelp() {
        return "Shows ingredients used by recipes that were made in a given time frame";
    }
}
