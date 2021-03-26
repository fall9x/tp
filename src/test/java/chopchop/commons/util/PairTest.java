// PairTest.java

package chopchop.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PairTest {

    @Test
    public void test_pairs() {

        var p1 = new Pair<>("asdf", 1234);
        var p2 = Pair.of("asdf", 1234);

        assertEquals("asdf", p1.fst());
        assertEquals(1234, p1.snd());

        assertEquals("asdf", p2.fst());
        assertEquals(1234, p2.snd());

        var p3 = p1.map((a, b) -> Pair.of(a.toUpperCase(), b * b));
        var p4 = p2.map((a, b) -> Pair.of(a.toUpperCase(), b * b));

        assertEquals("ASDF", p3.fst());
        assertEquals(1522756, p3.snd());

        assertEquals("ASDF", p4.fst());
        assertEquals(1522756, p4.snd());

        assertEquals(p3, p4);
        assertEquals(p4, p3);
        assertEquals(p3, p3);
        assertEquals(p4, p4);

        var p5 = p1.mapFst(x -> x + x);
        var p6 = p1.mapSnd(x -> x + x);

        assertEquals("asdfasdf", p5.fst());
        assertEquals(2468, p6.snd());

        assertEquals("(asdf, 1234)", p1.toString());

        assertNotEquals(p1, "asdf");
        assertNotEquals(p3, p1);
        assertNotEquals(p5, p1);
        assertNotEquals(p6, p1);
    }
}
