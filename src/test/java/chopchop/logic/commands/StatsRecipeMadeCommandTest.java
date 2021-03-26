package chopchop.logic.commands;


import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalUsages.Date.FORMATTER;
import static chopchop.testutil.TypicalUsages.Date.ON_FORMATTER;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_A;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_A0;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_C;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_D;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_E0;
import static chopchop.testutil.TypicalUsages.RECIPE_A_A;
import static chopchop.testutil.TypicalUsages.RECIPE_A_E;
import static chopchop.testutil.TypicalUsages.RECIPE_B_A;
import static chopchop.testutil.TypicalUsages.RECIPE_B_E;
import static chopchop.testutil.TypicalUsages.getUnsortedRecipeList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chopchop.commons.util.Pair;
import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.UsageList;
import chopchop.testutil.StubbedUsageModel;
import chopchop.testutil.TypicalUsages;

class StatsRecipeMadeCommandTest {
    private Model model;
    private Model emptyModel;


    @BeforeEach
    public void setUp() {
        model = StubbedUsageModel.filled();
        emptyModel = StubbedUsageModel.empty();
    }

    @Test
    public void execute_expectedBeforeAfterEmptyModel_noRecipesFound() {
        LocalDateTime before = USAGE_DATE_E0;
        LocalDateTime after = USAGE_DATE_A0;
        var cmd = new StatsRecipeMadeCommand(after, before);
        var cmdRes = cmd.execute(emptyModel, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<Pair<String, String>>(),
            String.format("No recipes were made between %s and %s", after.format(FORMATTER), before.format(FORMATTER)));
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_beforeAndAfterNull_recipeJustAddedInFound() {
        var now = LocalDateTime.now();
        LocalDateTime nowAfter = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        LocalDateTime nowBefore = nowAfter.plusDays(1);

        var cmd = new StatsRecipeMadeCommand(null, null);
        var cmdRes = cmd.execute(model, new HistoryManager());
        var expectedRes = new StatsRecipeMadeCommand(nowAfter, nowBefore).execute(model, new HistoryManager());
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_beforeEqualToAfterPlusOneDay_showRecipesMadeOn() {
        LocalDateTime after = USAGE_DATE_A0;
        LocalDateTime before = after.plusDays(1);
        var cmd = new StatsRecipeMadeCommand(after, before);
        var cmdRes = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<Pair<String, String>>(
            Arrays.asList(new Pair<>(RECIPE_A_A.getName(), RECIPE_A_A.getPrintableDate()),
                new Pair<>(RECIPE_B_A.getName(), RECIPE_B_A.getPrintableDate()))),
            String.format("Showing recipes made between %s and %s", after.format(FORMATTER), before.format(FORMATTER)));
        assertEquals(cmdRes, expectedRes);

        LocalDateTime afterOn = LocalDateTime.of(0, 1, 1, 0, 0);
        LocalDateTime beforeOn = LocalDateTime.of(0, 1, 2, 0, 0);
        cmd = new StatsRecipeMadeCommand(afterOn, beforeOn);
        cmdRes = cmd.execute(model, new HistoryManager());
        expectedRes = CommandResult.statsMessage(new ArrayList<Pair<String, String>>(),
            String.format("No recipes were made on %s", afterOn.format(ON_FORMATTER)));
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_beforeIsNull_recipesAfterFound() {
        LocalDateTime after = USAGE_DATE_A0;
        var cmd = new StatsRecipeMadeCommand(after, null);
        var cmdRes = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<Pair<String, String>>(
                TypicalUsages.getListViewRecipeList().stream()
                    .map(x -> new Pair<>(x.getName(), x.getPrintableDate()))
                    .collect(Collectors.toList())
            ),
            String.format("Showing recipes made after %s", after.format(FORMATTER)));
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_afterIsNull_recipesBeforeFound() {
        LocalDateTime before = USAGE_DATE_E0;
        var cmd = new StatsRecipeMadeCommand(null, before);
        var cmdRes = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<Pair<String, String>>(
                TypicalUsages.getListViewRecipeList().stream()
                    .map(x -> new Pair<>(x.getName(), x.getPrintableDate()))
                    .collect(Collectors.toList())
            ),
            String.format("Showing recipes made before %s", before.format(FORMATTER)));
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_afterMoreThanOrEqualBefore_noRecipesFound() {
        LocalDateTime before = USAGE_DATE_A;
        LocalDateTime after = USAGE_DATE_C;
        var cmd = new StatsRecipeMadeCommand(after, before);
        var cmdRes = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.error("'after' date cannot be later than 'before' date");
        assertEquals(cmdRes, expectedRes);
        cmd = new StatsRecipeMadeCommand(after, after);
        cmdRes = cmd.execute(model, new HistoryManager());
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_expectedBeforeAndAfter_recipesFound() {
        LocalDateTime before = USAGE_DATE_E0;
        LocalDateTime after = USAGE_DATE_D;
        var cmd = new StatsRecipeMadeCommand(after, before);
        var cmdRes = cmd.execute(model, new HistoryManager());
        var expectedRes = CommandResult.statsMessage(new ArrayList<Pair<String, String>>(
                Arrays.asList(new Pair<>(RECIPE_A_E.getName(), RECIPE_A_E.getPrintableDate()),
                    new Pair<>(RECIPE_B_E.getName(), RECIPE_B_E.getPrintableDate()))
            ),
            String.format("Showing recipes made between %s and %s", after.format(FORMATTER), before.format(FORMATTER)));
        assertEquals(cmdRes, expectedRes);
        this.model.setRecipeUsageList(new UsageList<>(getUnsortedRecipeList()));
        cmdRes = cmd.execute(model, new HistoryManager());
        assertEquals(cmdRes, expectedRes);
    }

    @Test
    public void execute_nullModel_nullPointerException() {
        var cmd = new StatsRecipeMadeCommand(null, null);
        assertThrows(AssertionError.class, () -> cmd.execute(null, new HistoryManager()));
    }
}
