// UsageStorageTest.java

package chopchop.storage;

import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;


public class UsageStorageTest {

    private final Path path = Paths.get("src", "test", "data", "UsageStorageTest");

    private void testRecipeOk(Path path) {
        var u = new JsonRecipeUsageStorage(path);
        assertEquals(path, u.getUsageFilePath());

        try {
            var ul = u.readUsages();
            if (ul.isPresent()) {
                u.saveUsages(ul.get());
            }
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    private void testRecipeError(Path path) {
        var u = new JsonRecipeUsageStorage(path);
        assertEquals(path, u.getUsageFilePath());

        assertThrows(Exception.class, () -> u.readUsages());
    }


    private void testIngredientOk(Path path) {
        var u = new JsonIngredientUsageStorage(path);
        assertEquals(path, u.getUsageFilePath());

        try {
            var ul = u.readUsages();
            if (ul.isPresent()) {
                u.saveUsages(ul.get());
            }
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    private void testIngredientError(Path path) {
        var u = new JsonIngredientUsageStorage(path);
        assertEquals(path, u.getUsageFilePath());

        assertThrows(Exception.class, () -> u.readUsages());
    }


    @Test
    public void test_recipeUsageLoad() {
        testRecipeOk(path.resolve("recipe_kekw.json"));
        testRecipeOk(path.resolve("recipe_0.json"));
        testRecipeOk(path.resolve("recipe_1.json"));

        testRecipeError(path.resolve("recipe_2.json"));
        testRecipeError(path.resolve("recipe_3.json"));
    }

    @Test
    public void test_ingredientUsageLoad() {
        testIngredientOk(path.resolve("ingredient_kekw.json"));
        testIngredientOk(path.resolve("ingredient_0.json"));
        testIngredientOk(path.resolve("ingredient_1.json"));

        testIngredientError(path.resolve("ingredient_2.json"));
        testIngredientError(path.resolve("ingredient_3.json"));
    }
}
