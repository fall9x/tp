package chopchop.storage;

import static chopchop.storage.JsonAdaptedIngredient.IND_MISSING_FIELD_MESSAGE_FORMAT;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static chopchop.testutil.TypicalIngredients.BANANA;

import org.junit.jupiter.api.Test;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Quantity;

public class JsonAdaptedIngredientTest {
    private static final String INVALID_NAME = "";
    private static final String INVALID_EXPIRY = "20-10-20";
    private static final String INVALID_QTY = "";

    private static final String VALID_NAME = BANANA.getName().toString();
    private static final String VALID_EXPIRY = BANANA.getExpiryDate().get().toString();
    private static final String VALID_QTY = BANANA.getQuantity().toString();

    @Test
    public void toModelType_validIngredientDetails_returnsIngredient() throws Exception {
        JsonAdaptedIngredient ingredient = new JsonAdaptedIngredient(BANANA);
        assertEquals(BANANA, ingredient.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient =
            new JsonAdaptedIngredient(INVALID_NAME, VALID_QTY, VALID_EXPIRY);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient = new JsonAdaptedIngredient(null, VALID_QTY, VALID_EXPIRY);
        String expectedMessage = String.format(IND_MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_invalidQuantity_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient =
            new JsonAdaptedIngredient(VALID_NAME, INVALID_QTY, VALID_EXPIRY);
        String expectedMessage = Quantity.parse(INVALID_QTY).getError();
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullQuantity_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient = new JsonAdaptedIngredient(VALID_NAME, null, VALID_EXPIRY);
        String expectedMessage = String.format(IND_MISSING_FIELD_MESSAGE_FORMAT, Quantity.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient =
            new JsonAdaptedIngredient(VALID_NAME, VALID_QTY, INVALID_EXPIRY);
        String expectedMessage = ExpiryDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient = new JsonAdaptedIngredient(VALID_NAME, VALID_QTY, null);
        String expectedMessage = String.format(IND_MISSING_FIELD_MESSAGE_FORMAT, ExpiryDate.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

}
