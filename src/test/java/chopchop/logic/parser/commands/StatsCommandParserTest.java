// StatsCommandParserTest.java

package chopchop.logic.parser.commands;

import java.util.HashMap;

import chopchop.logic.parser.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsCommandParserTest {

    @Test
    void test_parse() {
        var cases = new HashMap<String, Boolean>();
        var parser = new CommandParser();

        cases.put("stats recipe",                                                       false);
        cases.put("stats recipe used",                                                  false);
        cases.put("stats recipe top /before never",                                     false);
        cases.put("stats recipe made /name owo",                                        false);
        cases.put("stats recipe made /after /after",                                    false);
        cases.put("stats recipe made /before /before",                                  false);
        cases.put("stats recipe made /after owo",                                       false);

        cases.put("stats ingredient",                                                   false);
        cases.put("stats ingredient used",                                              false);
        cases.put("stats ingredient top /before never",                                 false);
        cases.put("stats ingredient used /name owo",                                    false);
        cases.put("stats ingredient used /after /after",                                false);
        cases.put("stats ingredient used /before /before",                              false);
        cases.put("stats ingredient used /after owo",                                   false);

        cases.put("stats recipe top",                                                   true);
        cases.put("stats recipe made",                                                  true);
        cases.put("stats recipe clear",                                                 true);
        cases.put("stats recipe recent",                                                true);
        cases.put("stats recipe made /after 2019-01-01",                                true);
        cases.put("stats recipe made /after 2019-01-01 01:01",                          true);

        cases.put("stats ingredient used",                                              true);
        cases.put("stats ingredient clear",                                             true);
        cases.put("stats ingredient recent",                                            true);
        cases.put("stats ingredient used /after 2019-01-01",                            true);
        cases.put("stats ingredient used /after 2019-01-01 01:01",                      true);


        cases.forEach((k, v) -> {
            System.out.printf("%s\n", k);
            assertEquals(v, parser.parse(k).hasValue());
        });
    }
}
