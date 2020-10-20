package chopchop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import chopchop.commons.exceptions.DataConversionException;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.recipe.Recipe;

public interface RecipeBookStorage {
    /**
     * Returns the file path of the data file.
     */
    Path getRecipeBookFilePath();

    /**
     * Returns RecipeBook data as a {@link ReadOnlyEntryBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     */
    Optional<ReadOnlyEntryBook<Recipe>> readRecipeBook() throws DataConversionException;

    /**
     * @see #getRecipeBookFilePath()
     */
    Optional<ReadOnlyEntryBook<Recipe>> readRecipeBook(Path filePath) throws DataConversionException;

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     * @param recipeBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveRecipeBook(ReadOnlyEntryBook<Recipe> recipeBook) throws IOException;

    /**
     * @see #saveRecipeBook(ReadOnlyEntryBook)
     */
    void saveRecipeBook(ReadOnlyEntryBook<Recipe> recipeBook, Path filePath) throws IOException;
}
