package chopchop.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import chopchop.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();

    private Path ingredientBookFilePath = Paths.get("data" , "ingredientbook.json");
    private Path recipeBookFilePath = Paths.get("data" , "recipebook.json");

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setIngredientBookFilePath(newUserPrefs.getIngredientBookFilePath());
        setRecipeBookFilePath(newUserPrefs.getRecipeBookFilePath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getIngredientBookFilePath() {
        return ingredientBookFilePath;
    }

    public Path getRecipeBookFilePath() {
        return this.recipeBookFilePath;
    }

    public void setIngredientBookFilePath(Path bookFilePath) {
        requireNonNull(bookFilePath);
        this.ingredientBookFilePath = bookFilePath;
    }

    public void setRecipeBookFilePath(Path bookFilePath) {
        requireNonNull(bookFilePath);
        this.recipeBookFilePath = bookFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
            && ingredientBookFilePath.equals(o.ingredientBookFilePath)
            && recipeBookFilePath.equals(o.recipeBookFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, ingredientBookFilePath, recipeBookFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data ingredient file location : " + ingredientBookFilePath);
        sb.append("\nLocal data recipe file location : " + recipeBookFilePath);
        return sb.toString();
    }
}
