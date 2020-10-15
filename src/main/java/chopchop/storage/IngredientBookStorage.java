package chopchop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.model.ingredient.ReadOnlyIngredientBook;

public interface IngredientBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getIngredientBookFilePath();

    /**
     * Returns IngredientBook data as a {@link ReadOnlyIngredientBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyIngredientBook> readIngredientBook() throws DataConversionException, IOException;

    /**
     * @see #getIngredientBookFilePath()
     */
    Optional<ReadOnlyIngredientBook> readIngredientBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyIngredientBook} to the storage.
     * @param ingredientBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveIngredientBook(ReadOnlyIngredientBook ingredientBook) throws IOException;

    /**
     * @see #saveIngredientBook(ReadOnlyIngredientBook)
     */
    void saveIngredientBook(ReadOnlyIngredientBook ingredientBook, Path filePath) throws IOException;

}
