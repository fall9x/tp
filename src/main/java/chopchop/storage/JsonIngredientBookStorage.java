package chopchop.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import chopchop.commons.core.Log;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.commons.util.FileUtil;
import chopchop.commons.util.JsonUtil;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.ingredient.Ingredient;

public class JsonIngredientBookStorage implements IngredientBookStorage {
    private static final Log logger = new Log(JsonIngredientBookStorage.class);

    private final Path filePath;

    public JsonIngredientBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path of the data file.
     */
    @Override
    public Path getIngredientBookFilePath() {
        return this.filePath;
    }

    /**
     * Returns IngredientBook data as a {@link ReadOnlyEntryBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     */
    @Override
    public Optional<ReadOnlyEntryBook<Ingredient>> readIngredientBook() throws DataConversionException {
        return this.readIngredientBook(this.filePath);
    }

    /**
     * @param filePath
     * @see #getIngredientBookFilePath()
     */
    @Override
    public Optional<ReadOnlyEntryBook<Ingredient>> readIngredientBook(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableIngredientBook> jsonIngredientBook = JsonUtil.readJsonFile(
            filePath, JsonSerializableIngredientBook.class);
        if (jsonIngredientBook.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonIngredientBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.warn("json error ('%s'): %s", filePath, ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     *
     * @param ingredientBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    @Override
    public void saveIngredientBook(ReadOnlyEntryBook<Ingredient> ingredientBook) throws IOException {
        this.saveIngredientBook(ingredientBook, this.filePath);
    }

    /**
     * Updates the json file with the current state of ingredient book.
     *
     * @param ingredientBook updated ingredient book.
     * @param filePath relative path where the json file is at.
     * @see #saveIngredientBook(ReadOnlyEntryBook)
     */
    @Override
    public void saveIngredientBook(ReadOnlyEntryBook<Ingredient> ingredientBook, Path filePath) throws IOException {
        requireNonNull(ingredientBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableIngredientBook(ingredientBook), filePath);
    }
}
