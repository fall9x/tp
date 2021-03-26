// ListCommandParser.java

package chopchop.logic.parser.commands;

import java.util.Optional;

import chopchop.commons.util.Result;
import chopchop.logic.parser.CommandArguments;
import chopchop.logic.commands.Command;
import chopchop.logic.commands.ListRecipeCommand;
import chopchop.logic.commands.ListIngredientCommand;
import chopchop.logic.commands.ListRecommendationCommand;

import static chopchop.commons.util.Strings.COMMAND_LIST;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;

public class ListCommandParser {

    /**
     * Parses a 'list' command. Syntax(es):
     * {@code list recipe}
     * {@code list ingredient}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     a ListCommand, if the input was valid.
     */
    public static Result<? extends Command> parseListCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_LIST);

        // we expect no named arguments
        Optional<String> err;
        if ((err = checkArguments(args, "list")).isPresent()) {
            return Result.error(err.get());
        }

        return getCommandTarget(args, /* acceptsPlural: */ true)
            .then(target -> {
                switch (target.fst()) {
                case RECIPE:
                    return Result.of(new ListRecipeCommand());

                case INGREDIENT:
                    return Result.of(new ListIngredientCommand());

                case RECOMMENDATION:
                    return Result.of(new ListRecommendationCommand());

                default:
                    return Result.error("Can only list recipes, ingredients, or recommendations ('%s' invalid)",
                            target.fst());
                }
            });
    }
}
