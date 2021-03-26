package chopchop.model;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import chopchop.commons.core.GuiSettings;
import chopchop.commons.util.Pair;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.recipe.Recipe;

import chopchop.model.ingredient.Ingredient;

import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;
import javafx.collections.ObservableList;


/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getIngredientBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setIngredientBookFilePath(Path indBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setIngredientBook(ReadOnlyEntryBook<Ingredient> newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyEntryBook<Ingredient> getIngredientBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasIngredient(Ingredient ingredient) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteIngredient(Ingredient target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setIngredient(Ingredient target, Ingredient editedIngredient) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Ingredient> getFilteredIngredientList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredIngredientList(Predicate<? super Ingredient> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void startEditingIngredients() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void finishEditingIngredients() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getRecipeBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setRecipeBookFilePath(Path indBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addRecipe(Recipe recipe) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setRecipeBook(ReadOnlyEntryBook<Recipe> newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyEntryBook<Recipe> getRecipeBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasRecipe(Recipe recipe) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteRecipe(Recipe target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setRecipe(Recipe target, Recipe editedRecipe) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Recipe> getFilteredRecipeList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredRecipeList(Predicate<? super Recipe> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Recipe> findRecipeWithName(String name) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Ingredient> findIngredientWithName(String name) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public UsageList<RecipeUsage> getRecipeUsageList() {
        throw new AssertionError("This method should not be called.");
    }

    /**
     * Return the List sorted by most made recipe.
     */
    @Override
    public List<Pair<String, String>> getMostMadeRecipeList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public UsageList<IngredientUsage> getIngredientUsageList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<RecipeUsage> getObservableRecipeUsages() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<IngredientUsage> getObservableIngredientUsages() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addRecipeUsage(Recipe recipe) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void removeRecipeUsage(Recipe recipe) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addIngredientUsage(IngredientReference ingredient) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void removeIngredientUsage(IngredientReference ingredient) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setRecipeUsageList(UsageList<RecipeUsage> rl) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setIngredientUsageList(UsageList<IngredientUsage> rl) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public List<Pair<String, String>> getRecipesMadeBetween(LocalDateTime after, LocalDateTime before) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public List<Pair<String, String>> getIngredientsUsedBetween(LocalDateTime after, LocalDateTime before) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public List<Pair<String, String>> getRecentlyUsedRecipes(int n) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public List<Pair<String, String>> getRecentlyUsedIngredients(int n) {
        throw new AssertionError("This method should not be called.");
    }
}
