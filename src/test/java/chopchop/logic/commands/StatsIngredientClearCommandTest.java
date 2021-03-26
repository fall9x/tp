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

class StatsIngredientClearCommandTest {
    private Model model;
    private Model emptyModel;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = StubbedUsageModel.filled();
        emptyModel = StubbedUsageModel.empty();
        expectedModel = StubbedUsageModel.filled();
        expectedModel.setIngredientUsageList(new UsageList<>());
    }

    @Test
    public void execute_filledModel_success() {
        var cmd = new StatsIngredientClearCommand();
        assertCommandSuccess(cmd, model, expectedModel);
    }

    @Test
    public void execute_emptyModel_success() {
        var cmd = new StatsIngredientClearCommand();
        assertCommandSuccess(cmd, emptyModel, emptyModel);
    }

    @Test
    public void undo_success() {
        var cmd = new StatsIngredientClearCommand();
        cmd.execute(model, new HistoryManager());
        assertEquals(model, expectedModel);
        cmd.undo(model);
        assertEquals(model, model);
    }

    @Test
    public void execute_nullModel_nullPointerException() {
        var cmd = new StatsIngredientClearCommand();
        assertThrows(AssertionError.class, () -> cmd.execute(null, new HistoryManager()));
    }

}
