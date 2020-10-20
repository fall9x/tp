package chopchop.storage;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static chopchop.testutil.TypicalIngredients.CUSTARD;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import chopchop.model.EntryBook;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.ingredient.Ingredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import chopchop.commons.exceptions.DataConversionException;

public class JsonIngredientBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonIndBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readIngredientBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readIngredientBook(null));
    }

    private java.util.Optional<ReadOnlyEntryBook<Ingredient>> readIngredientBook(String filePath) throws Exception {
        return new JsonIngredientBookStorage(Paths.get(filePath))
            .readIngredientBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readIngredientBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readIngredientBook("notJsonFormatIndBook.json"));
    }

    @Test
    public void readIngredientBook_invalidPersonIngredientBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readIngredientBook("invalidIndBook.json"));
    }

    @Test
    public void readIngredientBook_invalidAndValidPersonIngredientBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readIngredientBook("invalidAndValidIndBook.json"));
    }

    @Test
    public void readAndSaveIngredientBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempIngredientBook.json");
        EntryBook<Ingredient> original = getTypicalIngredientBook();
        JsonIngredientBookStorage jsonIngredientBookStorage = new JsonIngredientBookStorage(filePath);

        // Save in new file and read back
        jsonIngredientBookStorage.saveIngredientBook(original, filePath);
        ReadOnlyEntryBook<Ingredient> readBack = jsonIngredientBookStorage.readIngredientBook(filePath).get();
        assertEquals(original.getEntryList(), new EntryBook<>(readBack).getEntryList());

        System.out.println(original.getEntryList().stream().map(x-> x.toString()).collect(Collectors.joining()));
        // Modify data, overwrite exiting file, and read back
        original.add(CUSTARD);
        original.remove(BANANA);
        jsonIngredientBookStorage.saveIngredientBook(original, filePath);
        readBack = jsonIngredientBookStorage.readIngredientBook(filePath).get();
        assertEquals(original, new EntryBook<>(readBack));

        // Save and read without specifying file path
        original.add(BANANA);
        jsonIngredientBookStorage.saveIngredientBook(original); // file path not specified
        readBack = jsonIngredientBookStorage.readIngredientBook().get(); // file path not specified
        assertEquals(original, new EntryBook<>(readBack));

    }

    @Test
    public void saveIngredientBook_nullIngredientBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveIngredientBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveIngredientBook(ReadOnlyEntryBook<Ingredient> addressBook, String filePath) {
        try {
            new JsonIngredientBookStorage(Paths.get(filePath))
                .saveIngredientBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveIngredientBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveIngredientBook(new EntryBook<>(), null));
    }

}
