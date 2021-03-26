package chopchop.logic.commands;

import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalUsages.RECIPE_A_A;
import static chopchop.testutil.TypicalUsages.RECIPE_B_A;
import static chopchop.testutil.TypicalUsages.getListViewRecipeList;
import static chopchop.testutil.TypicalUsages.getRecipeList;
import static chopchop.testutil.TypicalUsages.getRecipeUsageList;
import static chopchop.testutil.TypicalUsages.getUnsortedRecipeList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chopchop.commons.util.Pair;
import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.UsageList;
import chopchop.testutil.StubbedUsageModel;

class StatsRecipeRecentCommandTest {
    private Model model;
    private CommandResult recipeListRes = CommandResult.statsMessage(getListViewRecipeList().stream()
            .map(x -> new Pair<>(x.getName(), x.getPrintableDate())).collect(Collectors.toList()),
        "Here are your recently made recipes");

    @BeforeEach
    public void setUp() {
        this.model = StubbedUsageModel.filled();
    }

    @Test
    public void execute_modelUnchanged_success() {
        var cmd = new StatsRecipeRecentCommand();
        assertCommandSuccess(cmd, model, model);
    }

    // in the case where the user changed the order of usage
    @Test
    public void execute_modelUnsortedUsageListUnchanged_success() {
        var unsortedModel = StubbedUsageModel.filled();
        unsortedModel.setRecipeUsageList(new UsageList<>(getUnsortedRecipeList()));
        model.setRecipeUsageList(new UsageList<>(getUnsortedRecipeList()));
        var cmd = new StatsRecipeRecentCommand();
        assertCommandSuccess(cmd, unsortedModel, model);
    }

    @Test
    public void execute_noRecent() {
        var expectedRes = CommandResult.statsMessage(new ArrayList<>(),
            "No recipes were made recently");
        var cmd = new StatsRecipeRecentCommand();
        var cmdRes = cmd.execute(StubbedUsageModel.empty(), new HistoryManager());
        assertEquals(expectedRes, cmdRes);
    }

    @Test
    public void execute_oneRecent() {
        var expectedRes = CommandResult.statsMessage(new ArrayList<>(
                List.of(new Pair<>(RECIPE_A_A.getName(), RECIPE_A_A.getPrintableDate()))),
            "Here are your recently made recipes");
        model.setRecipeUsageList(new UsageList<>(List.of(RECIPE_A_A)));
        var cmd = new StatsRecipeRecentCommand();
        var cmdRes = cmd.execute(model, new HistoryManager());
        assertEquals(expectedRes, cmdRes);
    }

    @Test
    public void execute_sortedUsages() {
        model.setRecipeUsageList(getRecipeUsageList());
        var cmd = new StatsRecipeRecentCommand();
        var cmdRes = cmd.execute(model, new HistoryManager());
        assertEquals(recipeListRes, cmdRes);
    }

    @Test
    public void execute_unsortedUsages() {
        model.setRecipeUsageList(new UsageList<>(getUnsortedRecipeList()));
        var cmd = new StatsRecipeRecentCommand();
        var cmdRes = cmd.execute(model, new HistoryManager());
        assertEquals(recipeListRes, cmdRes);
    }

    //max of 10 items in the list
    @Test
    public void execute_elevenUsages_only10UsagesReturned() {
        var newList = getRecipeList();
        newList.add(RECIPE_B_A);
        model.setRecipeUsageList(new UsageList<>(newList));
        var cmd = new StatsRecipeRecentCommand();
        var cmdRes = cmd.execute(model, new HistoryManager());
        assertEquals(recipeListRes, cmdRes);
    }

    @Test
    public void execute_nullModel_nullPointerException() {
        var cmd = new StatsRecipeRecentCommand();
        assertThrows(AssertionError.class, () -> cmd.execute(null, new HistoryManager()));
    }
}
