package chopchop.storage;

import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        var ingredientBookStorage = new JsonIngredientBookStorage(getTempFilePath("ab"));
        var recipeBookStorage = new JsonRecipeBookStorage(getTempFilePath("abc"));
        var userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        var recipeUsageStorage = new JsonRecipeUsageStorage(getTempFilePath("cc"));
        var ingredientUsageStorage = new JsonIngredientUsageStorage(getTempFilePath("dd"));
        storageManager = new StorageManager(recipeBookStorage, ingredientBookStorage, recipeUsageStorage,
            ingredientUsageStorage, userPrefsStorage);

        storageManager.getUserPrefsFilePath();
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


        assertEquals(Paths.get("owo"), new JsonUserPrefsStorage(Paths.get("owo")).getUserPrefsFilePath());
    }

    @Test
    public void test_guiSettings() {
        var s1 = new GuiSettings(300, 600, 4, 6);
        var s2 = new GuiSettings(300, 600, 4, 6);
        var s3 = new GuiSettings(300, 6, 9, 12);
        var s4 = new GuiSettings();
        var s5 = new GuiSettings(300, 600, 1, 2);

        // test GuiSettings
        assertEquals(300, s1.getWindowWidth());
        assertEquals(600, s1.getWindowHeight());
        assertEquals(new java.awt.Point(4, 6), s1.getWindowCoordinates());

        assertEquals(null, s4.getWindowCoordinates());

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        assertNotEquals(s1, s4);
        assertNotEquals(s1, s3);
        assertNotEquals(s1, s5);
        assertNotEquals(s1, "asdf");
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




    @Test
    public void test_getIngredientUsageFilePath() {
        assertNotNull(storageManager.getIngredientUsageFilePath());
    }

    @Test
    public void test_getRecipeUsageFilePath() {
        assertNotNull(storageManager.getRecipeUsageFilePath());
    }


    // @Test
    // public void ingredientUsageReadSave() throws Exception {

    //     EntryBook<Ingredient> original = getTypicalIngredientBook();
    //     storageManager.saveIngredientBook(original);
    //     ReadOnlyEntryBook<Ingredient> retrieved = storageManager.readIngredientBook().get();
    //     assertEquals(original, new EntryBook<>(retrieved));
    // }

    // @Test
    // public void recipeBookReadSave() throws Exception {
    //     EntryBook<Recipe> original = getTypicalRecipeBook();
    //     storageManager.saveRecipeBook(original);
    //     ReadOnlyEntryBook<Recipe> retrieved = storageManager.readRecipeBook().get();
    //     assertEquals(original, new EntryBook<>(retrieved));
    // }
}
