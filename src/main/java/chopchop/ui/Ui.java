package chopchop.ui;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * API of UI component
 */
public interface Ui {

    /**
     * Starts the UI (and the App).
     */
    void start(Stage primaryStage);

    /**
     * Displays a modal dialog box
     */
    void displayModalDialog(AlertType alertType, String title, String header, String body);

    /**
     * Displays text in the command output box.
     */
    void showCommandOutput(String text, boolean isError);
}
