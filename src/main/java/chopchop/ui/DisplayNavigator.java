package chopchop.ui;

import chopchop.model.recipe.Recipe;

/**
 * Utility class for controlling navigation between displays.
 *
 * All methods on the navigator are static to facilitate
 * simple access from the different displays.
 */
public class DisplayNavigator {

    private static DisplayController displayController;

    /**
     * Sets the displayController for the navigator.
     */
    protected static void setDisplayController(DisplayController displayController) {
        DisplayNavigator.displayController = displayController;
    }

    /**
     * Loads the RecipeViewPanel onto the swappable display region.
     */
    protected static void loadRecipePanel() {
        displayController.displayRecipeList();
    }

    /**
     * Loads the RecipeDisplay onto the swappable display region.
     * @param recipe
     */
    protected static void loadRecipeDisplay(Recipe recipe) {
        displayController.displayRecipe(recipe);
    }
    /**
     * Loads the IngredientViewPanel into the swappable display region.
     */
    protected static void loadIngredientPanel() {
        displayController.displayIngredientList();
    }
}
