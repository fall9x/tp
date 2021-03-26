package chopchop.logic.commands;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

/**
 * Represents an undoable command with the ability to be undone and redone.
 */
public interface Undoable {
    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param historyManager {@code HistoryManager} which the command should record to.
     * @return feedback message of the operation result for display
     */
    CommandResult execute(Model model, HistoryManager historyManager);

    /**
     * Undo the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     */
    CommandResult undo(Model model);

    /**
     * Redo the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param historyManager {@code HistoryManager} which the command should record to.
     * @return feedback message of the operation result for display
     */
    default CommandResult redo(Model model, HistoryManager historyManager) {
        return this.execute(model, historyManager);
    }
}
