//@@author fall9x

package chopchop.ui;

import chopchop.logic.Logic;
import chopchop.logic.commands.CommandResult;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final Logic logic;

    private int historyPointer;

    @FXML
    private TextField commandTextField;

    /**
     * Constructs {@code CommandBox}
     */
    public CommandBox(CommandExecutor commandExecutor, Logic logic) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.logic = logic;

        // No commands entered yet.
        this.historyPointer = 0;

        this.commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.DOWN) && this.historyPointer < logic.getInputHistory().size()) {
                this.historyPointer++;

                if (this.historyPointer == logic.getInputHistory().size()) {
                    this.commandTextField.clear();
                } else {
                    var command = logic.getInputHistory().get(this.historyPointer);
                    this.commandTextField.setText(command);
                    this.commandTextField.positionCaret(command.length());
                }

                logic.resetCompletionState();
                event.consume();
            } else if (event.getCode().equals(KeyCode.UP) && this.historyPointer > 0) {
                this.historyPointer--;
                var command = logic.getInputHistory().get(this.historyPointer);
                this.commandTextField.setText(command);
                this.commandTextField.positionCaret(command.length());

                logic.resetCompletionState();
                event.consume();
            } else if (event.getCode().equals(KeyCode.TAB)) {
                var text = this.commandTextField.getText();
                var cursor = this.commandTextField.getCaretPosition();

                // first split the thing by cursor position -- only complete the first portion.
                var fst = text.substring(0, cursor);
                var snd = text.substring(cursor);

                var completion = logic.getCompletionForInput(fst);

                // now, we can change the cursor position accordingly.
                cursor = completion.length();

                this.commandTextField.setText(completion + snd);
                this.commandTextField.positionCaret(cursor);

                event.consume();
            } else {
                // when the user presses any other key, just reset the completion state.
                logic.resetCompletionState();
            }
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        var command = this.commandTextField.getText();
        if (!command.isEmpty()) {
            this.commandExecutor.execute(command);
            this.historyPointer = this.logic.getInputHistory().size();
            this.commandTextField.clear();
        }
    }

    protected void setFocus(String keypress) {
        if (!this.commandTextField.isFocused()) {
            this.commandTextField.appendText(keypress);
            this.commandTextField.requestFocus();
            this.commandTextField.end();
        }
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see Logic#execute(String)
         */
        CommandResult execute(String commandText);
    }
}
