// AddCommandParserTest.java

package chopchop.logic.parser.commands;

import java.util.HashMap;

import chopchop.logic.parser.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCommandParserTest {

    @Test
    void test_parse() {
        var cases = new HashMap<String, Boolean>();
        var parser = new CommandParser();

        cases.put("add recipe",                                                         false);
        cases.put("add recipe f /name",                                                 false);
        cases.put("add recipe f /ingredient:add",                                       false);
        cases.put("add recipe f /ingredient /qty 5",                                    false);
        cases.put("add recipe f /ingredient x /ingredient",                             false);
        cases.put("add recipe f /ingredient x /ingredient y /qty zzz",                  false);
        cases.put("add recipe f /qty 700",                                              false);
        cases.put("add recipe f /qty -700",                                             false);
        cases.put("add recipe #123",                                                    false);

        cases.put("add ingredient",                                                     false);
        cases.put("add ingredient f /name",                                             false);
        cases.put("add ingredient f /qty:add",                                          false);
        cases.put("add ingredient f /qty",                                              false);
        cases.put("add ingredient f /qty /qty",                                         false);
        cases.put("add ingredient f /qty 1 /qty 2",                                     false);
        cases.put("add ingredient f /qty x",                                            false);
        cases.put("add ingredient f /qty -1",                                           false);
        cases.put("add ingredient f /qty x y /qty zzz",                                 false);
        cases.put("add ingredient f /expiry",                                           false);
        cases.put("add ingredient f /expiry /expiry",                                   false);

        cases.put("add recipe #123 a",                                                  true);
        cases.put("add recipe #123 123",                                                true);

        cases.forEach((k, v) -> {
            assertEquals(v, parser.parse(k).hasValue());
        });
    }
}
