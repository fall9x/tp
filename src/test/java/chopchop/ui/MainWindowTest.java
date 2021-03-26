package chopchop.ui;


import static chopchop.commons.util.Strings.COMMAND_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.Collections;

import chopchop.logic.LogicManager;
import chopchop.model.ModelManager;
import chopchop.storage.JsonIngredientBookStorage;
import chopchop.storage.JsonRecipeBookStorage;
import chopchop.storage.JsonUserPrefsStorage;
import chopchop.storage.JsonIngredientUsageStorage;
import chopchop.storage.JsonRecipeUsageStorage;
import chopchop.storage.StorageManager;
import guitests.guihandles.HelpWindowHandle;
import guitests.guihandles.StageHandle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxToolkit;


public class MainWindowTest extends GuiUnitTest {

    private static final String COMMAND_THAT_SUCCEEDS = COMMAND_LIST + " recipe";
    private static final String COMMAND_THAT_FAILS = "test";

    @TempDir
    public Path tempFolder;

    private MainWindow mainWindow;
    private EmptyMainWindowHandle mainWindowHandle;
    private Stage stage;

    private StorageManager storageManager;
    private ModelManager modelManager;
    private LogicManager logic;

    @BeforeEach
    private void setUp() throws Exception {
        JsonIngredientBookStorage ingredientBookStorage = new JsonIngredientBookStorage(getTempFilePath("ab"));
        JsonRecipeBookStorage recipeBookStorage = new JsonRecipeBookStorage(getTempFilePath("abc"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonRecipeUsageStorage recipeUsageStorage = new JsonRecipeUsageStorage(getTempFilePath("cc"));
        JsonIngredientUsageStorage ingredientUsageStorage = new JsonIngredientUsageStorage(getTempFilePath("dd"));
        storageManager = new StorageManager(recipeBookStorage, ingredientBookStorage, recipeUsageStorage,
                ingredientUsageStorage, userPrefsStorage);

        modelManager = new ModelManager();

        logic = new LogicManager(modelManager, storageManager);

        FxToolkit.setupStage(stage -> {
            this.stage = stage;
            mainWindow = new MainWindow(stage, logic, modelManager);
            mainWindow.fillInnerParts();
            mainWindowHandle = new EmptyMainWindowHandle(stage);
            mainWindowHandle.focus();
        });
        FxToolkit.showStage();
    }


    @Test
    public void close_menuBarExitButton_allWindowsClosed() {
        mainWindowHandle.clickOnMenuExitButton();
        // The application will exit when all windows are closed.
        assertEquals(Collections.emptyList(), guiRobot.listWindows());
    }

    @Test
    public void close_externalRequest_exitAppRequestEventPosted() {
        mainWindowHandle.clickOnMenuHelpButton();
        assertFalse(HelpWindowHandle.isWindowPresent());
        mainWindowHandle.closeMainWindowExternally();
        // The application will exit when all windows are closed.
        assertEquals(Collections.emptyList(), guiRobot.listWindows());
    }

    @Test
    public void stageCheck() {
        assertEquals(stage, mainWindow.getPrimaryStage());
    }

    private Path getTempFilePath(String fileName) {
        return tempFolder.resolve(fileName);
    }

    /**
     * A handle for an empty {@code MainWindow}. The components in {@code MainWindow} are not initialized.
     */
    private class EmptyMainWindowHandle extends StageHandle {

        private EmptyMainWindowHandle(Stage stage) {
            super(stage);
        }

        /**
         * Closes the {@code MainWindow} by clicking on the menu bar's exit button.
         */
        private void clickOnMenuExitButton() {
            guiRobot.clickOn("File");
            guiRobot.clickOn("Exit");
        }

        /**
         * Closes the {@code MainWindow} through an external request {@code MainWindow} (e.g pressing the 'X' button on
         * the {@code MainWindow} or closing the app through the taskbar).
         */
        private void closeMainWindowExternally() {
            guiRobot.interact(() -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
        }

        /**
         * Opens the {@code HelpWindow} by clicking on the menu bar's help button.
         */
        private void clickOnMenuHelpButton() {
            guiRobot.clickOn("Help");
            guiRobot.clickOn("F1");
        }
    }
}
