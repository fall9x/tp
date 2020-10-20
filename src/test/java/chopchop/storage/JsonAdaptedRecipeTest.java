package chopchop.storage;

import static chopchop.storage.JsonAdaptedRecipe.RECIPE_MISSING_FIELD_MESSAGE_FORMAT;
import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.BANANA_SALAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Step;
import chopchop.model.ingredient.IngredientReference;

public class JsonAdaptedRecipeTest {
    private static final String INVALID_NAME = "";
    private static final JsonAdaptedIngredientReference INVALID_REF = new JsonAdaptedIngredientReference(null, null);
    private static final String INVALID_STEP = "";

    private static final String VALID_NAME = APRICOT_SALAD.getName();
    private static final List<JsonAdaptedIngredientReference> VALID_REFS = BANANA_SALAD.getIngredients()
        .stream().map(JsonAdaptedIngredientReference::new).collect(Collectors.toList());
    private static final List<String> VALID_STEPS = BANANA_SALAD.getSteps()
        .stream().map(Step::toString).collect(Collectors.toList());

    @Test
    public void toModelType_validRecipeDetails_returnsRecipe() throws Exception {
        JsonAdaptedRecipe recipe = new JsonAdaptedRecipe(BANANA_SALAD);
        assertEquals(BANANA_SALAD, recipe.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedRecipe recipe =
            new JsonAdaptedRecipe(INVALID_NAME, VALID_REFS,  VALID_STEPS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedRecipe recipe = new JsonAdaptedRecipe(null, VALID_REFS, VALID_STEPS);
        String expectedMessage = String.format(RECIPE_MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    //IngredientReference has null values.
    @Test
    public void toModelType_invalidReference_throwsIllegalValueException() {
        JsonAdaptedRecipe recipe =
            new JsonAdaptedRecipe(VALID_NAME, Collections.singletonList(INVALID_REF), VALID_STEPS);
        String expectedMessage = String.format(
            JsonAdaptedIngredientReference.INGREDIENT_REFERENCE_MISSING_FIELD_MESSAGE_FORMAT,
            Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullReference_throwsIllegalValueException() {
        JsonAdaptedRecipe recipe = new JsonAdaptedRecipe(VALID_NAME, null, VALID_STEPS);
        String expectedMessage = String.format(RECIPE_MISSING_FIELD_MESSAGE_FORMAT,
            IngredientReference.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

    @Test
    public void toModelType_nullStep_throwsIllegalValueException() {
        JsonAdaptedRecipe recipe = new JsonAdaptedRecipe(VALID_NAME, VALID_REFS, null);
        String expectedMessage = String.format(RECIPE_MISSING_FIELD_MESSAGE_FORMAT, Step.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, recipe::toModelType);
    }

}
