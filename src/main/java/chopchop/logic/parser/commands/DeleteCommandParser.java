// DeleteCommandParser.java

package chopchop.logic.parser.commands;

import java.util.List;
import java.util.Optional;

import chopchop.commons.util.Result;
import chopchop.logic.parser.ItemReference;
import chopchop.logic.parser.CommandArguments;
import chopchop.logic.commands.Command;
import chopchop.logic.commands.DeleteRecipeCommand;
import chopchop.logic.commands.DeleteIngredientCommand;

import static chopchop.commons.util.Strings.ARG_QUANTITY;
import static chopchop.commons.util.Strings.COMMAND_DELETE;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;

public class DeleteCommandParser {
    /**
     * Parses a 'delete' command. Syntax(es):
     * {@code delete recipe REF}
     * {@code delete ingredient REF [/qty QUANTITY]}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     a DeleteCommand, if the input was valid.
     */
    public static Result<? extends Command> parseDeleteCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_DELETE);

        return getCommandTarget(args)
            .then(target -> {
                if (target.snd().isEmpty()) {
                    return Result.error("Recipe or ingredient name cannot be empty");
                }

                switch (target.fst()) {
                case RECIPE:
                    return parseDeleteRecipeCommand(target.snd().strip(), args);

                case INGREDIENT:
                    return parseDeleteIngredientCommand(target.snd().strip(), args);

                default:
                    return Result.error("Can only delete recipes or ingredients ('%s' invalid)", target.fst());
                }
            });
    }

    /**
     * Parses a 'delete ingredient' command. Syntax:
     * {@code delete ingredient REF [/qty QUANTITY]}
     */
    private static Result<DeleteIngredientCommand> parseDeleteIngredientCommand(String name, CommandArguments args) {

        Optional<String> err;
        if ((err = checkArguments(args, "delete ingredient", List.of(ARG_QUANTITY))).isPresent()) {
            return Result.error(err.get());
        }

        var qtys = args.getArgument(ARG_QUANTITY);
        if (qtys.size() > 1) {
            return Result.error("Multiple quantities specified");
        } else if (qtys.size() == 1 && qtys.get(0).isEmpty()) {
            return Result.error("Specified quantity cannot be empty");
        }

        return ItemReference.parse(name)
            .then(ref -> Result.transpose(qtys
                .stream()
                .findFirst()
                .map(CommonParser::parseQuantity))
                .map(qty -> new DeleteIngredientCommand(ref, qty)));
    }

    /**
     * Parses a 'delete recipe' command. Syntax:
     * {@code delete recipe REF}
     */
    private static Result<DeleteRecipeCommand> parseDeleteRecipeCommand(String name, CommandArguments args) {

        Optional<String> err;
        if ((err = checkArguments(args, "delete recipe")).isPresent()) {
            return Result.error(err.get());
        }

        return ItemReference.parse(name).map(DeleteRecipeCommand::new);
    }
}
