package chopchop.ui;

import static chopchop.commons.util.Strings.COMMAND_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;

import chopchop.logic.LogicManager;
import chopchop.logic.commands.CommandResult;
import chopchop.model.ModelManager;
import chopchop.storage.JsonIngredientBookStorage;
import chopchop.storage.JsonRecipeBookStorage;
import chopchop.storage.JsonUserPrefsStorage;
import chopchop.storage.JsonIngredientUsageStorage;
import chopchop.storage.JsonRecipeUsageStorage;
import chopchop.storage.StorageManager;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guitests.guihandles.CommandBoxHandle;
import org.junit.jupiter.api.io.TempDir;

public class CommandBoxTest extends GuiUnitTest {

    private static final String COMMAND_THAT_SUCCEEDS = COMMAND_LIST + " recipe";
    private static final String COMMAND_THAT_FAILS = "test";

    @TempDir
    public Path testFolder;

    private ArrayList<String> defaultStyleOfCommandBox;
    private ArrayList<String> errorStyleOfCommandBox;
    private final ArrayList<String> history = new ArrayList<>();

    private CommandBoxHandle commandBoxHandle;
    private StorageManager storageManager;
    private ModelManager modelManager;
    private LogicManager logic;


    @BeforeEach
    public void setUp() {
        JsonIngredientBookStorage ingredientBookStorage = new JsonIngredientBookStorage(getTempFilePath("ab"));
        JsonRecipeBookStorage recipeBookStorage = new JsonRecipeBookStorage(getTempFilePath("abc"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonRecipeUsageStorage recipeUsageStorage = new JsonRecipeUsageStorage(getTempFilePath("cc"));
        JsonIngredientUsageStorage ingredientUsageStorage = new JsonIngredientUsageStorage(getTempFilePath("dd"));
        storageManager = new StorageManager(recipeBookStorage, ingredientBookStorage, recipeUsageStorage,
                ingredientUsageStorage, userPrefsStorage);

        modelManager = new ModelManager();

        logic = new LogicManager(modelManager, storageManager);

        CommandBox commandBox = new CommandBox(commandText -> {
            history.add(commandText);
            logic.execute(commandText);
            if (commandText.equals(COMMAND_THAT_SUCCEEDS)) {
                return CommandResult.message("Command successful");
            } else {
                return CommandResult.error("Command failed");
            }
        }, logic);
        commandBoxHandle = new CommandBoxHandle(getChildNode(commandBox.getRoot(),
                CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        uiPartExtension.setUiPart(commandBox);

        defaultStyleOfCommandBox = new ArrayList<>(commandBoxHandle.getStyleClass());

        errorStyleOfCommandBox = new ArrayList<>(defaultStyleOfCommandBox);
    }

    @Test
    public void commandBox_startingWithSuccessfulCommand() {
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
    }

    @Test
    public void commandBox_startingWithFailedCommand() {
        assertBehaviorForFailedCommand();
        assertBehaviorForSuccessfulCommand();

        // verify that style is changed correctly even after multiple consecutive failed commands
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
        assertBehaviorForFailedCommand();
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    /**
     * Runs a command that fails, then verifies that <br>
     *      - the text is cleared <br>
     *      - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForFailedCommand() {
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertEquals("", commandBoxHandle.getInput());
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    @Test
    public void handleKeyPress_startingWithUp() {
        // empty history
        assertInputHistory(KeyCode.UP, "");
        assertInputHistory(KeyCode.DOWN, "");

        // one command
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, "");

        // two commands (latest command is failure)
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);

        // insert command in the middle of retrieving previous commands
        guiRobot.push(KeyCode.UP);
        String thirdCommand = "list recipe";
        commandBoxHandle.run(thirdCommand);
        assertInputHistory(KeyCode.UP, thirdCommand);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.DOWN, thirdCommand);
        assertInputHistory(KeyCode.DOWN, "");
    }

    @Test
    public void handleKeyPress_startingWithDown() {
        // empty history
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, "");

        // one command
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);

        // two commands
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);

        // insert command in the middle of retrieving previous commands
        guiRobot.push(KeyCode.UP);
        String thirdCommand = "list";
        commandBoxHandle.run(thirdCommand);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, thirdCommand);
    }

    @Test
    public void handleKeyPress_tabAutocompleteTargetWithTarget() {

        commandBoxHandle.run("");
        // Autocomplete command
        executeKeyCodes(KeyCode.L);
        assertInputHistory(KeyCode.TAB, "list ");

        // Autocomplete command target
        executeKeyCodes(KeyCode.R);
        assertInputHistory(KeyCode.TAB, COMMAND_THAT_SUCCEEDS + " ");
    }

    @Test
    public void handleKeyPress_tabAutocompleteForUserInputs() {
        // one arg starting with char
        String firstCommand = "add recipe ";
        String firstArg = "duck soup";
        String secondCommand = "view recipe ";
        commandBoxHandle.run(firstCommand + firstArg);
        executeKeyCodes(KeyCode.V, KeyCode.TAB, KeyCode.R, KeyCode.TAB, KeyCode.D);
        assertInputHistory(KeyCode.TAB, secondCommand + firstArg + " ");

        // two args in history with same prefix get second
        String secondArg = "duck pudding";
        commandBoxHandle.run(firstCommand + secondArg);
        executeKeyCodes(KeyCode.V, KeyCode.TAB, KeyCode.R, KeyCode.TAB, KeyCode.D, KeyCode.TAB);
        assertInputHistory(KeyCode.TAB, secondCommand + secondArg + " ");

        // two args in history with same prefix get first
        commandBoxHandle.clearInput();
        executeKeyCodes(KeyCode.V, KeyCode.TAB, KeyCode.R, KeyCode.TAB, KeyCode.D, KeyCode.TAB, KeyCode.TAB);
        assertInputHistory(KeyCode.TAB, secondCommand + firstArg + " ");
    }



    /**
     * Runs a command that succeeds, then verifies that <br>
     *      - the text is cleared <br>
     *      - the command box's style is the same as {@code defaultStyleOfCommandBox}.
     */
    private void assertBehaviorForSuccessfulCommand() {
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertEquals("", commandBoxHandle.getInput());
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    private void executeKeyCodes(KeyCode... keyCodes) {
        for (KeyCode keyCode : keyCodes) {
            guiRobot.push(keyCode);
        }
    }

    /**
     * Pushes {@code keycode} and checks that the input in the {@code commandBox} equals to {@code expectedCommand}.
     */
    private void assertInputHistory(KeyCode keycode, String expectedCommand) {
        guiRobot.push(keycode);
        assertEquals(expectedCommand, commandBoxHandle.getInput());
    }
}
