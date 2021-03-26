package chopchop.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.ItemReference;
import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.recipe.Recipe;

public class CommandTestUtil {
    public static final String VALID_INGREDIENT_NAME_APRICOT = "Apricot";
    public static final String VALID_INGREDIENT_NAME_BANANA = "Banana";
    public static final String VALID_INGREDIENT_NAME_CUSTARD = "Custard";
    public static final double VALID_INGREDIENT_QTY_APRICOT = 1000000;
    public static final double VALID_INGREDIENT_QTY_BANANA = 17;
    public static final double VALID_INGREDIENT_QTY_CUSTARD = 18;
    public static final String VALID_INGREDIENT_EXPIRY_APRICOT = "2020-12-01";
    public static final String VALID_INGREDIENT_EXPIRY_BANANA = "2021-10-13";
    public static final String VALID_INGREDIENT_EXPIRY_CUSTARD = "2020-04-04";
    public static final String VALID_RECIPE_NAME_APRICOT_SALAD = "Apricot Salad";
    public static final String VALID_RECIPE_NAME_BANANA_SALAD = "Banana Salad";
    public static final String VALID_RECIPE_NAME_CUSTARD_SALAD = "Custard Salad";


    /**
     * Executes the given command and asserts that it succeeds.
     */
    public static void assertCommandSuccess(Command command, Model model, Model expectedModel) {
        var result = command.execute(model, new HistoryManagerStub());
        assertTrue(result.didSucceed());
        assertEquals(expectedModel, model);

        if (command instanceof Undoable) {
            var r = ((Undoable) command).undo(model);
            assertTrue(r.didSucceed());
        }
    }

    /**
     * Executes the given command and asserts that it fails.
     */
    public static void assertCommandFailure(Command command, Model model) {

        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        EntryBook<Ingredient> expectedIndBook = new EntryBook<>(model.getIngredientBook());
        List<Ingredient> expectedFilteredList = new ArrayList<>(model.getFilteredIngredientList());

        var result = command.execute(model, new HistoryManagerStub());
        assertTrue(result.isError());

        assertEquals(expectedIndBook, model.getIngredientBook());
        assertEquals(expectedFilteredList, model.getFilteredIngredientList());
    }


    /**
     * Updates {@code model}'s filtered ingredient list to show only the ingredient at the given {@code targetIndex}
     * in the {@code model}'s ingredient book.
     */
    public static void showIngredientAtIndex(Model model, ItemReference targetIndex) {
        assertTrue(targetIndex.getZeroIndex() < model.getFilteredIngredientList().size());

        Ingredient ind = model.getFilteredIngredientList().get(targetIndex.getZeroIndex());
        final String[] splitName = ind.getName().split("\\s+");
        model.updateFilteredIngredientList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredIngredientList().size());
    }

    /**
     * Updates {@code model}'s filtered recipe list to show only the recipe at the given {@code targetIndex} in the
     * {@code model}'s recipe book.
     * @param model
     * @param targetIndex
     */
    public static void showRecipeAtIndex(Model model, ItemReference targetIndex) {
        assertTrue(targetIndex.getZeroIndex() < model.getFilteredRecipeList().size());

        Recipe rec = model.getFilteredRecipeList().get(targetIndex.getZeroIndex());
        final String[] splitName = rec.getName().split("\\s+");
        model.updateFilteredRecipeList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredRecipeList().size());
    }

    /**
     * A default history stub that have all of the methods failing.
     */
    public static class HistoryManagerStub extends HistoryManager {
        public void add(Command command) {
            throw new AssertionError("This method should not be called.");
        }

        public CommandResult undo(Model model) {
            throw new AssertionError("This method should not be called.");
        }

        public CommandResult redo(Model model) {
            throw new AssertionError("This method should not be called.");
        }
    }
}
