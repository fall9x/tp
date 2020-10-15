package chopchop.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.testutil.Assert.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import chopchop.logic.parser.ItemReference;
import chopchop.logic.commands.exceptions.CommandException;
import chopchop.model.Model;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientBook;

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
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        IngredientBook expectedIndBook = new IngredientBook(actualModel.getIngredientBook());
        List<Ingredient> expectedFilteredList = new ArrayList<>(actualModel.getFilteredIngredientList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedIndBook, actualModel.getIngredientBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredIngredientList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, ItemReference targetIndex) {
        assertTrue(targetIndex.getZeroIndex() < model.getFilteredIngredientList().size());

        Ingredient person = model.getFilteredIngredientList().get(targetIndex.getZeroIndex());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredIngredientList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredIngredientList().size());
    }
}
