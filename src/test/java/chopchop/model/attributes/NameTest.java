package chopchop.model.attributes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;
import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only

        // valid name
        assertTrue(Name.isValidName("corn*")); // contains non-alphanumeric characters
        assertTrue(Name.isValidName("egg plant")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("eggplant the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Corn Starch")); // with capital letters
        assertTrue(Name.isValidName("The Great 2nd Corn Starch of the U.K.")); // long names

        assertFalse(Name.isValidName("  asdf  "));
    }

    @Test
    public void caseInsensitiveComparison() {

        assertEquals(new Name("aaaaa"), new Name("aAaAa"));
        assertEquals(new Name("aaaaa"), "AAAAA");

        assertEquals(new Name("chopchop"), new Name("cHopChOP"));
        assertEquals(new Name("chopchop"), new Name("CHOPCHOP"));
        assertEquals(new Name("name"), "name");

        var t = new Name("t");
        assertEquals(t, t);

        assertNotEquals(new Name("name"), "owo");
        assertNotEquals(new Name("3"), 3);
    }
}
