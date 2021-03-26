package chopchop.logic.commands;

import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.logic.commands.CommandTestUtil.showIngredientAtIndex;
import static chopchop.testutil.TypicalReferences.INDEXED_FIRST;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;

public class ListIngredientCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new EntryBook<>(), getTypicalIngredientBook(), new UsageList<RecipeUsage>(),
            new UsageList<IngredientUsage>(), new UserPrefs());
        expectedModel = new ModelManager(new EntryBook<>(), model.getIngredientBook(), new UsageList<RecipeUsage>(),
            new UsageList<IngredientUsage>(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListIngredientCommand(), model, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {

        showIngredientAtIndex(model, INDEXED_FIRST);
        assertCommandSuccess(new ListIngredientCommand(), model, expectedModel);
    }
}
