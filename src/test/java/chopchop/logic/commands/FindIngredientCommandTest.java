package chopchop.logic.commands;

import static chopchop.commons.core.Messages.MESSAGE_INGREDIENTS_LISTED_OVERVIEW;
import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;
import java.util.Arrays;
import java.util.Collections;

import chopchop.model.EntryBook;
import org.junit.jupiter.api.Test;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UserPrefs;
import chopchop.model.attributes.NameContainsKeywordsPredicate;

public class FindIngredientCommandTest {
    private Model model = new ModelManager(new EntryBook<>(), getTypicalIngredientBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(new EntryBook<>(), getTypicalIngredientBook(), new UserPrefs());

    @Test
    public void equals() {
        var firstPredicate = new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        var secondPredicate = new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        var findFirstCommand = new FindIngredientCommand(firstPredicate);
        var findSecondCommand = new FindIngredientCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        var findFirstCommandCopy = new FindIngredientCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        var expectedMessage = String.format(MESSAGE_INGREDIENTS_LISTED_OVERVIEW, 0);
        var predicate = preparePredicate(" ");
        var command = new FindIngredientCommand(predicate);
        expectedModel.updateFilteredIngredientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredIngredientList());
    }

    @Test
    public void execute_multipleKeywords_multipleIngredientsFound() {
        var expectedMessage = String.format(MESSAGE_INGREDIENTS_LISTED_OVERVIEW, 2);
        var predicate = preparePredicate("apricot banana");
        var command = new FindIngredientCommand(predicate);
        expectedModel.updateFilteredIngredientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(APRICOT, BANANA), model.getFilteredIngredientList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
