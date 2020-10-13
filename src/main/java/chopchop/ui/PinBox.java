package chopchop.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * The UI component that is responsible for displaying pinned information.
 */
public class PinBox extends UiPart<Region> {

    private static final String FXML = "PinBox.fxml";

    @FXML
    private TextArea pins;

    /**
     * Creates a {@code PinBox}.
     */
    public PinBox() {
        super(FXML);
        // Have to decide what to do with this real estate uwu
        pins.setText("Pins\n To be implemented.");
    }
}
