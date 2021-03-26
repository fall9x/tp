package guitests.guihandles;

import javafx.scene.control.TextArea;

/**
 * A handle to the {@code TextDisplay} in the GUI.
 */
public class TextDisplayHandle extends NodeHandle<TextArea> {

    public static final String RESULT_DISPLAY_ID = "#textDisplay";

    /**
     * Constructs a {@code commandOutputHandle} with the given {@code textDisplayNode}.
     */
    public TextDisplayHandle(TextArea textDisplayNode) {
        super(textDisplayNode);
    }

    /**
     * Returns the text in the text display.
     */
    public String getText() {
        return getRootNode().getText();
    }
}
