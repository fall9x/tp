// StreamUtilsTest.java

package chopchop.commons.util;

import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamUtilsTest {

    @Test
    public void test_streamUtils() {
        var s1 = Stream.iterate(0, i -> i + 1);
        var s2 = Stream.iterate("", x -> x + "a");

        var s3 = Stream.iterate(0, i -> i + 1);
        var s4 = Stream.iterate("", x -> x + "a");
        var s5 = Stream.iterate("", x -> x + "a");

        var z1 = StreamUtils.zip(s1, s2).map(p -> p.fst() == p.snd().length());
        assertTrue(z1.limit(10).allMatch(x -> x));

        assertEquals(StreamUtils.zip(s3, s4).limit(10).collect(Collectors.toList()),
            StreamUtils.indexed(s5).limit(10).collect(Collectors.toList()));
    }
}
