package chopchop.logic.commands;

import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import chopchop.model.attributes.IngredientsContainsKeywordsPredicate;
import chopchop.model.attributes.NameContainsKeywordsFilterPredicate;
import chopchop.model.attributes.TagContainsKeywordsPredicate;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;
import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;

public class FilterRecipeCommandTest {
    private Model model = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(), new UsageList<RecipeUsage>(),
        new UsageList<IngredientUsage>(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(),
        new UsageList<RecipeUsage>(), new UsageList<IngredientUsage>(), new UserPrefs());

    @Test
    public void execute_multipleNames_noRecipeFound() {
        var namePredicate = prepareNamePredicate("Salad", "Spicy");
        var command = new FilterRecipeCommand(null, null, namePredicate);
        expectedModel.updateFilteredRecipeList(namePredicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleNames_multipleRecipesFound() {
        var namePredicate = prepareNamePredicate("Salad", "sa", "lad");
        var command = new FilterRecipeCommand(null, null, namePredicate);
        expectedModel.updateFilteredRecipeList(namePredicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD, BANANA_SALAD), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleTags_noRecipeFound() {
        var tagPredicate = prepareTagPredicate("Salad", "Spicy");
        var command = new FilterRecipeCommand(tagPredicate, null, null);
        expectedModel.updateFilteredRecipeList(tagPredicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleTags_multipleRecipesFound() {
        var tagPredicate = prepareTagPredicate("Salad", "Cold Food");
        var command = new FilterRecipeCommand(tagPredicate, null, null);
        expectedModel.updateFilteredRecipeList(tagPredicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD, BANANA_SALAD), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleIngredients_moreThanZeroRecipeFound() {
        var indPredicate = prepareIngredientPredicate("Apricot", "Custard");
        var command = new FilterRecipeCommand(null, indPredicate, null);
        expectedModel.updateFilteredRecipeList(indPredicate);
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleNamesIngredients_multipleRecipesFound() {
        var namePredicate = prepareNamePredicate("Salad", "sa", "lad");
        var indPredicate = prepareIngredientPredicate("Custard");
        var command = new FilterRecipeCommand(null, indPredicate, namePredicate);
        expectedModel.updateFilteredRecipeList(indPredicate.and(namePredicate));
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD, BANANA_SALAD), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleNamesIngredients_noRecipeFound() {
        var namePredicate = prepareNamePredicate("Salad", "lady");
        var indPredicate = prepareIngredientPredicate("Custard");
        var command = new FilterRecipeCommand(null, indPredicate, namePredicate);
        expectedModel.updateFilteredRecipeList(indPredicate.and(namePredicate));
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleTagsIngredients_multipleRecipesFound() {
        var tagPredicate = prepareTagPredicate("Salad", "Cold Food");
        var indPredicate = prepareIngredientPredicate("Custard");
        var command = new FilterRecipeCommand(tagPredicate, indPredicate, null);
        expectedModel.updateFilteredRecipeList(indPredicate.and(tagPredicate));
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD, BANANA_SALAD), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleTagsIngredients_noRecipeFound() {
        var tagPredicate = prepareTagPredicate("Spicy", "Salad");
        var indPredicate = prepareIngredientPredicate("Custard");
        var command = new FilterRecipeCommand(tagPredicate, indPredicate, null);
        expectedModel.updateFilteredRecipeList(indPredicate.and(tagPredicate));
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleNamesTagsIngredients_multipleRecipeFound() {
        var namePredicate = prepareNamePredicate("Salad", "sa", "lad");
        var tagPredicate = prepareTagPredicate("Cold Food", "Salad");
        var indPredicate = prepareIngredientPredicate("Custard");
        var command = new FilterRecipeCommand(tagPredicate, indPredicate, namePredicate);
        expectedModel.updateFilteredRecipeList(indPredicate.and(tagPredicate).and(namePredicate));
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(APRICOT_SALAD, BANANA_SALAD), model.getFilteredRecipeList());
    }

    @Test
    public void execute_multipleNamesTagsIngredients_noRecipeFound() {
        var namePredicate = prepareNamePredicate("Salad", "lady");
        var tagPredicate = prepareTagPredicate("Spicy", "Salad");
        var indPredicate = prepareIngredientPredicate("Custard");
        var command = new FilterRecipeCommand(tagPredicate, indPredicate, namePredicate);
        expectedModel.updateFilteredRecipeList(indPredicate.and(tagPredicate).and(namePredicate));
        assertCommandSuccess(command, model, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredRecipeList());
    }

    /**
     * Parses {@code userInputs} into a {@code NameContainsKeywordsFilterPredicate}.
     */
    private NameContainsKeywordsFilterPredicate prepareNamePredicate(String... userInputs) {
        return new NameContainsKeywordsFilterPredicate(Arrays.asList(userInputs));
    }

    /**
     * Parses {@code userInputs} into a {@code TagContainsKeywordsPredicate}.
     */
    private TagContainsKeywordsPredicate prepareTagPredicate(String... userInputs) {
        return new TagContainsKeywordsPredicate(Arrays.asList(userInputs));
    }

    /**
     * Parses {@code userInputs} into a {@code IngredientContainsKeywordsPredicate}.
     */
    private IngredientsContainsKeywordsPredicate prepareIngredientPredicate(String... userInputs) {
        return new IngredientsContainsKeywordsPredicate(Arrays.asList(userInputs));
    }
}
