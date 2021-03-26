// LogTest.java

package chopchop.commons.core;

import org.junit.jupiter.api.Test;

public class LogTest {
    @Test
    public void test() {

        var l = new Log(LogTest.class);
        l.debug("asdf");
        l.log("uwu");
        l.warn("owo");
        l.error("monkaMEGA");
    }
}
