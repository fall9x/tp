package chopchop.model;

import static chopchop.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_A;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_A0;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_C;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_E0;
import static chopchop.testutil.TypicalUsages.INGREDIENT_A_B;
import static chopchop.testutil.TypicalUsages.INGREDIENT_B_B;
import static chopchop.testutil.TypicalUsages.RECIPE_A_B;
import static chopchop.testutil.TypicalUsages.RECIPE_B_B;
import static chopchop.testutil.TypicalUsages.getListViewIngredientList;
import static chopchop.testutil.TypicalUsages.getListViewRecipeList;
import static chopchop.testutil.TypicalUsages.getRecipeUsageList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import chopchop.model.exceptions.EntryNotFoundException;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.recipe.Recipe;
import org.junit.jupiter.api.Test;
import chopchop.commons.core.GuiSettings;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;
import chopchop.model.usage.Usage;
import chopchop.testutil.IngredientBookBuilder;
import chopchop.testutil.RecipeBookBuilder;


public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    private EntryBook<Ingredient> ingredientBook = new IngredientBookBuilder()
        .withIngredient(APRICOT).withIngredient(BANANA).build();
    private EntryBook<Ingredient> differentIngredientBook = new EntryBook<>();

    private EntryBook<Recipe> recipeBook = new RecipeBookBuilder()
        .withRecipe(APRICOT_SALAD).withRecipe(BANANA_SALAD).build();
    private EntryBook<Recipe> differentRecipeBook = new EntryBook<>();

    private UserPrefs userPrefs = new chopchop.model.UserPrefs();

    private UsageList<RecipeUsage> recipeUL = new UsageList<>(getListViewRecipeList());
    private UsageList<RecipeUsage> differentRecipeUL = new UsageList<>();

    private UsageList<IngredientUsage> ingredientUL = new UsageList<>(getListViewIngredientList());
    private UsageList<IngredientUsage> differentIngredientUL = new UsageList<>();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new EntryBook<>(), new EntryBook<>(modelManager.getIngredientBook()));
        assertEquals(new EntryBook<>(), new EntryBook<>(modelManager.getRecipeBook()));
        assertEquals(new UsageList<>(), modelManager.getRecipeUsageList());
        assertEquals(new UsageList<>(), modelManager.getIngredientUsageList());
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
    public void setRecipe_notFound() {
        assertThrows(EntryNotFoundException.class, () -> {
            modelManager.setRecipe(new Recipe("owo salad", List.of(), List.of(), Set.of()), APRICOT_SALAD);
        });
    }

    @Test
    public void getFilteredRecipeList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredRecipeList().remove(0));
    }

    @Test
    public void equals() {
        // same values -> returns true
        modelManager = new ModelManager(recipeBook, ingredientBook,  recipeUL, ingredientUL, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(recipeBook, ingredientBook, recipeUL, ingredientUL, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // different ingredientBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(
            recipeBook, differentIngredientBook, recipeUL, ingredientUL, userPrefs)));

        // different recipeBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(
            differentRecipeBook, ingredientBook, recipeUL, ingredientUL, userPrefs)));

        // different recipeUsageList -> returns false
        assertFalse(modelManager.equals(new ModelManager(
            recipeBook, ingredientBook, differentRecipeUL, ingredientUL, userPrefs)));

        // different ingredientUsageList -> returns false
        assertFalse(modelManager.equals(new ModelManager(
            recipeBook, ingredientBook, recipeUL, differentIngredientUL, userPrefs)));

        // different filteredIngredientList -> returns false
        final String[] indKeywords = APRICOT.getName().split("\\s+");
        modelManager.updateFilteredIngredientList(new NameContainsKeywordsPredicate(Arrays.asList(indKeywords)));
        assertFalse(modelManager.equals(new ModelManager(recipeBook, ingredientBook, recipeUL, ingredientUL,
            userPrefs)));

        // different filteredRecipeList -> returns false
        final String[] recKeywords = APRICOT_SALAD.getName().split("\\s+");
        modelManager.updateFilteredRecipeList(new NameContainsKeywordsPredicate(Arrays.asList(recKeywords)));
        assertFalse(modelManager.equals(new ModelManager(recipeBook, ingredientBook, recipeUL, ingredientUL,
            userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredIngredientList(PREDICATE_SHOW_ALL_ENTRIES);
        modelManager.updateFilteredRecipeList(PREDICATE_SHOW_ALL_ENTRIES);

    }

    @Test
    void getMostMadeRecipeList_returnsEqual() {
        var ul = getRecipeUsageList();
        var model = new ModelManager(recipeBook, ingredientBook,  recipeUL, ingredientUL, userPrefs);
        assertEquals(ul.getMostUsed(), model.getMostMadeRecipeList());
    }

    @Test
    void setRecipeAndIngredientUsageList_returnsEqual() {
        var expectedModel = new ModelManager(recipeBook, ingredientBook,  recipeUL, ingredientUL, userPrefs);
        var model1 = new ModelManager(recipeBook, ingredientBook,  differentRecipeUL, ingredientUL, userPrefs);
        model1.setRecipeUsageList(recipeUL);
        assertEquals(expectedModel, model1);
        var model2 = new ModelManager(recipeBook, ingredientBook,  recipeUL, differentIngredientUL, userPrefs);
        model2.setIngredientUsageList(ingredientUL);
        assertEquals(expectedModel, model2);
    }

    @Test
    void getRecipesMadeBetween_returnsEqual() {
        var expectedFullList = getListViewRecipeList().stream()
            .map(Usage::getListViewPair)
            .collect(Collectors.toList());
        var expectedSubList = new ArrayList<>(Arrays.asList(RECIPE_A_B.getListViewPair(),
            RECIPE_B_B.getListViewPair()));
        var filledModel = new ModelManager(recipeBook, ingredientBook, recipeUL, ingredientUL, userPrefs);
        assertEquals(expectedFullList, filledModel.getRecipesMadeBetween(USAGE_DATE_A0, USAGE_DATE_E0));
        assertEquals(expectedSubList, filledModel.getRecipesMadeBetween(USAGE_DATE_A, USAGE_DATE_C));
        assertEquals(new ArrayList<>(), this.modelManager.getRecipesMadeBetween(USAGE_DATE_A, USAGE_DATE_C));
    }

    @Test
    void getIngredientsUsedBetween() {
        var expectedFullList = getListViewIngredientList().stream()
            .map(Usage::getListViewPair)
            .collect(Collectors.toList());
        var expectedSubList = new ArrayList<>(Arrays.asList(INGREDIENT_A_B.getListViewPair(),
            INGREDIENT_B_B.getListViewPair()));
        var filledModel = new ModelManager(recipeBook, ingredientBook, recipeUL, ingredientUL, userPrefs);
        assertEquals(expectedFullList, filledModel.getIngredientsUsedBetween(USAGE_DATE_A0, USAGE_DATE_E0));
        assertEquals(expectedSubList, filledModel.getIngredientsUsedBetween(USAGE_DATE_A, USAGE_DATE_C));
        assertEquals(new ArrayList<>(), this.modelManager.getIngredientsUsedBetween(USAGE_DATE_A, USAGE_DATE_C));
    }

    @Test
    void getRecentlyUsedRecipesAndIngredients() {
        var model = new ModelManager(recipeBook, ingredientBook,  recipeUL, ingredientUL, userPrefs);
        assertEquals(recipeUL.getRecentlyUsed(10), model.getRecentlyUsedRecipes(10));
        assertEquals(ingredientUL.getRecentlyUsed(10), model.getRecentlyUsedIngredients(10));
    }
}
