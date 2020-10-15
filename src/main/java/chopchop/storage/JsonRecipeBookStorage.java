package chopchop.storage;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import chopchop.commons.core.LogsCenter;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.commons.util.FileUtil;
import chopchop.commons.util.JsonUtil;
import chopchop.model.recipe.ReadOnlyRecipeBook;

public class JsonRecipeBookStorage implements RecipeBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonRecipeBookStorage.class);

    private Path filePath;

    public JsonRecipeBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path of the data file.
     */
    @Override
    public Path getRecipeBookFilePath() {
        return filePath;
    }

    /**
     * Returns RecipeBook data as a {@link ReadOnlyRecipeBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    @Override
    public Optional<ReadOnlyRecipeBook> readRecipeBook() throws DataConversionException, IOException {
        return readRecipeBook(filePath);
    }

    /**
     * Read and parse the json file to a serializable recipe book
     *
     * @param filePath the relative path where the json file of recipe book is saved.
     * @see #getRecipeBookFilePath()
     */
    @Override
    public Optional<ReadOnlyRecipeBook> readRecipeBook(Path filePath) throws DataConversionException, IOException {
        requireNonNull(filePath);

        Optional<JsonSerializableRecipeBook> jsonIndBook = JsonUtil.readJsonFile(
            filePath, JsonSerializableRecipeBook.class);
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
     * Saves the given {@link ReadOnlyRecipeBook} to the storage.
     *
     * @param recipeBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    @Override
    public void saveRecipeBook(ReadOnlyRecipeBook recipeBook) throws IOException {
        saveRecipeBook(recipeBook, filePath);
    }

    /**
     * Updates the json file with the current state of recipe book.
     *
     * @param recipeBook updated recipe book.
     * @param filePath relative path where the json file is at.
     * @see #saveRecipeBook(ReadOnlyRecipeBook)
     */
    @Override
    public void saveRecipeBook(ReadOnlyRecipeBook recipeBook, Path filePath) throws IOException {
        requireNonNull(recipeBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableRecipeBook(recipeBook), filePath);
    }
}
