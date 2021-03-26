package chopchop.storage;

import static chopchop.storage.JsonAdaptedIngredientUsage.USAGE_MISSING_FIELD_MESSAGE_FORMAT;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.units.Volume;
import chopchop.model.usage.IngredientUsage;

public class JsonAdaptedIngredientUsageTest {

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        var usage = new JsonAdaptedIngredientUsage(null, "date", "quantity");

        var expectedMessage = String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "name");
        assertThrows(IllegalValueException.class, expectedMessage, usage::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        var usage = new JsonAdaptedIngredientUsage("name", null, "quantity");

        var expectedMessage = String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "date");
        assertThrows(IllegalValueException.class, expectedMessage, usage::toModelType);
    }

    @Test
    public void toModelType_nullQuantity_throwsIllegalValueException() {
        var usage = new JsonAdaptedIngredientUsage("name", "date", null);

        var expectedMessage = String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "quantity");
        assertThrows(IllegalValueException.class, expectedMessage, usage::toModelType);
    }

    @Test
    public void toModelType_invalidQuantity_throwsIllegalValueException() {
        var usage = new JsonAdaptedIngredientUsage("name", "date", "asdf");

        assertThrows(IllegalValueException.class, "Couldn't parse number from quantity 'asdf': empty String",
            usage::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {

        assertThrows(DateTimeParseException.class, "Text 'date' could not be parsed at index 0", () -> {
            new JsonAdaptedIngredientUsage("name", "date", "300ml").toModelType();
        });
    }

    @Test
    public void toModelType_valid_success() {
        try {
            var usage = new JsonAdaptedIngredientUsage("name", "2020-01-01T21:28:32.624831", "300ml");
            usage.toModelType();
        } catch (IllegalValueException e) {
            assertEquals(false, true);
        }
    }


    @Test
    public void test_backAndForth() {
        try {
            var u1 = new JsonAdaptedIngredientUsage("name", "2020-01-01T21:28:32.624831", "300ml")
                .toModelType();

            var u2 = new IngredientUsage("name", LocalDateTime.parse("2020-01-01T21:28:32.624831"),
                Volume.millilitres(300));

            var u3 = new JsonAdaptedIngredientUsage(u2).toModelType();

            assertEquals(u1, u2);
            assertEquals(u1, u3);
            assertEquals(u2, u3);

        } catch (IllegalValueException e) {
            assertEquals(false, true);
        }
    }
}
