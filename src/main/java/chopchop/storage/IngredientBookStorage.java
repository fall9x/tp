package chopchop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import chopchop.commons.exceptions.DataConversionException;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.ingredient.Ingredient;

public interface IngredientBookStorage {
    /**
     * Returns the file path of the data file.
     */
    Path getIngredientBookFilePath();

    /**
     * Returns IngredientBook data as a {@link ReadOnlyEntryBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     */
    Optional<ReadOnlyEntryBook<Ingredient>> readIngredientBook() throws DataConversionException;

    /**
     * @see #getIngredientBookFilePath()
     */
    Optional<ReadOnlyEntryBook<Ingredient>> readIngredientBook(Path filePath) throws DataConversionException;

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     * @param ingredientBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveIngredientBook(ReadOnlyEntryBook<Ingredient> ingredientBook) throws IOException;

    /**
     * @see #saveIngredientBook(ReadOnlyEntryBook)
     */
    void saveIngredientBook(ReadOnlyEntryBook<Ingredient> ingredientBook, Path filePath) throws IOException;
}
