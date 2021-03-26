package chopchop.storage;

import static chopchop.storage.JsonAdaptedIngredient.INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static chopchop.testutil.TypicalIngredients.BANANA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Tag;
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
        var ingredient = new JsonAdaptedIngredient(INVALID_NAME, getValidJsonIngredientSet(), new ArrayList<>());

        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedIngredient ingredient = new JsonAdaptedIngredient(null, getValidJsonIngredientSet(),
                new ArrayList<>());

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_invalidQuantity_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(INVALID_QTY, VALID_EXPIRY))), new ArrayList<>()
        );

        var expectedMessage = Quantity.parse(INVALID_QTY).getError();
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullQuantity_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(null, VALID_EXPIRY))), new ArrayList<>()
        );

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, Quantity.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_invalidExpiry_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(VALID_QTY, INVALID_EXPIRY))), new ArrayList<>()
        );

        var expectedMessage = ExpiryDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullExpiry_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(VALID_QTY, null))), new ArrayList<>()
        );

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT,
            ExpiryDate.class.getSimpleName());

        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }


    @Test
    public void toModelType_nullSets_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME, null, new ArrayList<>());

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, "sets");
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }

    @Test
    public void toModelType_nullTags_throwsIllegalValueException() {
        var ingredient = new JsonAdaptedIngredient(VALID_NAME,
            new JsonAdaptedIngredientSet(Arrays.asList(
                new JsonAdaptedIngredientSet.JsonAdaptedPair(VALID_QTY, null))), null);

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, Tag.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, ingredient::toModelType);
    }


    @Test
    public void toModelType_nullSets2_throwsIllegalValueException() {
        var sets = new JsonAdaptedIngredientSet((List<JsonAdaptedIngredientSet.JsonAdaptedPair>) null);

        var expectedMessage = String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, "sets");
        assertThrows(IllegalValueException.class, expectedMessage, sets::toModelType);
    }

    @Test
    public void toModelType_emptyDate_throwsIllegalValueException() {
        var pair = new JsonAdaptedIngredientSet.JsonAdaptedPair("300ml", "");
        try {
            assertEquals(true, pair.toModelType().fst().isEmpty());
        } catch (IllegalValueException e) {
            assertEquals(true, false);
        }
    }


    @Test
    public void test_ingredientRefs() {
        var m = JsonAdaptedIngredientReference.INGREDIENT_REFERENCE_MISSING_FIELD_MESSAGE_FORMAT;
        assertThrows(IllegalValueException.class, String.format(m, "name"), () -> {
            new JsonAdaptedIngredientReference(null, "asdf").toModelType();
        });

        assertThrows(IllegalValueException.class, String.format(m, "quantity"), () -> {
            new JsonAdaptedIngredientReference("asdf", null).toModelType();
        });

        assertThrows(IllegalValueException.class, "Unknown unit 'asdf' (from '300asdf')", () -> {
            new JsonAdaptedIngredientReference("asdf", "300asdf").toModelType();
        });
    }
}
