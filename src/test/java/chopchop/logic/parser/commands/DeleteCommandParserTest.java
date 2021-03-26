// DeleteCommandParserTest.java

package chopchop.logic.parser.commands;

import java.util.HashMap;

import chopchop.logic.parser.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCommandParserTest {

    @Test
    void test_parse() {
        var cases = new HashMap<String, Boolean>();
        var parser = new CommandParser();

        cases.put("delete recipe",                                                      false);
        cases.put("delete recipe f /name",                                              false);
        cases.put("delete recipe f /ingredient:delete",                                 false);

        cases.put("delete ingredient",                                                  false);
        cases.put("delete ingredient f /name",                                          false);
        cases.put("delete ingredient f /ingredient:delete",                             false);
        cases.put("delete ingredient f /qty",                                           false);
        cases.put("delete ingredient f /qty /qty",                                      false);

        cases.forEach((k, v) -> {
            assertEquals(v, parser.parse(k).hasValue());
        });
    }
}
