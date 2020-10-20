package chopchop.logic;

import java.nio.file.Path;

import chopchop.commons.core.GuiSettings;
import chopchop.logic.commands.CommandResult;
import chopchop.logic.commands.exceptions.CommandException;
import chopchop.logic.parser.exceptions.ParseException;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.recipe.Recipe;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the RecipeBook.
     */
    ReadOnlyEntryBook<Recipe> getRecipeBook();

    /**
     * Returns the IngredientBook.
     */
    ReadOnlyEntryBook<Ingredient> getIngredientBook();

    /**
     * Returns an unmodifiable view of the filtered list of recipes.
     */
    ObservableList<Recipe> getFilteredRecipeList();

    /**
     * Returns an unmodifiable view of the filtered ingredient list.
     */
    ObservableList<Ingredient> getFilteredIngredientList();

    /**
     * Returns the user prefs' recipe book file path.
     */
    Path getRecipeBookFilePath();

    /**
     * Returns the user prefs' ingredient book file path.
     */
    Path getIngredientBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
