package chopchop.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.function.Predicate;

import chopchop.model.recipe.Recipe;
import chopchop.model.recipe.RecipeBook;
import chopchop.model.recipe.ReadOnlyRecipeBook;

import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientBook;
import chopchop.model.ingredient.ReadOnlyIngredientBook;

import chopchop.model.attributes.Name;

import chopchop.commons.core.GuiSettings;
import chopchop.commons.core.LogsCenter;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import static java.util.Objects.requireNonNull;
import static chopchop.commons.util.CollectionUtil.requireAllNonNull;


/**
 * Represents the in-memory model of the recipe book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final RecipeBook recipeBook;
    private final IngredientBook ingredientBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Recipe> filteredRecipes;
    private final FilteredList<Ingredient> filteredIngredients;


    /**
     * Initializes a ModelManager with the given recipeBook and userPrefs.
     */
    public ModelManager(ReadOnlyRecipeBook recipeBook, ReadOnlyIngredientBook ingredientBook,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(recipeBook, ingredientBook, userPrefs);

        logger.fine("Initializing with recipe book: " + recipeBook + " and user prefs " + userPrefs);
        this.userPrefs = new UserPrefs(userPrefs);
        this.recipeBook = new RecipeBook(recipeBook);
        filteredRecipes = new FilteredList<Recipe>(this.recipeBook.getFoodEntryList());
        logger.fine("Initializing with ingredient book: " + ingredientBook + " and user prefs " + userPrefs);
        this.ingredientBook = new IngredientBook(ingredientBook);
        filteredIngredients = new FilteredList<Ingredient>(this.ingredientBook.getFoodEntryList());

    }

    public ModelManager() {
        this(new RecipeBook(), new IngredientBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getRecipeBookFilePath() {
        return userPrefs.getRecipeBookFilePath();
    }

    @Override
    public void setRecipeBookFilePath(Path recipeBookFilePath) {
        requireNonNull(recipeBookFilePath);
        userPrefs.setRecipeBookFilePath(recipeBookFilePath);
    }



    //=========== AddressBook ================================================================================

    @Override
    public void setRecipeBook(ReadOnlyRecipeBook recipeBook) {
        this.recipeBook.resetData(recipeBook);
    }

    @Override
    public ReadOnlyRecipeBook getRecipeBook() {
        return recipeBook;
    }

    @Override
    public boolean hasRecipe(Recipe recipe) {
        requireNonNull(recipe);
        return recipeBook.hasRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe target) {
        recipeBook.removeRecipe(target);
    }

    @Override
    public void addRecipe(Recipe recipe) {
        recipeBook.addRecipe(recipe);
        updateFilteredRecipeList(PREDICATE_SHOW_ALL_RECIPES);
    }

    @Override
    public void setRecipe(Recipe target, Recipe editedRecipe) {
        requireAllNonNull(target, editedRecipe);

        recipeBook.setRecipe(target, editedRecipe);
    }

    @Override
    public Optional<Recipe> findRecipeWithName(String name) {
        return this.findRecipeWithName(new Name(name));
    }

    @Override
    public Optional<Recipe> findRecipeWithName(Name name) {
        return this.recipeBook.getFoodEntryList()
            .stream()
            .filter(r -> r.getName().equals(name))
            .findFirst();
    }


    //=========== Filtered Recipe List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Recipe} backed by the internal list of
     * {@code versionedRecipeBook}
     */
    @Override
    public ObservableList<Recipe> getFilteredRecipeList() {
        return filteredRecipes;
    }

    @Override
    public void updateFilteredRecipeList(Predicate<FoodEntry> predicate) {
        requireNonNull(predicate);
        filteredRecipes.setPredicate(predicate);
    }

    /**
     * Returns the user prefs' ingredient book file path.
     */
    @Override
    public Path getIngredientBookFilePath() {
        return userPrefs.getIngredientBookFilePath();
    }

    /**
     * Sets the user prefs' address book file path.
     *
     * @param indBookFilePath
     */
    @Override
    public void setIngredientBookFilePath(Path indBookFilePath) {
        requireNonNull(indBookFilePath);
        userPrefs.setIngredientBookFilePath(indBookFilePath);
    }

    //=========== Filtered Ingredient List Accessors =============================================================

    public void setIngredientBook(ReadOnlyIngredientBook ingredientBook) {
        this.ingredientBook.resetData(ingredientBook);
    }

    @Override
    public ReadOnlyIngredientBook getIngredientBook() {
        return ingredientBook;
    }

    @Override
    public boolean hasIngredient(Ingredient ind) {
        requireNonNull(ind);
        return ingredientBook.hasIngredient(ind);
    }

    @Override
    public void deleteIngredient(Ingredient target) {
        ingredientBook.removeIngredient(target);
    }

    @Override
    public void addIngredient(Ingredient ind) {
        ingredientBook.addIngredient(ind);
        updateFilteredIngredientList(PREDICATE_SHOW_ALL_INGREDIENTS);
    }

    @Override
    public void setIngredient(Ingredient target, Ingredient editedIngredient) {
        requireAllNonNull(target, editedIngredient);

        ingredientBook.setIngredient(target, editedIngredient);
    }

    @Override
    public Optional<Ingredient> findIngredientWithName(String name) {
        return this.findIngredientWithName(new Name(name));
    }

    @Override
    public Optional<Ingredient> findIngredientWithName(Name name) {
        return this.ingredientBook.getFoodEntryList()
            .stream()
            .filter(r -> r.getName().equals(name))
            .findFirst();
    }

    //=========== Filtered Ingredient List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Ingredient} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Ingredient> getFilteredIngredientList() {
        return filteredIngredients;
    }


    @Override
    public void updateFilteredIngredientList(Predicate<FoodEntry> predicate) {
        requireNonNull(predicate);
        filteredIngredients.setPredicate(predicate);

    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;

        return recipeBook.equals(other.recipeBook)
                && userPrefs.equals(other.userPrefs)
                && filteredRecipes.equals(other.filteredRecipes)
                && ingredientBook.equals(other.ingredientBook)
                && filteredIngredients.equals(other.filteredIngredients);

    }

}
