package chopchop.storage;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.commons.util.FileUtil;
import chopchop.commons.util.JsonUtil;
import chopchop.model.ingredient.ReadOnlyIngredientBook;
import chopchop.commons.core.LogsCenter;

public class JsonIngredientBookStorage implements IngredientBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonIngredientBookStorage.class);

    private Path filePath;

    public JsonIngredientBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path of the data file.
     */
    @Override
    public Path getIngredientBookFilePath() {
        return filePath;
    }

    /**
     * Returns IngredientBook data as a {@link ReadOnlyIngredientBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    @Override
    public Optional<ReadOnlyIngredientBook> readIngredientBook() throws DataConversionException, IOException {
        return readIngredientBook(filePath);
    }

    /**
     * @param filePath
     * @see #getIngredientBookFilePath()
     */
    @Override
    public Optional<ReadOnlyIngredientBook> readIngredientBook(Path filePath)
        throws DataConversionException, IOException {
        requireNonNull(filePath);

        Optional<JsonSerializableIngredientBook> jsonIndBook = JsonUtil.readJsonFile(
            filePath, JsonSerializableIngredientBook.class);
        if (!jsonIndBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonIndBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Saves the given {@link ReadOnlyIngredientBook} to the storage.
     *
     * @param ingredientBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    @Override
    public void saveIngredientBook(ReadOnlyIngredientBook ingredientBook) throws IOException {
        saveIngredientBook(ingredientBook, filePath);
    }

    /**
     * Updates the json file with the current state of ingredient book.
     *
     * @param ingredientBook updated ingredient book.
     * @param filePath relative path where the json file is at.
     * @see #saveIngredientBook(ReadOnlyIngredientBook)
     */
    @Override
    public void saveIngredientBook(ReadOnlyIngredientBook ingredientBook, Path filePath) throws IOException {
        requireNonNull(ingredientBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableIngredientBook(ingredientBook), filePath);
    }
}
