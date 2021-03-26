package chopchop.model.usage;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalUsages.Date.USAGE_DATE_A;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import chopchop.commons.util.Pair;
import chopchop.model.attributes.units.Mass;

class IngredientUsageTest {
    @Test
    void getListViewPair_returnsEqual() {
        var qty = Mass.grams(10);
        var iu = new IngredientUsage("A", USAGE_DATE_A, qty);
        assertEquals(new Pair<>("A", String.format("%s \nQty: %s", "0001-01-01 01:01", qty.toString())),
            iu.getListViewPair());
    }

    @Test
    void constructor_nullQty_nullPtrException() {
        assertThrows(NullPointerException.class, () -> new IngredientUsage("A", USAGE_DATE_A, null));
    }
}
