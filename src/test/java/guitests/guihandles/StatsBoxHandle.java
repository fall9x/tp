package guitests.guihandles;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A handle to the {@code StatsBox} in the GUI.
 */
public class StatsBoxHandle extends NodeHandle<Region> {

    public static final String STATS_FIELD_ID = "statsBox";

    private static final String PINS_FIELD_ID = "#pins";
    private static final String HEADER_FIELD_ID = "#header1";
    private static final String BODY_FIELD_ID = "#body1";

    private final TextArea pins;
    private final TextArea header1;
    private final TextArea body1;

    /**
     * Constructs a {@code StatsBox} with the given {@code commandBoxNode}.
     */
    public StatsBoxHandle(Region statsBoxNode) {
        super(statsBoxNode);
        pins = getChildNode(PINS_FIELD_ID);
        header1 = getChildNode(HEADER_FIELD_ID);
        body1 = getChildNode(BODY_FIELD_ID);
    }

    /**
     * Retrieves the content of the header.
     */
    public String getHeader() {
        return header1.getText();
    }

    /**
     * Retrieves the content of the body.
     */
    public String getBody() {
        return body1.getText();
    }
}
