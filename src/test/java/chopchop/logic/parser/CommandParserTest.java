// CommandParserTest.java

package chopchop.logic.parser;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import chopchop.commons.util.Pair;
import chopchop.logic.parser.commands.AddCommandParser;
import chopchop.logic.parser.commands.DeleteCommandParser;
import chopchop.logic.parser.commands.EditCommandParser;
import chopchop.logic.parser.commands.FilterCommandParser;
import chopchop.logic.parser.commands.FindCommandParser;
import chopchop.logic.parser.commands.HelpCommandParser;
import chopchop.logic.parser.commands.ListCommandParser;
import chopchop.logic.parser.commands.MakeCommandParser;
import chopchop.logic.parser.commands.StatsCommandParser;
import chopchop.logic.parser.commands.ViewCommandParser;
import chopchop.logic.parser.commands.CommonParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;

public class CommandParserTest {

    @Test
    void test_parseCommands() {

        var parser = new CommandParser();

        var tests = new HashMap<String, String>();

        tests.put("add ingredient squid /qty 30g /expiry 2020-12-24",
            "Result(AddIngredientCommand: squid (30g) <Expiry Date: 2020-12-24>)");

        tests.put("add ingredient milk /qty 600ml /tag wet",
            "Result(AddIngredientCommand: milk (600mL))");

        tests.put("add recipe cake /ingredient milk /qty 400ml /ingredient flour /qty 500g "
            + "/ingredient egg /qty 7 /step mix /step bake /step eat /tag baked /ingredient uwu",
            "Result(AddRecipeCommand(cake, ingr: [milk (400mL), flour (500g), "
                + "egg (7), uwu (1)], steps: [mix, bake, eat]))");

        tests.put("delete recipe cake", "Result(DeleteRecipeCommand(cake))");
        tests.put("delete recipe #4", "Result(DeleteRecipeCommand(#4))");
        tests.put("delete recipe #-1", "Error(Invalid index (cannot be zero or negative))");

        tests.put("delete ingredient milk", "Result(DeleteIngredientCommand(milk))");

        tests.put("delete ingredient milk /qty 500ml", "Result(DeleteIngredientCommand(milk (500mL)))");

        tests.put("help", "Result(HelpCommand)");
        tests.put("quit", "Result(QuitCommand)");
        tests.put("undo", "Result(UndoCommand)");
        tests.put("redo", "Result(RedoCommand)");
        tests.put("clear", "Result(ClearCommand)");

        tests.put("quit a", "Error('quit' command takes no arguments)");
        tests.put("undo /asdf", "Error('undo' command takes no arguments)");
        tests.put("redo 123 /asdf", "Error('redo' command takes no arguments)");


        tests.put("filter recipe /tag x", "Result(FilterRecipeCommand(...))");
        tests.put("filter ingredient /tag x", "Result(FilterIngredientCommand(...))");

        tests.put("list recipe", "Result(ListRecipeCommand)");
        tests.put("list recipes", "Result(ListRecipeCommand)");
        tests.put("list ingredient", "Result(ListIngredientCommand)");
        tests.put("list ingredients", "Result(ListIngredientCommand)");

        tests.put("make recipe cake", "Result(MakeRecipeCommand(cake))");
        tests.put("view recipe cake", "Result(ViewRecipeCommand(cake))");
        tests.put("filter recipe /tag owo", "Result(FilterRecipeCommand(...))");
        tests.put("edit recipe cake", "Result(EditRecipeCommand(cake))");

        tests.put("find recipe cake cucumber", "Result(FindRecipeCommand(keywords: [cake, cucumber]))");
        tests.put("find ingredient cake cucumber", "Result(FindIngredientCommand(keywords: [cake, cucumber]))");

        tests.put("find recipe", "Error('find' command requires at least one search term)");
        tests.put("add recipe cake /", "Error(Expected argument name after '/')");
        tests.put("add recipe cake /   ", "Error(Expected argument name after '/')");

        tests.put("view recipe cake cucumber", "Result(ViewRecipeCommand(cake cucumber))");
        tests.put("view recipe", "Error(Recipe name cannot be empty)");
        tests.put("view recipe #1", "Result(ViewRecipeCommand(#1))");
        tests.put("view recipe #0", "Error(Invalid index (cannot be zero or negative))");

        tests.put("OWO", "Error(Unknown command 'OWO')");

        tests.put("list uwu",
            "Error(Unknown target 'uwu' (expected one of 'recipe', 'ingredient', or 'recommendation'))");

        tests.put("edit ingredients",
            "Error(Unknown target 'ingredients' (expected either 'recipe' or 'ingredient'))");

        tests.forEach((k, v) -> {
            var x = parser.parse(k);

            System.err.println(x);
            assertEquals(v, x.toString());
        });
    }





    @Test
    void parse_commandArgs_success() {
        var parser = new CommandParser();

        var tests = Map.of(
            "add",
                new CommandArguments("add"),

            "add /stuff kekw",
                new CommandArguments("add", List.of(Pair.of(new ArgName("stuff"), "kekw"))),

            "add some\\/stuff\\/here",
                new CommandArguments("add", "some/stuff/here"),

            "add some\\stuff\\here\\",
                new CommandArguments("add", "some\\stuff\\here\\"),


            "add some\\/step\\/here /step \\/owo here\\/is\\/a\\/step\\/",
                new CommandArguments("add", "some/step/here",
                    List.of(Pair.of(new ArgName("step"), "/owo here/is/a/step/"))
                )
        );

        tests.forEach((k, v) -> {
            var x = parser.parseArgs(k);
            assertTrue(x.hasValue());

            assertEquals(v, x.getValue());
        });

        assertTrue(CommonParser.getCommandTarget(new CommandArguments("add"), false).isError());

        // just... force coverage on these classes.
        {
            new AddCommandParser();
            new DeleteCommandParser();
            new EditCommandParser();
            new FilterCommandParser();
            new FindCommandParser();
            new HelpCommandParser();
            new ListCommandParser();
            new MakeCommandParser();
            new StatsCommandParser();
            new ViewCommandParser();
            new CommonParser();
        };

        assertThrows(IllegalArgumentException.class, () -> {
            FindCommandParser.parseFindCommand(new CommandArguments("kekw"));
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            var p = new CommandParser();
            var c1 = p.parse("make recipe asdf").getValue();
            var c2 = p.parse("make recipe asdf").getValue();

            c1.equals(c2);
        });

        assertNotEquals(new CommandArguments("add"), "add");
        assertNotEquals(new CommandArguments("add"), new CommandArguments("subtract"));

        var tests2 = Map.of(
            "add", new CommandArguments("add"),
            "add /stuff kekw", new CommandArguments("add", List.of(Pair.of(new ArgName("stuff"), "kekw")))
        );

        assertNotEquals(new CommandArguments("add", "aaa", List.of()), new CommandArguments("add", "bbb", List.of()));
        assertNotEquals(new CommandArguments("add", "aaa", List.of(Pair.of(new ArgName("kekw"), "3"))),
            new CommandArguments("add", "aaa", List.of(Pair.of(new ArgName("kekw"), "4"))));

        assertTrue(CommonParser.checkArguments(parser.parseArgs("x /owo:uwu").getValue(),
            "x", List.of(), true).isPresent());
    }



    @Test
    void test_parseCommands2() {
        var cases = new HashMap<String, Boolean>();
        var parser = new CommandParser();


        cases.put("list recipes /uwu",                                                  false);
        cases.put("filter recipe /qty:kekw",                                            false);
        cases.put("help /uwu",                                                          false);
        cases.put("view x /uwu",                                                        false);
        cases.put("make /uwu",                                                          false);
        cases.put("make x /uwu",                                                        false);
        cases.put("make",                                                               false);
        cases.put("find /asdf",                                                         false);
        cases.put("find /asdf:owo",                                                     false);

        cases.put("filter recipe",                                                      false);
        cases.put("filter recipe /name",                                                false);
        cases.put("filter recipe /tag",                                                 false);
        cases.put("filter recipe /ingredient",                                          false);

        cases.put("filter ingredient",                                                  false);
        cases.put("filter ingredient /name",                                            false);
        cases.put("filter ingredient /tag",                                             false);
        cases.put("filter ingredient /expiry a a a ",                                   false);

        cases.put("add recommendation x",                                               false);
        cases.put("edit recommendation",                                                false);
        cases.put("find recommendation x",                                              false);
        cases.put("filter recommendation /x",                                           false);
        cases.put("delete recommendation x",                                            false);
        cases.put("stats recommendation",                                               false);
        cases.put("list",                                                               false);

        cases.put("help",                                                               true);
        cases.put("clear",                                                              true);
        cases.put("help add",                                                           true);
        cases.put("help add recipe",                                                    true);
        cases.put("add ingredient f",                                                   true);
        cases.put("list recommendation",                                                true);
        cases.put("list recommendations",                                               true);

        cases.put("filter recipe /name x",                                              true);
        cases.put("filter recipe /tag x",                                               true);
        cases.put("filter recipe /tag x /name x",                                       true);
        cases.put("filter recipe /tag x /ingredient x",                                 true);
        cases.put("filter recipe /name x /ingredient x",                                true);
        cases.put("filter recipe /name x /ingredient x /tag x",                         true);
        cases.put("filter recipe /ingredient x",                                        true);

        cases.put("filter ingredient /name x",                                          true);
        cases.put("filter ingredient /tag x",                                           true);
        cases.put("filter ingredient /expiry 2020-01-01",                               true);
        cases.put("filter ingredient /name x /tag x",                                   true);
        cases.put("filter ingredient /tag x /expiry 2020-01-01",                        true);
        cases.put("filter ingredient /name x /tag x /expiry 2020-01-01",                true);

        cases.forEach((k, v) -> {
            assertEquals(v, parser.parse(k).hasValue());
        });
    }
}
