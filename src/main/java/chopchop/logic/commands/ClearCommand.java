// ClearCommand.java

package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.ItemReference;
import chopchop.model.Model;

public class ClearCommand extends Command implements Undoable {

    private final List<Undoable> commands = new ArrayList<>();

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model, historyManager);

        // clear all recipes:
        model.getRecipeBook().getEntryList().stream().forEach(x -> {
            this.commands.add(new DeleteRecipeCommand(ItemReference.ofZeroIndex(0)));
        });

        // clear all ingredients:
        model.getIngredientBook().getEntryList().stream().forEach(x -> {
            this.commands.add(new DeleteIngredientCommand(ItemReference.ofZeroIndex(0), Optional.empty()));
        });

        // clear usages:
        this.commands.add(new StatsRecipeClearCommand());
        this.commands.add(new StatsIngredientClearCommand());


        // execute them all
        this.commands.stream().forEach(cmd -> {
            cmd.execute(model, historyManager);
        });

        return CommandResult.message("Cleared all data")
            .showingRecipeList();
    }

    @Override
    public CommandResult undo(Model model) {
        enforceNonNull(model);

        Collections.reverse(this.commands);
        this.commands.stream().forEach(cmd -> {
            cmd.undo(model);
        });

        return CommandResult.message("Undo: restored all data")
            .showingRecipeList();
    }

    @Override
    public String toString() {
        return "ClearCommand";
    }

    public static String getCommandString() {
        return "clear";
    }

    public static String getCommandHelp() {
        return "Clears all data, including recipes, ingredients, and statistics";
    }
}
