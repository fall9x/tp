// FindCommandParser.java

package chopchop.logic.parser.commands;

import java.util.Optional;

import chopchop.commons.util.Result;
import chopchop.commons.util.StringView;
import chopchop.logic.parser.CommandArguments;
import chopchop.logic.commands.Command;
import chopchop.logic.commands.FindRecipeCommand;
import chopchop.logic.commands.FindIngredientCommand;
import chopchop.model.attributes.NameContainsKeywordsPredicate;

import static chopchop.commons.util.Strings.COMMAND_FIND;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;

public class FindCommandParser {

    /**
     * Parses a 'find' command. Syntax(es):
     * {@code find recipe (keywords)+}
     * {@code find ingredient (keywords)+}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     a FindCommand, if the input was valid.
     */
    public static Result<? extends Command> parseFindCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_FIND);

        // we expect no named arguments. note we don't need to check for augments.
        Optional<String> err;
        if ((err = checkArguments(args, "find")).isPresent()) {
            return Result.error(err.get());
        }

        return getCommandTarget(args, /* acceptsPlural: */ true)
            .then(target -> {
                var words = new StringView(target.snd()).words();

                if (words.isEmpty()) {
                    return Result.error("'find' command requires at least one search term");
                }

                switch (target.fst()) {
                case RECIPE:
                    return Result.of(new FindRecipeCommand(new NameContainsKeywordsPredicate(words)));

                case INGREDIENT:
                    return Result.of(new FindIngredientCommand(new NameContainsKeywordsPredicate(words)));

                default:
                    return Result.error("Can only find recipes or ingredients ('%s' invalid)", target.fst());
                }
            });
    }

}
