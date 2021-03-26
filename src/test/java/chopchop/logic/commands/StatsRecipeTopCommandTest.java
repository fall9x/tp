package chopchop.logic.commands;

import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chopchop.commons.util.Pair;
import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.UsageList;
import chopchop.testutil.StubbedUsageModel;
import chopchop.testutil.TypicalUsages;

class StatsRecipeTopCommandTest {
    private Model model;
    private Model emptyModel;


    @BeforeEach
    public void setUp() {
        model = StubbedUsageModel.filled();
        emptyModel = StubbedUsageModel.empty();
    }

    @Test
    public void execute_emptyModeL_noRecipesMade() {
        var cmd = new StatsRecipeTopCommand();
        var res = cmd.execute(emptyModel, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<>(), "No recipes were made recently");
        assertEquals(expectedRes, res);
    }

    @Test
    public void execute_filledModel_sameCounts() {
        var cmd = new StatsRecipeTopCommand();
        var res = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<>(
            Arrays.asList(new Pair<>("A", "No. of times made: 5"),
                new Pair<>("B", "No. of times made: 5"))
        ), "Here are your top recipes");
        assertEquals(expectedRes, res);
    }

    @Test
    public void execute_duplicateUsageModel_success() {
        var cmd = new StatsRecipeTopCommand();
        this.model.setRecipeUsageList(new UsageList<>(TypicalUsages.getDuplicateRecipeList()));
        var res = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<>(
            Arrays.asList(new Pair<>("A", "No. of times made: 3"),
                new Pair<>("B", "No. of times made: 3"))
        ), "Here are your top recipes");
        assertEquals(expectedRes, res);
    }

    @Test
    public void execute_nullModel_nullPointerException() {
        var cmd = new StatsRecipeTopCommand();
        assertThrows(AssertionError.class, () -> cmd.execute(null, new HistoryManager()));
    }
}
