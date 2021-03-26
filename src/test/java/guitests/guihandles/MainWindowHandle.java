package guitests.guihandles;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final CommandBoxHandle commandBox;
    private final CommandOutputHandle commandOutput;
    private final StatsBoxHandle statsBox;
    private final MainMenuHandle mainMenu;
    private Stage stage;

    /**
     * Constructs a {@code MainWindowHandle} from the given {@code Stage}.
     */
    public MainWindowHandle(Stage stage) {
        super(stage);
        this.stage = stage;
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        commandOutput = new CommandOutputHandle(getChildNode(CommandOutputHandle.RESULT_DISPLAY_ID));
        statsBox = new StatsBoxHandle(getChildNode(StatsBoxHandle.STATS_FIELD_ID));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_FIELD_ID));
    }

    /**
     * Retrieves the command box.
     */
    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    /**
     * Retrieves the command output box.
     */
    public CommandOutputHandle getCommandOutput() {
        return commandOutput;
    }

    /**
     * Retrieves the stats box.
     */
    public StatsBoxHandle getStatsBox() {
        return statsBox;
    }

    /**
     * Retrieves the main menu.
     */
    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }


    /**
     * Closes the {@code MainWindow} by clicking on the menu bar's exit button.
     */
    public void clickOnMenuExitButton() {
        guiRobot.clickOn("File");
        guiRobot.clickOn("F4");
    }

    /**
     * Closes the {@code MainWindow} through an external request {@code MainWindow} (e.g pressing the 'X' button on
     * the {@code MainWindow} or closing the app through the taskbar).
     */
    public void closeMainWindowExternally() {
        guiRobot.interact(() -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
    }


    /**
     * Opens the {@code HelpWindow} by clicking on the menu bar's help button.
     */
    public void clickOnMenuHelpButton() {
        guiRobot.clickOn("Help");
        guiRobot.clickOn("F1");
    }


}
