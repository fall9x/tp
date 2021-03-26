// EitherTest.java

package chopchop.commons.util;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;

public class EitherTest {

    private final Either<String, Integer> l1 = Either.left("foo");
    private final Either<String, Integer> l2 = Either.left("foofoo");

    private final Either<String, Integer> r1 = Either.right(123);
    private final Either<String, Integer> r2 = Either.right(246);


    @Test
    public void test_values() {

        assertTrue(l1.isLeft());
        assertFalse(l1.isRight());

        assertTrue(r1.isRight());
        assertFalse(r1.isLeft());

        assertEquals("foo", l1.fromLeft());
        assertEquals(123, r1.fromRight());

        assertThrows(NoSuchElementException.class, () -> l1.fromRight());
        assertThrows(NoSuchElementException.class, () -> r1.fromLeft());

        assertTrue(l1.fromLeftOpt().isPresent());
        assertTrue(l1.fromRightOpt().isEmpty());

        assertTrue(r1.fromRightOpt().isPresent());
        assertTrue(r1.fromLeftOpt().isEmpty());

        assertThrows(AssertionError.class, () -> new Either<>("asdf", "bsdf"));

        // this is a bit hacky, but just make a new Strings() class owo
        var s = new Strings();
    }

    @Test
    public void test_mapping() {
        assertEquals(l2, l1.mapLeft(x -> x + x));
        assertEquals(r2, r1.mapRight(x -> x + x));

        assertEquals(l1, l1.mapRight(x -> x + x));
        assertEquals(r1, r1.mapLeft(x -> x + x));
    }

    @Test
    public void test_equals() {

        assertNotEquals(l1, "foo");
        assertEquals(l1, l1);
        assertEquals(l2, l1.mapLeft(x -> x + x));
        assertEquals(r2, r1.mapRight(x -> x + x));

        var n1 = Either.left(null);
        var n2 = Either.right(null);

        assertEquals(n1, n2);

        assertNotEquals(n1, l1);
        assertNotEquals(n1, r1);
    }

    @Test
    public void test_toString() {
        assertEquals("Left(foo)", l1.toString());
        assertEquals("Right(123)", r1.toString());
    }
}
