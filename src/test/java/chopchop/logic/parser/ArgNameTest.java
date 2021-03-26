// ArgNameTest.java

package chopchop.logic.parser;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;

public class ArgNameTest {

    @Test
    void test_argName() {
        // check that this stuff throws
        assertThrows(NullPointerException.class, () -> new ArgName(null));
        assertThrows(IllegalArgumentException.class, () -> new ArgName(""));
        assertThrows(IllegalArgumentException.class, () -> new ArgName("/foo"));

        assertEquals(new ArgName("foo").name(), "foo");
        assertEquals(new ArgName("foo").toString(), "/foo");

        assertNotEquals(new ArgName("foo"), "foo");

        assertEquals(new ArgName("foo"), new ArgName("foo"));
        assertNotEquals(new ArgName("FOO"), new ArgName("foo"));

        assertEquals(new ArgName("foo").hashCode(), new ArgName("foo").hashCode());
        assertNotEquals(new ArgName("FOO").hashCode(), new ArgName("foo").hashCode());

        assertEquals(new ArgName("step:").name(), "step");
        assertEquals(new ArgName("step:add:1").name(), "step");
        assertEquals(new ArgName("step:add:1").getComponents(), List.of("add", "1"));
        assertEquals(new ArgName("step:edit:30:40:50").getComponents(), List.of("edit", "30", "40", "50"));

        assertEquals(new ArgName("step:add:1"), new ArgName("step:add:1"));
        assertNotEquals(new ArgName("step:add:1"), new ArgName("step:add:2"));
    }
}
