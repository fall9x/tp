// EditCommandParserTest.java

package chopchop.logic.parser.commands;

import java.util.HashMap;

import chopchop.logic.parser.ArgName;
import chopchop.logic.parser.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditCommandParserTest {

    @Test
    void test_parse() {
        var cases = new HashMap<String, Boolean>();
        var parser = new CommandParser();

        cases.put("edit recipe",                                                        false);
        cases.put("edit ingredient",                                                    false);
        cases.put("edit recipe x /ingredient",                                          false);
        cases.put("edit recipe x /ingredient:owo",                                      false);
        cases.put("edit recipe x /ingredient:owo:uwu",                                  false);
        cases.put("edit recipe x /ingredient:add:add",                                  false);
        cases.put("edit recipe x /ingredient:add",                                      false);
        cases.put("edit recipe x /ingredient:edit y",                                   false);
        cases.put("edit recipe x /ingredient:add y",                                    false);
        cases.put("edit recipe x /ingredient:delete y /qty 6",                          false);
        cases.put("edit recipe x /ingredient:delete y /qty z",                          false);
        cases.put("edit recipe x /ingredient:delete y /qty -999",                       false);

        cases.put("edit recipe x /tag",                                                 false);
        cases.put("edit recipe x /tag:owo",                                             false);
        cases.put("edit recipe x /tag:owo:uwu",                                         false);
        cases.put("edit recipe x /tag:add:add",                                         false);
        cases.put("edit recipe x /tag:add",                                             false);

        cases.put("edit recipe x /step",                                                false);
        cases.put("edit recipe x /step:owo",                                            false);
        cases.put("edit recipe x /step:owo:uwu",                                        false);
        cases.put("edit recipe x /step:add",                                            false);
        cases.put("edit recipe x /step:add:zzz",                                        false);
        cases.put("edit recipe x /step:add:999",                                        false);
        cases.put("edit recipe x /step:add:-3",                                         false);
        cases.put("edit recipe x /step:add:3:3",                                        false);
        cases.put("edit recipe x /step:delete y",                                       false);
        cases.put("edit recipe x /step:delete: y",                                      false);
        cases.put("edit recipe x /step:edit y",                                         false);

        cases.put("edit recipe x /name",                                                false);
        cases.put("edit recipe x /name asdf /name uwu",                                 false);

        cases.put("edit recipe x /qty",                                                 false);
        cases.put("edit recipe x /expiry",                                              false);

        cases.put("edit recipe x /tag:add y",                                           true);
        cases.put("edit recipe x /tag:delete y",                                        true);
        cases.put("edit recipe x /step:add y",                                          true);
        cases.put("edit recipe x /step:edit:4 y",                                       true);
        cases.put("edit recipe x /ingredient:edit y /qty 6",                            true);
        cases.put("edit recipe x /ingredient:add y /qty 1",                             true);
        cases.put("edit recipe x /ingredient:delete y",                                 true);
        cases.put("edit recipe x /ingredient:delete y /step:add z",                     true);

        cases.put("edit ingredient x /tag:add y",                                       true);
        cases.put("edit ingredient x /tag:delete y",                                    true);


        cases.forEach((k, v) -> {
            System.out.printf("** testing: %s\n", k);
            assertEquals(v, parser.parse(k).hasValue());
        });


        try {
            var m = EditCommandParser.class.getDeclaredMethod("parseTagEdit", ArgName.class, String.class);
            m.setAccessible(true);

            m.invoke(null, new ArgName("tag:add"), "  owo  ");

            var m2 = EditCommandParser.class.getDeclaredMethod("ensureNoArgsForDeleteAndGetOperationType",
                String.class, String.class, boolean.class);
            m2.setAccessible(true);

            m2.invoke(null, "tag", "kekw", true);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
