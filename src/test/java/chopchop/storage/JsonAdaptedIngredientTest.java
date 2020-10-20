package chopchop.storage;

import static chopchop.storage.JsonAdaptedIngredient.INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static chopchop.testutil.TypicalIngredients.BANANA;

import java.util.Arrays;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Quantity;
import chopchop.model.ingredient.Ingredient;

public class JsonAdaptedIngredientTest {
    private static final String INVALID_NAME = "";
    private static final String INVALID_EXPIRY = "20-10-20";
    private static final String INVALID_QTY = "";

    private static final String VALID_NAME = BANANA.getName();
    private static final String VALID_EXPIRY = BANANA.getExpiryDate().get().toString();
    private static final String VALID_QTY = BANANA.getQuantity().toString();

    private JsonAdaptedIngredientSet getValidJsonIngredientSet() {
        var map = new TreeMap<Optional<ExpiryDate>, Quantity>(Ingredient.SET_COMPARATOR);
        map.put(BANANA.getExpiryDate(), BANANA.getQuantity());

        return new JsonAdaptedIngredientSet(map);
    }

    @Test
    public void toModelType_validIngredientDetails_returnsIngredient() throws Exception {
        var ingredient = new JsonAdaptedIngredient(BANANA);
        assertEquals(BANANA, ingredient.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(INVALID_NAME, getValidJsonIngredientSet());

        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient = new JsonAdaptedIngredient(null, getValidJsonIngredientSet());

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_invalidQuantity_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(INVALID_QTY, VALID_EXPIRY)))
            );

        var expectedMessage = Quantity.parse(INVALID_QTY).getError();
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullQuantity_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(null, VALID_EXPIRY)))
            );

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, Quantity.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_invalidExpiry_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(VALID_QTY, INVALID_EXPIRY)))
            );

        var expectedMessage = ExpiryDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullExpiry_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(VALID_QTY, null)))
            );

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT,
            ExpiryDate.class.getSimpleName());

        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }
}
