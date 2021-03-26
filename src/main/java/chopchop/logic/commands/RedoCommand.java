package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

/**
 * Redo the last redoable command.
 */
public class RedoCommand extends Command {

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        requireNonNull(historyManager);

        return historyManager.redo(model);
    }

    @Override
    public String toString() {
        return String.format("RedoCommand");
    }

    public static String getCommandString() {
        return "redo";
    }

    public static String getCommandHelp() {
        return "Redoes the last performed command";
    }
}
