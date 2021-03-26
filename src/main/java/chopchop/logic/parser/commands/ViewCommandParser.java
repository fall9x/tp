package chopchop.logic.parser.commands;

import java.util.Optional;

import chopchop.logic.commands.Command;
import chopchop.logic.commands.ViewRecipeCommand;
import chopchop.logic.parser.CommandArguments;
import chopchop.logic.parser.ItemReference;
import chopchop.commons.util.Result;

import static chopchop.commons.util.Strings.COMMAND_VIEW;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;

public class ViewCommandParser {

    /**
     * Parses a 'view' command. Syntax:
     * {@code view recipe REF}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     a ViewCommand , if the input was valid.
     */
    public static Result<? extends Command> parseViewCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_VIEW);

        // we expect no named arguments
        Optional<String> err;
        if ((err = checkArguments(args, "view")).isPresent()) {
            return Result.error(err.get());
        }

        return getCommandTarget(args)
            .then(target -> {
                if (target.fst() != CommandTarget.RECIPE) {
                    return Result.error("Only recipes can be viewed");
                } else if (target.snd().isEmpty()) {
                    return Result.error("Recipe name cannot be empty");
                }

                return ItemReference.parse(target.snd()).map(ViewRecipeCommand::new);
            });
    }
}
