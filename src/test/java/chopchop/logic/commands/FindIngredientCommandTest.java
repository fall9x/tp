package chopchop.logic.commands;

import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;

import java.util.Arrays;
import java.util.Collections;

import chopchop.model.EntryBook;
import org.junit.jupiter.api.Test;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;

public class FindIngredientCommandTest {
    private Model model = new ModelManager(new EntryBook<>(), getTypicalIngredientBook(), new UsageList<RecipeUsage>(),
        new UsageList<IngredientUsage>(), new UserPrefs());
    private Model expectedModel = new ModelManager(new EntryBook<>(), getTypicalIngredientBook(),
        new UsageList<RecipeUsage>(), new UsageList<IngredientUsage>(), new UserPrefs());

    @Test
    public void execute_zeroKeywords_noIngredientFound() {
        var predicate = preparePredicate(" ");
        var command = new FindIngredientCommand(predicate);
        expectedModel.updateFilteredIngredientList(predicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredIngredientList());
    }

    @Test
    public void execute_multipleKeywords_multipleIngredientsFound() {
        var predicate = preparePredicate("apricot banana");
        var command = new FindIngredientCommand(predicate);
        expectedModel.updateFilteredIngredientList(predicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT, BANANA), model.getFilteredIngredientList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
