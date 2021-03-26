package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

public class StatsRecipeMadeCommand extends Command {

    private final LocalDateTime before;
    private final LocalDateTime after;

    /**
     * Creates an StatsRecipeDateCommand to add the specified {@code Ingredient}.
     * If both before and after are not specified, it is assumed that the time frame is today.
     */
    public StatsRecipeMadeCommand(LocalDateTime after, LocalDateTime before) {
        if (after == null && before == null) {
            var now = LocalDateTime.now();
            this.after = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
            this.before = this.after.plusDays(1);
        } else {
            this.after = after;
            this.before = before;
        }
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        var output = model.getRecipesMadeBetween(after, before);

        if (!checkValidDateRange(this.after, this.before)) {
            return CommandResult.error("'after' date cannot be later than 'before' date");
        }

        return CommandResult.statsMessage(output,
            formatSubtitle(output.isEmpty(), "recipes", "made", this.after, this.before)
        );
    }

    @Override
    public String toString() {
        return String.format("StatsRecipeMadeCommand");
    }

    public static String getCommandString() {
        return "stats recipe made";
    }

    public static String getCommandHelp() {
        return "Shows recipes that were made in a given time frame";
    }

    /**
     * Formats the subtitle according to the given parameters.
     * This method is also used by StatsIngredientUsedCommand.
     */
    static String formatSubtitle(boolean isEmpty, String item, String verb,
        LocalDateTime after, LocalDateTime before) {

        var onFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (before != null && after != null) {
            var lower = after.format(formatter);
            var upper = before.format(formatter);
            var thisDay = after.format(onFormatter);

            if (after.getSecond() + after.getMinute() + after.getHour() == 0
                && after.plusDays(1).equals(before)) {
                return String.format(isEmpty ? "No %s were %s on %s"
                                             : "Showing %s %s on %s", item, verb, thisDay);
            } else {
                return String.format(isEmpty ? "No %s were %s between %s and %s"
                                             : "Showing %s %s between %s and %s", item, verb, lower, upper);
            }
        } else if (before != null) {
            var upper = before.format(formatter);
            return String.format(isEmpty ? "No %s were %s before %s"
                                         : "Showing %s %s before %s", item, verb, upper);
        } else {
            var lower = after.format(formatter);
            return String.format(isEmpty ? "No %s were %s after %s"
                                         : "Showing %s %s after %s", item, verb, lower);
        }
    }

    /**
     * Checks whether the date range is valid, ie. whether lower is before upper.
     */
    static boolean checkValidDateRange(LocalDateTime lower, LocalDateTime upper) {
        if (upper == null || lower == null) {
            return true;
        }
        return upper.isAfter(lower);
    }
}
