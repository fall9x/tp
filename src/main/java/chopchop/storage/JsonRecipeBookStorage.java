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
import chopchop.model.recipe.Recipe;

public class JsonRecipeBookStorage implements RecipeBookStorage {
    private static final Log logger = new Log(JsonRecipeBookStorage.class);

    private final Path filePath;

    /**
     * Constucts a {@code JsonRecipeBookStorage} based on filePath.
     */
    public JsonRecipeBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path of the data file.
     */
    @Override
    public Path getRecipeBookFilePath() {
        return this.filePath;
    }

    /**
     * Returns RecipeBook data as a {@link ReadOnlyEntryBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     */
    @Override
    public Optional<ReadOnlyEntryBook<Recipe>> readRecipeBook() throws DataConversionException {
        return this.readRecipeBook(this.filePath);
    }

    /**
     * Read and parse the json file to a serializable recipe book
     *
     * @param filePath the relative path where the json file of recipe book is saved.
     * @see #getRecipeBookFilePath()
     */
    @Override
    public Optional<ReadOnlyEntryBook<Recipe>> readRecipeBook(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableRecipeBook> jsonRecipeBook = JsonUtil.readJsonFile(
            filePath, JsonSerializableRecipeBook.class);
        if (jsonRecipeBook.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonRecipeBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.warn("json error ('%s'): %s", filePath, ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     *
     * @param recipeBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    @Override
    public void saveRecipeBook(ReadOnlyEntryBook<Recipe> recipeBook) throws IOException {
        this.saveRecipeBook(recipeBook, this.filePath);
    }

    /**
     * Updates the json file with the current state of recipe book.
     *
     * @param recipeBook updated recipe book.
     * @param filePath relative path where the json file is at.
     * @see #saveRecipeBook(ReadOnlyEntryBook)
     */
    @Override
    public void saveRecipeBook(ReadOnlyEntryBook<Recipe> recipeBook, Path filePath) throws IOException {
        requireNonNull(recipeBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableRecipeBook(recipeBook), filePath);
    }
}
