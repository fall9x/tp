package chopchop.logic.commands;

import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.UsageList;
import chopchop.testutil.StubbedUsageModel;

class StatsRecipeClearCommandTest {
    private Model model;
    private Model emptyModel;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = StubbedUsageModel.filled();
        emptyModel = StubbedUsageModel.empty();
        expectedModel = StubbedUsageModel.filled();
        expectedModel.setRecipeUsageList(new UsageList<>());
    }

    @Test
    void execute_filledModel_success() {
        var cmd = new StatsRecipeClearCommand();
        assertCommandSuccess(cmd, model, expectedModel);
    }

    @Test
    void execute_emptyModel_success() {
        var cmd = new StatsRecipeClearCommand();
        assertCommandSuccess(cmd, emptyModel, emptyModel);
    }

    @Test
    void undo() {
        var cmd = new StatsRecipeClearCommand();
        cmd.execute(model, new HistoryManager());
        assertEquals(model, expectedModel);
        cmd.undo(model);
        assertEquals(model, model);
    }

    @Test
    public void execute_nullModel_nullPointerException() {
        var cmd = new StatsRecipeClearCommand();
        assertThrows(AssertionError.class, () -> cmd.execute(null, new HistoryManager()));
    }
}
