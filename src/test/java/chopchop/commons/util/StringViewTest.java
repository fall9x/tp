// StringViewTest.java

package chopchop.commons.util;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;

public class StringViewTest {

    private final StringView sv1 = new StringView("abcdef ghi jklmno ");
    private final StringView sv2 = new StringView("owo uwu AYAYA");

    @Test
    public void test_stringViews() {

        assertEquals(3, sv1.find("def"));
        assertEquals(7, sv1.find(new StringView("ghi")));
        assertEquals(1, sv1.find('b'));

        assertTrue(sv2.startsWith("owo"));
        assertTrue(sv2.startsWith(new StringView("owo")));

        assertEquals('o', sv2.at(2));
        assertEquals('a', sv1.front());
        assertEquals('A', sv2.back());

        assertThrows(IndexOutOfBoundsException.class, () -> new StringView("").front());
        assertThrows(IndexOutOfBoundsException.class, () -> new StringView("").back());
        assertThrows(IndexOutOfBoundsException.class, () -> new StringView("3").drop(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> new StringView("3").takeLast(-1));

        assertEquals("owo", sv2.take(3).toString());
        assertEquals("AYAYA", sv2.takeLast(5).toString());

        assertEquals("owo uwu AYAYA", sv2.take(400).toString());
        assertEquals("owo uwu AYAYA", sv2.takeLast(400).toString());

        assertEquals("wo uwu AYAYA", sv2.drop(3).undrop(2).toString());

        var sv4 = new StringView("");
        var sv5 = new StringView("");
        assertEquals("asdf", new StringView("asdfxxxxxuwuwu").bisect('x').fst().toString());
        assertEquals("asdf", new StringView("asdfxxxxxuwuwu").bisect('x', sv4).toString());
        new StringView("asdfxxxxxuwuwu").bisect(sv4, 'x', sv5);

        assertEquals("asdf", sv4.toString());
        assertEquals("uwuwu", sv5.toString());



        assertEquals(new StringView("ayaya").uppercase().toString(), "AYAYA");
        assertEquals(new StringView("AYAYA").lowercase().toString(), "ayaya");

        assertTrue(new StringView("asdf").equals("asdf"));
        assertEquals(sv1, sv1);
        assertEquals(sv1, new StringView(sv1.toString()));
        assertNotEquals(sv1, 3);

        assertFalse(sv1.startsWith("owo"));
        assertFalse(sv2.startsWith(new StringView("kekw")));

        assertEquals("asdf".hashCode(), new StringView("asdf").hashCode());


        var sv3 = new StringView("AAA      BBB CCC\t\t\tDDD    \t  \t");
        assertEquals(List.of("owo", "uwu", "AYAYA"), sv2.words());
        assertEquals(List.of("AAA", "BBB", "CCC", "DDD"), sv3.words());
        assertEquals(List.of("AAA"), new StringView("AAA::::").splitBy(c -> c == ':'));
    }
}
