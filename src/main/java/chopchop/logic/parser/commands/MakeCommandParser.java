package chopchop.logic.parser.commands;

import java.util.Optional;

import chopchop.commons.util.Result;
import chopchop.logic.commands.Command;
import chopchop.logic.commands.MakeRecipeCommand;
import chopchop.logic.parser.CommandArguments;
import chopchop.logic.parser.ItemReference;

import static chopchop.commons.util.Strings.COMMAND_MAKE;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;

public class MakeCommandParser {

    /**
     * Parses a 'make' command. Syntax:
     * {@code make recipe REF}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     a MakeCommand, if the input was valid.
     */
    public static Result<? extends Command> parseMakeCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_MAKE);

        // we expect no named arguments
        Optional<String> err;
        if ((err = checkArguments(args, "make")).isPresent()) {
            return Result.error(err.get());
        }

        return getCommandTarget(args)
            .then(target -> {
                if (target.fst() != CommandTarget.RECIPE) {
                    return Result.error("Only recipes can be made");
                } else if (target.snd().isEmpty()) {
                    return Result.error("Recipe name cannot be empty");
                }

                return ItemReference.parse(target.snd()).map(MakeRecipeCommand::new);
            });
    }
}
