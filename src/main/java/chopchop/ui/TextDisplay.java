//@@author fall9x

package chopchop.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Class for showing prompts to user when the main display area is empty.
 */
public class TextDisplay extends UiPart<Region> {
    private static final String FXML = "TextDisplay.fxml";

    @FXML
    private Label textDisplay;

    /**
     * Creates a {@code PinBox}.
     */
    public TextDisplay(String text) {
        super(FXML);
        // Have to decide what to do with this real estate uwu
        this.textDisplay.setText(text);
    }
}
