//@@author fall9x

package chopchop.ui;

import static java.util.Objects.requireNonNull;

import chopchop.logic.commands.CommandResult;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandOutput extends UiPart<Region> {
    private static final String FXML = "CommandOutput.fxml";

    @FXML
    private TextFlow displayBox;

    /**
     * Constructs {@code CommandBox}
     */
    public CommandOutput() {
        super(FXML);
    }

    /**
     * Clear wtv is displayed.
     */
    public void clear() {
        var texts = displayBox.getChildren();
        texts.clear();
    }

    /**
     * Displays the commandResult to the user.
     *
     * @param result the command result
     */
    public void setFeedbackToUser(CommandResult result) {
        requireNonNull(result);

        var texts = this.displayBox.getChildren();
        texts.clear();

        var shouldUnCaps = false;

        if (result.isError()) {
            var foo = new Text("Error: ");
            foo.setStyle("-fx-font-family: 'Source Sans Pro SemiBold'");
            foo.setFill(Color.valueOf("#ff1744"));

            texts.add(foo);
            shouldUnCaps = true;
        }


        for (var part : result.getParts()) {
            var msg = part.getText();

            if (shouldUnCaps) {
                shouldUnCaps = false;
                if (msg.length() > 0) {
                    msg = msg.substring(0, 1).toLowerCase() + msg.substring(1);
                }
            }

            if (part.isLink()) {
                texts.add(new Text(" "));
                texts.add(new ClickableLink(msg, part.getUrl()));
                texts.add(new Text(" "));
            } else {
                texts.add(new Text(msg));
            }

            if (part.appendNewline()) {
                texts.add(new Text("\n"));
            }
        }
    }
}
