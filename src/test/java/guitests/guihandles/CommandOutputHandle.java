package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.text.TextFlow;

import java.util.List;

/**
 * A handle to the {@code CommandOutput} in the GUI.
 */
public class CommandOutputHandle extends NodeHandle<TextFlow> {

    public static final String RESULT_DISPLAY_ID = "#displayBox";

    /**
     * Constructs a {@code commandOutputHandle} with the given {@code commandOutputNode}.
     */
    public CommandOutputHandle(TextFlow commandOutputNode) {
        super(commandOutputNode);
    }

    /**
     * Returns the text in the display box.
     */
    public List<Node> getText() {
        return getRootNode().getChildren();
    }
}
