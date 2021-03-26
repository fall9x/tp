package chopchop.model.attributes;

import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTag_throwsIllegalArgumentException() {
        String invalidTag = "  ";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTag));
    }

    @Test
    public void isValidTag() {
        // null tag
        assertThrows(NullPointerException.class, () -> Tag.isValidTag(null));

        // invalid tag name
        assertFalse(Tag.isValidTag(""));  // empty string
        assertFalse(Tag.isValidTag("  "));  // spaces only

        // valid tag name
        assertTrue(Tag.isValidTag("snacks123"));  // contains alphanumeric characters with no spaces
        assertTrue(Tag.isValidTag("family dinner"));  // contains alphanumeric characters with spaces
        assertTrue(Tag.isValidTag("Super"));  // contains capital letters
        assertTrue(Tag.isValidTag("#favourite"));  // contains non-alphanumeric characters
        assertTrue(Tag.isValidTag("#All time favourites *Top 5"));  // long tag name
    }

    @Test
    public void caseInsensitiveComparison() {
        assertEquals(new Tag("chopchop"), new Tag("cHopChOP"));
        assertEquals(new Tag("chopchop"), new Tag("CHOPCHOP"));
        assertEquals(new Tag("tag"), "TAG");

        var t = new Tag("t");
        assertEquals(t, t);

        assertNotEquals(new Tag("tag"), "owo");
        assertNotEquals(new Tag("3"), 3);
    }
}
