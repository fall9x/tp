package chopchop.ui;

import static chopchop.ui.testutil.GuiTestAssert.assertCardDisplaysIngredient;
import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.APRICOT_WITHOUT_TAGS;

import chopchop.model.ingredient.Ingredient;
import chopchop.testutil.IngredientBuilder;
import guitests.guihandles.IngredientCardHandle;
import org.junit.jupiter.api.Test;

public class IngredientCardTest extends GuiUnitTest {

    @Test
    public void display() {
        Ingredient ingredientWithNoTags = new IngredientBuilder(APRICOT_WITHOUT_TAGS).build();
        // id is set to zero as it is taken to be the only ingredient.
        IngredientCard ingredientCard = new IngredientCard(ingredientWithNoTags, 0);
        uiPartExtension.setUiPart(ingredientCard);
        assertCardDisplay(ingredientCard, ingredientWithNoTags, 0);

        Ingredient ingredientWithTags = new IngredientBuilder(APRICOT).build();
        ingredientCard = new IngredientCard(ingredientWithTags, 1);
        uiPartExtension.setUiPart(ingredientCard);
        assertCardDisplay(ingredientCard, ingredientWithTags, 1);
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(IngredientCard ingredientCard, Ingredient expectedIngredient, int expectedId) {
        guiRobot.pauseForHuman();

        IngredientCardHandle ingredientCardHandle = new IngredientCardHandle(ingredientCard.getRoot(), expectedId);

        // verify person details are displayed correctly
        assertCardDisplaysIngredient(expectedIngredient, ingredientCardHandle);
    }
}
