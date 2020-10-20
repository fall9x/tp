package chopchop.storage;

import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.nio.file.Path;

import chopchop.model.EntryBook;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.recipe.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import chopchop.commons.core.GuiSettings;
import chopchop.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonIngredientBookStorage ingredientBookStorage = new JsonIngredientBookStorage(getTempFilePath("ab"));
        JsonRecipeBookStorage recipeBookStorage = new JsonRecipeBookStorage(getTempFilePath("abc"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(recipeBookStorage, ingredientBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void ingredientBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        EntryBook<Ingredient> original = getTypicalIngredientBook();
        storageManager.saveIngredientBook(original);
        ReadOnlyEntryBook<Ingredient> retrieved = storageManager.readIngredientBook().get();
        assertEquals(original, new EntryBook<>(retrieved));
    }

    @Test
    public void getIngredientBookFilePath() {
        assertNotNull(storageManager.getIngredientBookFilePath());
    }

    @Test
    public void recipeBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        EntryBook<Recipe> original = getTypicalRecipeBook();
        storageManager.saveRecipeBook(original);
        ReadOnlyEntryBook<Recipe> retrieved = storageManager.readRecipeBook().get();
        assertEquals(original, new EntryBook<>(retrieved));
    }

    @Test
    public void getRecipeBookFilePath() {
        assertNotNull(storageManager.getRecipeBookFilePath());
    }
}
