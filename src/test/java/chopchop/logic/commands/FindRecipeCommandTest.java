package chopchop.logic.commands;

import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;
import java.util.Arrays;
import java.util.Collections;

import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UserPrefs;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import org.junit.jupiter.api.Test;

public class FindRecipeCommandTest {
    private Model model = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(), new UserPrefs());

    @Test
    public void equals() {
        var firstPredicate = new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        var secondPredicate = new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        var findFirstCommand = new FindRecipeCommand(firstPredicate);
        var findSecondCommand = new FindRecipeCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        var findFirstCommandCopy = new FindRecipeCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        //different recipe -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

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
