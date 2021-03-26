package chopchop.ui;

import static chopchop.ui.testutil.GuiTestUtil.parseTextToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import chopchop.logic.commands.CommandResult;
import guitests.guihandles.CommandOutputHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CommandOutputTest extends GuiUnitTest {

    private CommandOutput commandOutput;
    private CommandOutputHandle commandOutputHandle;

    @BeforeEach
    public void setup() {
        commandOutput = new CommandOutput();
        uiPartExtension.setUiPart(commandOutput);

        commandOutputHandle = new CommandOutputHandle(getChildNode(commandOutput.getRoot(),
                CommandOutputHandle.RESULT_DISPLAY_ID));
    }

    @Test
    public void displayValidMessage() {
        guiRobot.pauseForHuman();
        assertEquals("", parseTextToString(commandOutputHandle.getText()));

        // Command output received
        String testFeedback = "test feedback to user";
        guiRobot.interact(() -> commandOutput.setFeedbackToUser(CommandResult.message(testFeedback)));
        guiRobot.pauseForHuman();
        assertEquals(testFeedback, parseTextToString(commandOutputHandle.getText()));

        // Clear message
        guiRobot.interact(() -> commandOutput.clear());
        assertEquals("", parseTextToString(commandOutputHandle.getText()));
    }

    @Test
    public void displayErrorMessage() {
        String errorWrapper = "Error: ";
        String testError = "test error to user.";
        guiRobot.interact(() -> commandOutput.setFeedbackToUser(CommandResult.error(testError)));
        guiRobot.pauseForHuman();
        assertEquals(errorWrapper + testError, parseTextToString(commandOutputHandle.getText()));
    }
}
