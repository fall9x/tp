package chopchop.model.recipe;

import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chopchop.testutil.IngredientBuilder;
import chopchop.testutil.RecipeBuilder;
import org.junit.jupiter.api.Test;

public class RecipeTest {

    @Test
    public void equals() {
        // same values -> returns true
        Recipe apricotSaladCopy = new RecipeBuilder(APRICOT_SALAD).build();
        assertTrue(APRICOT_SALAD.equals(apricotSaladCopy));

        // same object -> returns true
        assertTrue(APRICOT_SALAD.equals(APRICOT_SALAD));

        // null -> returns false
        assertFalse(APRICOT_SALAD.equals(null));

        // different type -> returns false
        assertFalse(APRICOT_SALAD.equals(new IngredientBuilder().build()));

        // different recipe -> returns false
        assertFalse(APRICOT_SALAD.equals(BANANA_SALAD));

        // different name -> returns false
        Recipe editedApricotSalad = new RecipeBuilder(APRICOT_SALAD).withName("DD").build();
        assertFalse(APRICOT_SALAD.equals(editedApricotSalad));
    }
}
