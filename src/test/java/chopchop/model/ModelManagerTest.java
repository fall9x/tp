package chopchop.model;

import static chopchop.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;

import java.util.Arrays;

import chopchop.model.ingredient.Ingredient;
import chopchop.model.recipe.Recipe;
import org.junit.jupiter.api.Test;
import chopchop.commons.core.GuiSettings;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.testutil.IngredientBookBuilder;
import chopchop.testutil.RecipeBookBuilder;


public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new EntryBook<>(), new EntryBook<>(modelManager.getIngredientBook()));
        assertEquals(new EntryBook<>(), new EntryBook<>(modelManager.getRecipeBook()));
    }

    @Test
    public void hasIngredient_nullIngredient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasIngredient(null));
    }

    @Test
    public void hasIngredient_indNotInIngredientBook_returnsFalse() {
        assertFalse(modelManager.hasIngredient(APRICOT));
    }

    @Test
    public void hasIngredient_ingredientInIngredientBook_returnsTrue() {
        modelManager.addIngredient(APRICOT);
        assertTrue(modelManager.hasIngredient(APRICOT));
    }

    @Test
    public void getFilteredPIngredientList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredIngredientList().remove(0));
    }

    @Test
    public void hasRecipe_nullRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasRecipe(null));
    }

    @Test
    public void hasRecipe_recipeNotInRecipeBook_returnsFalse() {
        assertFalse(modelManager.hasRecipe(APRICOT_SALAD));
    }

    @Test
    public void hasRecipe_recipeInRecipeBook_returnsTrue() {
        modelManager.addRecipe(APRICOT_SALAD);
        assertTrue(modelManager.hasRecipe(APRICOT_SALAD));
    }

    @Test
    public void getFilteredRecipeList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredRecipeList().remove(0));
    }

    @Test
    public void equals() {
        EntryBook<Ingredient> ingredientBook = new IngredientBookBuilder()
            .withIngredient(APRICOT).withIngredient(BANANA).build();
        EntryBook<Ingredient> differentIngredientBook = new EntryBook<>();

        EntryBook<Recipe> recipeBook = new RecipeBookBuilder()
            .withRecipe(APRICOT_SALAD).withRecipe(BANANA_SALAD).build();

        EntryBook<Recipe> differentRecipeBook = new EntryBook<>();

        UserPrefs userPrefs = new chopchop.model.UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(recipeBook, ingredientBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(recipeBook, ingredientBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different ingredientBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(
            recipeBook, differentIngredientBook, userPrefs)));

        // different recipeBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(
            differentRecipeBook, ingredientBook, userPrefs)));

        // different filteredIngredientList -> returns false
        final String[] indKeywords = APRICOT.getName().split("\\s+");
        modelManager.updateFilteredIngredientList(new NameContainsKeywordsPredicate(Arrays.asList(indKeywords)));
        assertFalse(modelManager.equals(new ModelManager(recipeBook, ingredientBook, userPrefs)));

        // different filteredRecipeList -> returns false
        final String[] recKeywords = APRICOT_SALAD.getName().split("\\s+");
        modelManager.updateFilteredRecipeList(new NameContainsKeywordsPredicate(Arrays.asList(recKeywords)));
        assertFalse(modelManager.equals(new ModelManager(recipeBook, ingredientBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredIngredientList(PREDICATE_SHOW_ALL_ENTRIES);
        modelManager.updateFilteredRecipeList(PREDICATE_SHOW_ALL_ENTRIES);

    }
}
