package chopchop.model.attributes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.testutil.Assert.assertThrows;
import org.junit.jupiter.api.Test;

public class ExpiryDateTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExpiryDate(null));
    }

    @Test
    public void constructor_invalidExpiryDate_throwsIllegalArgumentException() {
        String invalidDate = "";
        assertThrows(IllegalArgumentException.class, () -> new ExpiryDate(invalidDate));
    }

    @Test
    public void isValidDate() {
        // null name
        assertThrows(NullPointerException.class, () -> ExpiryDate.isValidDate(null));

        // invalid name
        assertFalse(ExpiryDate.isValidDate("")); // empty string
        assertFalse(ExpiryDate.isValidDate(" ")); // spaces only
        assertFalse(ExpiryDate.isValidDate("^")); // only non-alphanumeric characters
        assertFalse(ExpiryDate.isValidDate("2020:12-01")); // invalid date format
        assertFalse(ExpiryDate.isValidDate("2020-13-32")); // valid date format but invalid date

        // valid name
        assertTrue(ExpiryDate.isValidDate("1990-12-01")); // a date that has passed, yyyy-MM-dd
        assertTrue(ExpiryDate.isValidDate("2021-12-01")); // a future date, yyyy-MM-dd

    }
}
