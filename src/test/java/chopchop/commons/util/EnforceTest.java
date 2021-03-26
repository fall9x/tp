// EnforceTest.java

package chopchop.commons.util;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.commons.util.Enforce.enforce;
import static chopchop.commons.util.Enforce.enforceAll;
import static chopchop.commons.util.Enforce.enforceAny;
import static chopchop.commons.util.Enforce.enforceContains;
import static chopchop.commons.util.Enforce.enforceEmpty;
import static chopchop.commons.util.Enforce.enforceEqual;
import static chopchop.commons.util.Enforce.enforceGreaterThan;
import static chopchop.commons.util.Enforce.enforceLessThan;
import static chopchop.commons.util.Enforce.enforceNonNull;
import static chopchop.commons.util.Enforce.enforceNotEmpty;
import static chopchop.commons.util.Enforce.enforcePresent;

public class EnforceTest {

    @Test
    public void test() {
        new Enforce();

        {
            Enforce.disableEnforce();
            enforce(false);
            Enforce.enableEnforce();
        }

        enforce(true);
        enforceNonNull(1, 2, 3);
        enforceAll(1 == 1, 3 == 3, true);
        enforceAny();
        enforceAny(true);
        enforceAny(false, false, false, false, true);
        enforceContains(1, List.of(1, 2, 3, 4));
        enforceContains("a", "a", "b", "c");
        enforceNotEmpty(List.of(1, 2, 3));
        enforcePresent(Optional.of("7"));
        enforceLessThan(1, 3);
        enforceEqual(3, 3);
        enforceGreaterThan(3, 1);
        enforceEmpty(List.of());
        enforceEmpty(Optional.empty());

        assertThrows(AssertionError.class, () -> enforce(false));
        assertThrows(AssertionError.class, () -> enforceNonNull(1, 2, null, "3"));
        assertThrows(AssertionError.class, () -> enforceAll(true, true, false, true, true));
        assertThrows(AssertionError.class, () -> enforceAny(false));
        assertThrows(AssertionError.class, () -> enforceAny(false, false, false, false));
        assertThrows(AssertionError.class, () -> enforceContains(1, 2, 3, 4));
        assertThrows(AssertionError.class, () -> enforceContains(1, List.of(2, 3, 4)));
        assertThrows(AssertionError.class, () -> enforceNotEmpty(List.of()));
        assertThrows(AssertionError.class, () -> enforcePresent(Optional.empty()));
        assertThrows(AssertionError.class, () -> enforceLessThan(3, 1));
        assertThrows(AssertionError.class, () -> enforceEqual(1, 3));
        assertThrows(AssertionError.class, () -> enforceGreaterThan(1, 3));
        assertThrows(AssertionError.class, () -> enforceEmpty(List.of(1)));
        assertThrows(AssertionError.class, () -> enforceEmpty(Optional.of(1)));
    }
}
