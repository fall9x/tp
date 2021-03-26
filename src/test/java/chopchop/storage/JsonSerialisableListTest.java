// JsonSerialisableListTest.java

package chopchop.storage;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import chopchop.commons.exceptions.IllegalValueException;
import org.junit.jupiter.api.Test;

public class JsonSerialisableListTest {

    @Test
    public void test_recipeBook() {
        var m = JsonSerializableRecipeBook.MESSAGE_DUPLICATE_RECIPE;
        assertThrows(IllegalValueException.class, m, () -> {
            new JsonSerializableRecipeBook(List.of(
                new JsonAdaptedRecipe(APRICOT_SALAD),
                new JsonAdaptedRecipe(APRICOT_SALAD)
            )).toModelType();
        });
    }

    @Test
    public void test_ingredientBook() {
        var m = JsonSerializableIngredientBook.MESSAGE_DUPLICATE_INGREDIENT;
        assertThrows(IllegalValueException.class, m, () -> {
            new JsonSerializableIngredientBook(List.of(
                new JsonAdaptedIngredient(BANANA),
                new JsonAdaptedIngredient(BANANA)
            )).toModelType();
        });
    }

    @Test
    public void test_recipeUsage() {
        var u1 = new JsonAdaptedRecipeUsage("a", "2020-01-01T21:28:32.624831");
        var u2 = new JsonAdaptedRecipeUsage("b", "2020-01-30T21:28:32.624831");

        var ul1 = new JsonSerializableRecipeUsageList(List.of(u1, u2));

        try {
            var ul2 = ul1.toModelType();
            var ul3 = new JsonSerializableRecipeUsageList(ul2).toModelType();
        } catch (IllegalValueException e) {
            assertEquals(true, false);
        }
    }

    @Test
    public void test_ingredientUsage() {
        var u1 = new JsonAdaptedIngredientUsage("a", "2020-01-01T21:28:32.624831", "400ml");
        var u2 = new JsonAdaptedIngredientUsage("b", "2020-01-30T21:28:32.624831", "400ml");

        var ul1 = new JsonSerializableIngredientUsageList(List.of(u1, u2));

        try {
            var ul2 = ul1.toModelType();
            var ul3 = new JsonSerializableIngredientUsageList(ul2).toModelType();
        } catch (IllegalValueException e) {
            assertEquals(true, false);
        }
    }



    // @Test
    // public void test_usageList() {
    //     var dt1 = "2020-01-01T21:28:32.624831";
    //     var dt2 = "2020-01-30T21:28:32.624831";

    //     var u1 = new JsonAdaptedUsageList(List.of(dt1, dt2));

    //     var ul1 = new JsonSerializableIngredientUsageList(List.of(u1, u2));

    //     try {
    //         var ul2 = ul1.toModelType();
    //         var ul3 = new JsonSerializableIngredientUsageList(ul2).toModelType();
    //     } catch (IllegalValueException e) {
    //         assertEquals(true, false);
    //     }
    // }
}
