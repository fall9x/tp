package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;

/**
 * A handle to the {@code NotificationWindow} of the application.
 */
public class NotificationWindowHandle extends StageHandle {

    public static final String NOTIFICATION_WINDOW_TITLE = "Notification";

    public NotificationWindowHandle(Stage notificationWindowStage) {
        super(notificationWindowStage);
    }

    /**
     * Returns true if a notification window is currently present in the application.
     */
    public static boolean isWindowPresent() {
        return new GuiRobot().isWindowShown(NOTIFICATION_WINDOW_TITLE);
    }
}
