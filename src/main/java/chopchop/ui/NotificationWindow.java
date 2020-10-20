package chopchop.ui;

import java.util.logging.Logger;

import chopchop.commons.core.LogsCenter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificationWindow extends UiPart<Stage> {
    public static final String NOTIFICATION_MESSAGE = "Feature will be coming soon!!";

    private static final Logger logger = LogsCenter.getLogger(NotificationWindow.class);
    private static final String FXML = "NotificationWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label notificationMessage;

    /**
     * Creates a new NotificationWindow.
     *
     * @param root Stage to use as the root of the NotificationWindow.
     */
    public NotificationWindow(Stage root) {
        super(FXML, root);
        notificationMessage.setText(NOTIFICATION_MESSAGE);
    }

    /**
     * Creates a new NotificationWindow.
     */
    public NotificationWindow() {
        this(new Stage());
    }

    /**
     * Shows the notification window.
     * @throws IllegalStateException
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show() {
        logger.fine("Showing notification page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the notification window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the notification window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the notification window.
     */
    public void focus() {
        getRoot().requestFocus();
    }
}
