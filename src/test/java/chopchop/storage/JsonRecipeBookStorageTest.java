package chopchop.storage;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static chopchop.testutil.TypicalRecipes.CUSTARD_SALAD;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import chopchop.model.EntryBook;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.recipe.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import chopchop.commons.exceptions.DataConversionException;

public class JsonRecipeBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonRecipeBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readRecipeBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readRecipeBook(null));
    }

    private java.util.Optional<ReadOnlyEntryBook<Recipe>> readRecipeBook(String filePath) throws Exception {
        return new JsonRecipeBookStorage(Paths.get(filePath))
            .readRecipeBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readRecipeBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readRecipeBook("notJsonRecBook.json"));
    }

    @Test
    public void readRecipeBook_invalidPersonRecipeBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readRecipeBook("invalidRecBook.json"));
    }

    @Test
    public void readRecipeBook_invalidAndValidPersonRecipeBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readRecipeBook("invalidAndValidRecBook.json"));
    }


    @Test
    public void readAndSaveRecipeBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempRecipeBook.json");
        EntryBook<Recipe> original = getTypicalRecipeBook();
        JsonRecipeBookStorage jsonRecipeBookStorage = new JsonRecipeBookStorage(filePath);

        // Save in new file and read back
        jsonRecipeBookStorage.saveRecipeBook(original, filePath);
        ReadOnlyEntryBook<Recipe> readBack = jsonRecipeBookStorage.readRecipeBook(filePath).get();
        assertEquals(original.getEntryList(), new EntryBook<>(readBack).getEntryList());

        System.out.println(original.getEntryList().stream().map(x-> x.toString()).collect(Collectors.joining()));
        // Modify data, overwrite exiting file, and read back
        original.add(CUSTARD_SALAD);
        original.remove(BANANA_SALAD);
        jsonRecipeBookStorage.saveRecipeBook(original, filePath);
        readBack = jsonRecipeBookStorage.readRecipeBook(filePath).get();
        assertEquals(original, new EntryBook<>(readBack));

        // Save and read without specifying file path
        original.add(BANANA_SALAD);
        jsonRecipeBookStorage.saveRecipeBook(original); // file path not specified
        readBack = jsonRecipeBookStorage.readRecipeBook().get(); // file path not specified
        assertEquals(original, new EntryBook<>(readBack));

    }

    @Test
    public void saveRecipeBook_nullRecipeBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveRecipeBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveRecipeBook(ReadOnlyEntryBook<Recipe> addressBook, String filePath) {
        try {
            new JsonRecipeBookStorage(Paths.get(filePath))
                .saveRecipeBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveRecipeBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveRecipeBook(new EntryBook<>(), null));
    }
}
