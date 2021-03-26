package chopchop.storage;

import static chopchop.storage.JsonAdaptedRecipeUsage.USAGE_MISSING_FIELD_MESSAGE_FORMAT;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.usage.RecipeUsage;

public class JsonAdaptedRecipeUsageTest {

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        var usage = new JsonAdaptedRecipeUsage(null, "date");

        var expectedMessage = String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "name");
        assertThrows(IllegalValueException.class, expectedMessage, usage::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        var usage = new JsonAdaptedRecipeUsage("name", null);

        var expectedMessage = String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "date");
        assertThrows(IllegalValueException.class, expectedMessage, usage::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {

        assertThrows(DateTimeParseException.class, "Text 'date' could not be parsed at index 0", () -> {
            new JsonAdaptedRecipeUsage("name", "date").toModelType();
        });
    }

    @Test
    public void toModelType_valid_success() {
        try {
            var usage = new JsonAdaptedRecipeUsage("name", "2020-01-01T21:28:32.624831");
            usage.toModelType();
        } catch (IllegalValueException e) {
            assertEquals(false, true);
        }
    }


    @Test
    public void test_backAndForth() {
        try {
            var u1 = new JsonAdaptedRecipeUsage("name", "2020-01-01T21:28:32.624831").toModelType();
            var u2 = new RecipeUsage("name", LocalDateTime.parse("2020-01-01T21:28:32.624831"));
            var u3 = new JsonAdaptedRecipeUsage(u2).toModelType();

            assertEquals(u1, u2);
            assertEquals(u1, u3);
            assertEquals(u2, u3);

        } catch (IllegalValueException e) {
            assertEquals(false, true);
        }
    }
}
