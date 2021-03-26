//@@author fall9x

package chopchop.ui;

import java.util.ArrayList;
import java.util.List;

import chopchop.MainApp;
import chopchop.commons.core.Log;
import chopchop.commons.util.StringUtil;
import chopchop.logic.Logic;
import chopchop.logic.commands.CommandResult;
import chopchop.model.Model;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The manager of the UI component.
 */
public class UiManager implements Ui {

    private static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";
    private static final String APPLICATION_ICON = "/images/chopchop.png";

    private static final Log logger = new Log(UiManager.class);
    private static final List<DummyAlert> startupAlerts = new ArrayList<>();


    private Logic logic;
    private Model model;
    private MainWindow mainWindow;

    /**
     * Creates a {@code UiManager} with the given {@code Logic}.
     */
    public UiManager(Logic logic, Model model) {
        super();
        this.logic = logic;
        this.model = model;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.log("Starting UI...");

        //Set the application icon.
        primaryStage.getIcons().add(this.getImage(APPLICATION_ICON));

        try {
            mainWindow = new MainWindow(primaryStage, logic, model);
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

            for (var alert : startupAlerts) {
                this.displayModalDialog(alert.type, alert.title, alert.header, alert.content);
            }

        } catch (Throwable e) {
            logger.error("Initialisation error: %s", StringUtil.getDetails(e));
            this.showFatalErrorDialogAndShutdown("Fatal error during initialisation", e);
        }
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    @Override
    public void showCommandOutput(String text, boolean isError) {
        this.mainWindow.showCommandOutput(isError
            ? CommandResult.error(text)
            : CommandResult.message(text)
        );
    }

    @Override
    public void displayModalDialog(AlertType type, String title, String header, String content) {

        var alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("stylesheets/Style.css");
        alert.initOwner(this.mainWindow.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    /**
     * Queues a startup alert to show up, without actually displaying it. When the UiManager starts
     * (the {@code start()} method), it will display all alerts in order before continuing.
     */
    public static void enqueueStartupAlert(AlertType type, String title, String header, String content) {
        var alert = new DummyAlert(type, title, header, content);
        UiManager.startupAlerts.add(alert);
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.error("Fatal error: %s, %s, %s", title, e.getMessage(), StringUtil.getDetails(e));

        this.displayModalDialog(AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }


    private static class DummyAlert {
        final AlertType type;
        final String title;
        final String header;
        final String content;

        DummyAlert(AlertType type, String title, String header, String content) {
            this.type = type;
            this.title = title;
            this.header = header;
            this.content = content;
        }
    }
}
