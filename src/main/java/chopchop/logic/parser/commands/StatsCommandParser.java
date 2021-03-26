// StatsCommandParser.java
//@@author trav1sT

package chopchop.logic.parser.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import chopchop.commons.util.Pair;
import chopchop.commons.util.Result;
import chopchop.commons.util.StringView;
import chopchop.logic.commands.Command;
import chopchop.logic.commands.StatsIngredientClearCommand;
import chopchop.logic.commands.StatsIngredientRecentCommand;
import chopchop.logic.commands.StatsIngredientUsedCommand;
import chopchop.logic.commands.StatsRecipeClearCommand;
import chopchop.logic.commands.StatsRecipeMadeCommand;
import chopchop.logic.commands.StatsRecipeRecentCommand;
import chopchop.logic.commands.StatsRecipeTopCommand;
import chopchop.logic.parser.CommandArguments;

import static chopchop.commons.util.Strings.ARG_AFTER;
import static chopchop.commons.util.Strings.ARG_BEFORE;
import static chopchop.commons.util.Strings.COMMAND_STATS;
import static chopchop.commons.util.Strings.STATS_KIND_CLEAR;
import static chopchop.commons.util.Strings.STATS_KIND_MADE;
import static chopchop.commons.util.Strings.STATS_KIND_RECENT;
import static chopchop.commons.util.Strings.STATS_KIND_TOP;
import static chopchop.commons.util.Strings.STATS_KIND_USED;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;

public class StatsCommandParser {

    /**
     * Parses a 'stats' command. Syntax(es):
     */
    public static Result<? extends Command> parseStatsCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_STATS);

        return getCommandTarget(args, /* acceptsPlural: */ true)
            .then(target -> {
                var words = new StringView(target.snd()).words();

                switch (target.fst()) {
                case RECIPE:
                    return parseRecipeStatsCommand(args, target.snd().strip());

                case INGREDIENT:
                    return parseIngredientStatsCommand(args, target.snd().strip());

                default:
                    return Result.error("Can only find stats of recipes or ingredients ('%s' invalid)", target.fst());
                }
            });
    }

    private static Result<? extends Command> parseRecipeStatsCommand(CommandArguments args, String kind) {
        var c = "stats recipe";
        switch (kind) {
        case STATS_KIND_TOP:
            return ensureNoArgs(args, c + " " + STATS_KIND_TOP, new StatsRecipeTopCommand());

        case STATS_KIND_CLEAR:
            return ensureNoArgs(args, c + " " + STATS_KIND_CLEAR, new StatsRecipeClearCommand());

        case STATS_KIND_RECENT:
            return ensureNoArgs(args, c + " " + STATS_KIND_RECENT, new StatsRecipeRecentCommand());

        case STATS_KIND_MADE:
            return parseDateCommand("recipe", args, (aft, bef) -> new StatsRecipeMadeCommand(aft, bef));

        default:
            return Result.error("Expected one of 'top', 'made', 'recent', or 'clear'"
                + " after '%s' (found '%s')", c, kind);
        }
    }

    private static Result<? extends Command> parseIngredientStatsCommand(CommandArguments args, String kind) {
        var c = "stats ingredient";

        switch (kind) {
        case STATS_KIND_CLEAR:
            return ensureNoArgs(args, c + " " + STATS_KIND_CLEAR, new StatsIngredientClearCommand());

        case STATS_KIND_RECENT:
            return ensureNoArgs(args, c + " " + STATS_KIND_RECENT, new StatsIngredientRecentCommand());

        case STATS_KIND_USED:
            return parseDateCommand("ingredient", args, (aft, bef) -> new StatsIngredientUsedCommand(aft, bef));

        default:
            return Result.error("Expected one of 'used', 'recent', or 'clear'"
                + " after '%s' (found '%s')", c, kind);
        }
    }









    private static Result<Command> parseDateCommand(String kind, CommandArguments args,
        BiFunction<LocalDateTime, LocalDateTime, Command> constructor) {

        Optional<String> err;
        var supportedArgs = List.of(ARG_BEFORE, ARG_AFTER);
        if ((err = checkArguments(args, String.format("stats %s", kind), supportedArgs)).isPresent()) {
            return Result.error(err.get());
        }

        var afters = args.getArgument(ARG_AFTER);
        var befores = args.getArgument(ARG_BEFORE);

        if (befores.size() > 1 || afters.size() > 1) {
            return Result.error("Multiple dates specified");
        }

        return Result.transpose(Result.listToOptional(afters).map(x -> processDate(x, "after")))
            .then(aft -> {
                return Result.transpose(Result.listToOptional(befores).map(x -> processDate(x, "before")))
                    .map(bef -> Pair.of(aft.orElse(null), bef.orElse(null)));
            })
            .map(p -> constructor.apply(p.fst(), p.snd()));
    }

    private static Result<LocalDateTime> processDate(String input, String kind) {

        if (input.isEmpty()) {
            return Result.error("date (for '/%s') cannot be empty", kind);
        }

        var timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (input.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                return Result.of(LocalDateTime.of(LocalDate.parse(input, formatter), LocalTime.of(0, 0)));
            } else {
                return Result.of(LocalDateTime.parse(input, timeFormatter));
            }
        } catch (DateTimeParseException e) {
            return Result.error("Unable to parse date/time (for '/%s'): %s", kind, e.getMessage());
        }
    }

    private static Result<? extends Command> ensureNoArgs(CommandArguments args, String cmd, Command ret) {
        Optional<String> err;
        if ((err = checkArguments(args, cmd)).isPresent()) {
            return Result.error(err.get());
        }

        return Result.of(ret);
    }
}
