package chopchop.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import chopchop.commons.core.GuiSettings;
import chopchop.model.recipe.Recipe;
import chopchop.model.recipe.ReadOnlyRecipeBook;

import chopchop.model.attributes.Name;

import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.ReadOnlyIngredientBook;

import javafx.collections.ObservableList;


/**
 * A default model stub that have all of the methods failing.
 */
public abstract class ModelStub implements Model {
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
    public void setIngredientBook(ReadOnlyIngredientBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyIngredientBook getIngredientBook() {
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
    public void updateFilteredIngredientList(Predicate<FoodEntry> predicate) {
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
    public void setRecipeBook(ReadOnlyRecipeBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyRecipeBook getRecipeBook() {
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
    public void updateFilteredRecipeList(Predicate<FoodEntry> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Recipe> findRecipeWithName(String name) {
        return this.findRecipeWithName(new Name(name));
    }

    @Override
    public Optional<Recipe> findRecipeWithName(Name name) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Ingredient> findIngredientWithName(String name) {
        return this.findIngredientWithName(new Name(name));
    }

    @Override
    public Optional<Ingredient> findIngredientWithName(Name name) {
        throw new AssertionError("This method should not be called.");
    }
}
