package chopchop.ui;

import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.ui.testutil.GuiTestUtil.parseIngredientsToString;
import static chopchop.ui.testutil.GuiTestUtil.parseStepList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import chopchop.model.recipe.Recipe;
import guitests.guihandles.RecipeDisplayHandle;
import org.junit.jupiter.api.Test;

public class RecipeDisplayTest extends GuiUnitTest {


    @Test
    public void displayRecipe() {
        RecipeDisplay recipeDisplay = new RecipeDisplay(APRICOT_SALAD);
        uiPartExtension.setUiPart(recipeDisplay);
        assertDisplay(recipeDisplay, APRICOT_SALAD);
    }


    private void assertDisplay(RecipeDisplay recipeDisplay, Recipe expectedRecipe) {
        guiRobot.pauseForHuman();
        RecipeDisplayHandle recipeDisplayHandle = new RecipeDisplayHandle(recipeDisplay.getRoot());

        assertEquals(expectedRecipe.getName(), recipeDisplayHandle.getName());
        assertEquals(parseIngredientsToString(expectedRecipe.getIngredients()), recipeDisplayHandle.getIngredients());
        assertEquals(parseStepList(expectedRecipe.getSteps()), recipeDisplayHandle.getSteps());
    }
}
