package chopchop.model.usage;

import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_A;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_B;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RecipeUsageTest {

    @Test
    void getName() {
        var u = new RecipeUsage("A", USAGE_DATE_A);
        assertEquals("A", u.getName());
    }

    @Test
    void getDate() {
        var u = new RecipeUsage("A", USAGE_DATE_A);
        assertEquals(USAGE_DATE_A, u.getDate());
    }

    @Test
    void getPrintableDate() {
        var u = new RecipeUsage("A", USAGE_DATE_A);
        assertEquals("0001-01-01 01:01", u.getPrintableDate());
    }

    @Test
    void isAfter() {
        var u = new RecipeUsage("A", USAGE_DATE_A);
        var u1 = new RecipeUsage("A", USAGE_DATE_B);
        assertFalse(u.isAfter(u1.getDate()));
        assertTrue(u1.isAfter(u.getDate()));
        assertFalse(u1.isAfter(u1.getDate()));
    }

    @Test
    void isBefore() {
        var u = new RecipeUsage("A", USAGE_DATE_A);
        var u1 = new RecipeUsage("A", USAGE_DATE_B);
        assertFalse(u1.isBefore(u.getDate()));
        assertTrue(u.isBefore(u1.getDate()));
        assertFalse(u1.isBefore(u1.getDate()));
    }
}
