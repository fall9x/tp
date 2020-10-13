package chopchop.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import chopchop.commons.core.GuiSettings;

import chopchop.model.attributes.Name;

import chopchop.model.recipe.Recipe;
import chopchop.model.recipe.ReadOnlyRecipeBook;

import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.ReadOnlyIngredientBook;

import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<FoodEntry> PREDICATE_SHOW_ALL_RECIPES = unused -> true;
    Predicate<FoodEntry> PREDICATE_SHOW_ALL_INGREDIENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' recipe book file path.
     */
    Path getRecipeBookFilePath();

    /**
     * Sets the user prefs' recipe book file path.
     */
    void setRecipeBookFilePath(Path recipeBookFilePath);

    /**
     * Replaces recipe book data with the data in {@code recipeBook}.
     */
    void setRecipeBook(ReadOnlyRecipeBook recipeBook);

    /** Returns the RecipeBook */
    ReadOnlyRecipeBook getRecipeBook();

    /**
     * Returns true if a recipe with the same identity as {@code recipe} exists in the recipe book.
     */
    boolean hasRecipe(Recipe recipe);

    /**
     * Finds a recipe by name.
     */
    Optional<Recipe> findRecipeWithName(String name);

    /**
     * Finds a recipe by name.
     */
    Optional<Recipe> findRecipeWithName(Name name);

    /**
     * Deletes the given recipe.
     * The recipe must exist in the recipe book.
     */
    void deleteRecipe(Recipe target);

    /**
     * Adds the given recipe.
     * {@code recipe} must not already exist in the recipe book.
     */
    void addRecipe(Recipe recipe);

    /**
     * Replaces the given recipe {@code target} with {@code editedRecipe}.
     * {@code target} must exist in the recipe book.
     * The recipe identity of {@code editedPerson} must not be the same as another existing recipe in the recipe book.
     */
    void setRecipe(Recipe target, Recipe editedRecipe);

    /** Returns an unmodifiable view of the filtered recipe list */
    ObservableList<Recipe> getFilteredRecipeList();

    /**
     * Updates the filter of the filtered recipe list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredRecipeList(Predicate<FoodEntry> predicate);

    /**
     * Returns the user prefs' ingredient book file path.
     */
    Path getIngredientBookFilePath();

    /**
     * Sets the user prefs' ingredient book file path.
     */
    void setIngredientBookFilePath(Path indBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setIngredientBook(ReadOnlyIngredientBook book);

    /** Returns the AddressBook */
    ReadOnlyIngredientBook getIngredientBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasIngredient(Ingredient ind);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deleteIngredient(Ingredient ind);

    /**
     * Finds an ingredient by name.
     */
    Optional<Ingredient> findIngredientWithName(String name);

    /**
     * Finds an ingredient by name.
     */
    Optional<Ingredient> findIngredientWithName(Name name);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addIngredient(Ingredient ind);

    /**
     * Replaces the given person {@code target} with {@code editedIngredient}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedIngredient} must not be the same as another existing person in the
     * address book.
     */
    void setIngredient(Ingredient target, Ingredient editedIngredient);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Ingredient> getFilteredIngredientList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredIngredientList(Predicate<FoodEntry> predicate);

}
