package chopchop.model.recipe;

import java.util.List;
import java.util.Set;

import static chopchop.testutil.TypicalIngredients.BANANA_REF;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import chopchop.model.attributes.Step;
import chopchop.model.attributes.Tag;
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


        assertEquals(APRICOT_SALAD.hashCode(), APRICOT_SALAD.hashCode());
        assertTrue(APRICOT_SALAD.isSame(new RecipeBuilder().withName("Apricot Salad").build()));
        assertFalse(APRICOT_SALAD.isSame(new IngredientBuilder().build()));


        assertNotEquals(APRICOT_SALAD, new RecipeBuilder().withName("Apricot Salad")
            .withIngredients(List.of(BANANA_REF)).build());

        assertNotEquals(APRICOT_SALAD, new RecipeBuilder(APRICOT_SALAD)
            .withSteps(List.of(new Step("asdf"), new Step("qwer"))).build());

        assertNotEquals(APRICOT_SALAD, new RecipeBuilder(APRICOT_SALAD)
            .withTags(Set.of(new Tag("asdf"), new Tag("qwer"))).build());
    }
}
