// CommandParserTest.java

package chopchop.logic.parser;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandParserTest {

    @Test
    void parse_validCommands_success() {

        var parser = new CommandParser();

        var tests = Map.of(
            "add ingredient squid /qty 30g /expiry 2020-12-24",
            "Result(AddIngredientCommand: squid (30g) expires: 2020-12-24)",

            "add ingredient milk /qty 600ml",
            "Result(AddIngredientCommand: milk (600ml))",

            "add recipe cake /ingredient milk /qty 400ml /ingredient flour /qty 500g "
                + "/ingredient egg /qty 7 /step mix /step bake /step eat",
            "Result(AddRecipeCommand: cake Ingredients: milk (400ml), flour (500g), "
                + "egg (7) Steps: 1. mix, 2. bake, 3. eat)"
        );

        tests.forEach((k, v) -> {
            var x = parser.parse(k);

            System.err.println(x);
            assertEquals(v, x.toString());
        });
    }
}
