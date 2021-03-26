package chopchop.logic.commands;

import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;
import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;

public class FindRecipeCommandTest {
    private Model model = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(), new UsageList<RecipeUsage>(),
            new UsageList<IngredientUsage>(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(),
            new UsageList<RecipeUsage>(), new UsageList<IngredientUsage>(), new UserPrefs());

    @Test
    public void execute_zeroKeywords_noRecipeFound() {
        var predicate = preparePredicate(" ");
        var command = new FindRecipeCommand(predicate);
        expectedModel.updateFilteredRecipeList(predicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleKeyWords_mutipleRecipesFound() {
        var predicate = preparePredicate("apricot salad banana salad");
        var command = new FindRecipeCommand(predicate);
        expectedModel.updateFilteredRecipeList(predicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD, BANANA_SALAD), model.getFilteredRecipeList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
