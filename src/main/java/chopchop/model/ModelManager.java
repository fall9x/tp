package chopchop.model;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import chopchop.commons.core.GuiSettings;
import chopchop.commons.core.Log;
import chopchop.commons.util.Pair;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.recipe.Recipe;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the recipe and ingredient book data.
 */
public class ModelManager implements Model {
    private static final Log logger = new Log(ModelManager.class);

    private final UserPrefs userPrefs;
    private final EntryBook<Recipe> recipeBook;
    private final EntryBook<Ingredient> ingredientBook;
    private final FilteredList<Recipe> filteredRecipes;
    private final FilteredList<Ingredient> filteredIngredients;
    private final UsageList<RecipeUsage> recipeUsageList;
    private final UsageList<IngredientUsage> ingredientUsageList;

    /**
     * Initializes a ModelManager with the given RecipeBook, IngredientBook and userPrefs.
     */
    public ModelManager(ReadOnlyEntryBook<Recipe> recipeBook, ReadOnlyEntryBook<Ingredient> ingredientBook,
                        UsageList<RecipeUsage> recipeUsageList, UsageList<IngredientUsage> ingredientUsageList,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(recipeBook, ingredientBook, userPrefs, recipeUsageList, ingredientUsageList);

        logger.debug("recipe book: %s", recipeBook);
        logger.debug("ingredient book: %s", ingredientBook);
        logger.debug("user prefs: %s", userPrefs);

        this.userPrefs = new UserPrefs(userPrefs);
        this.recipeBook = new EntryBook<>(recipeBook);
        this.ingredientBook = new EntryBook<>(ingredientBook);
        this.filteredRecipes = new FilteredList<>(this.recipeBook.getEntryList());
        this.filteredIngredients = new FilteredList<>(this.ingredientBook.getEntryList());

        this.recipeUsageList = recipeUsageList;
        this.ingredientUsageList = ingredientUsageList;
    }

    /**
     * Constructs an empty {@code ModelManger}.
     */
    public ModelManager() {
        this(new EntryBook<>(), new EntryBook<>(), new UsageList<RecipeUsage>(), new UsageList<IngredientUsage>(),
            new UserPrefs());
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return this.userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return this.userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getRecipeBookFilePath() {
        return this.userPrefs.getRecipeBookFilePath();
    }

    @Override
    public void setRecipeBookFilePath(Path recipeBookFilePath) {
        requireNonNull(recipeBookFilePath);
        this.userPrefs.setRecipeBookFilePath(recipeBookFilePath);
    }

    @Override
    public void setRecipeBook(ReadOnlyEntryBook<Recipe> recipeBook) {
        this.recipeBook.resetData(recipeBook);
    }

    @Override
    public ReadOnlyEntryBook<Recipe> getRecipeBook() {
        return this.recipeBook;
    }

    @Override
    public boolean hasRecipe(Recipe recipe) {
        requireNonNull(recipe);
        return this.recipeBook.has(recipe);
    }

    @Override
    public void deleteRecipe(Recipe target) {
        this.recipeBook.remove(target);
    }

    @Override
    public void addRecipe(Recipe recipe) {
        this.recipeBook.add(recipe);
        this.updateFilteredRecipeList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void setRecipe(Recipe target, Recipe editedRecipe) {
        requireAllNonNull(target, editedRecipe);

        this.recipeBook.set(target, editedRecipe);
    }

    @Override
    public Optional<Recipe> findRecipeWithName(String name) {
        return this.recipeBook.getEntryList()
            .stream()
            .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    /**
     * Returns an unmodifiable view of the list of {@code Recipe} backed by the internal list of
     * {@code versionedEntryBook<Recipe>}
     */
    @Override
    public ObservableList<Recipe> getFilteredRecipeList() {
        return this.filteredRecipes;
    }

    @Override
    public void updateFilteredRecipeList(Predicate<? super Recipe> predicate) {
        requireNonNull(predicate);
        this.filteredRecipes.setPredicate(predicate);
    }

    /**
     * Returns the user prefs' ingredient book file path.
     */
    @Override
    public Path getIngredientBookFilePath() {
        return this.userPrefs.getIngredientBookFilePath();
    }

    /**
     * Sets the user prefs' ingredient book file path.
     */
    @Override
    public void setIngredientBookFilePath(Path ingredientBookFilePath) {
        requireNonNull(ingredientBookFilePath);
        this.userPrefs.setIngredientBookFilePath(ingredientBookFilePath);
    }

    public void setIngredientBook(ReadOnlyEntryBook<Ingredient> ingredientBook) {
        this.ingredientBook.resetData(ingredientBook);
    }

    @Override
    public ReadOnlyEntryBook<Ingredient> getIngredientBook() {
        return this.ingredientBook;
    }

    @Override
    public boolean hasIngredient(Ingredient ingredient) {
        requireNonNull(ingredient);
        return this.ingredientBook.has(ingredient);
    }

    @Override
    public void deleteIngredient(Ingredient target) {
        this.ingredientBook.remove(target);
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        this.ingredientBook.add(ingredient);
        this.updateFilteredIngredientList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void setIngredient(Ingredient target, Ingredient editedIngredient) {
        requireAllNonNull(target, editedIngredient);

        this.ingredientBook.set(target, editedIngredient);
    }

    @Override
    public Optional<Ingredient> findIngredientWithName(String name) {
        return this.ingredientBook.getEntryList()
            .stream()
            .filter(ingredient -> ingredient.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    /**
     * Returns an unmodifiable view of the list of {@code Ingredient} backed by the internal list of
     * {@code FilteredList}
     */
    @Override
    public ObservableList<Ingredient> getFilteredIngredientList() {
        return this.filteredIngredients;
    }

    @Override
    public void updateFilteredIngredientList(Predicate<? super Ingredient> predicate) {
        requireNonNull(predicate);
        this.filteredIngredients.setPredicate(predicate);
    }

    @Override
    public void startEditingIngredients() {
        this.ingredientBook.startEditing();
    }

    @Override
    public void finishEditingIngredients() {
        this.ingredientBook.finishEditing();
    }


    /**
     * Returns a copy of RecipeUsage list.
     */
    @Override
    public UsageList<RecipeUsage> getRecipeUsageList() {
        return new UsageList<>(this.recipeUsageList.getUsageList());
    }

    /**
     * Return the UsageList sorted by most used recipe.
     */
    @Override
    public List<Pair<String, String>> getMostMadeRecipeList() {
        return this.recipeUsageList.getMostUsed();
    }

    /**
     * Returns a copy of IngredientUsage list.
     */
    @Override
    public UsageList<IngredientUsage> getIngredientUsageList() {
        return new UsageList<>(this.ingredientUsageList.getUsageList());
    }

    @Override
    public ObservableList<RecipeUsage> getObservableRecipeUsages() {
        return this.recipeUsageList.getUsages();
    }

    @Override
    public ObservableList<IngredientUsage> getObservableIngredientUsages() {
        return this.ingredientUsageList.getUsages();
    }

    /**
     * Sets the RecipeUsageList
     */
    @Override
    public void setRecipeUsageList(UsageList<RecipeUsage> rl) {
        this.recipeUsageList.setAll(rl);
    }

    /**
     * Sets the IngredientUsageList
     */
    @Override
    public void setIngredientUsageList(UsageList<IngredientUsage> il) {
        this.ingredientUsageList.setAll(il);
    }

    @Override
    public List<Pair<String, String>> getRecipesMadeBetween(LocalDateTime after, LocalDateTime before) {
        return this.recipeUsageList.getUsagesBetween(after, before);
    }

    @Override
    public List<Pair<String, String>> getIngredientsUsedBetween(LocalDateTime after, LocalDateTime before) {
        return this.ingredientUsageList.getUsagesBetween(after, before);
    }

    @Override
    public void addRecipeUsage(Recipe recipe) {
        RecipeUsage usage = new RecipeUsage(recipe.getName(), LocalDateTime.now());
        this.recipeUsageList.add(usage);
    }

    @Override
    public void removeRecipeUsage(Recipe recipe) {
        this.recipeUsageList.pop(recipe.getName());
    }

    @Override
    public void addIngredientUsage(IngredientReference ingredient) {
        IngredientUsage usage = new IngredientUsage(ingredient.getName(), LocalDateTime.now(),
            ingredient.getQuantity());
        this.ingredientUsageList.add(usage);
    }

    @Override
    public void removeIngredientUsage(IngredientReference ingredient) {
        this.ingredientUsageList.pop(ingredient.getName());
    }

    @Override
    public List<Pair<String, String>> getRecentlyUsedRecipes(int n) {
        return this.recipeUsageList.getRecentlyUsed(n);
    }

    @Override
    public List<Pair<String, String>> getRecentlyUsedIngredients(int n) {
        return this.ingredientUsageList.getRecentlyUsed(n);
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

        return this.userPrefs.equals(other.userPrefs)
                && this.recipeBook.equals(other.recipeBook)
                && this.filteredRecipes.equals(other.filteredRecipes)
                && this.ingredientBook.equals(other.ingredientBook)
                && this.filteredIngredients.equals(other.filteredIngredients)
                && this.recipeUsageList.equals(other.recipeUsageList)
                && this.ingredientUsageList.equals(other.ingredientUsageList);
    }
}
