// CommandResultTest.java

package chopchop.logic.commands;

import java.util.List;
import java.util.Optional;

import chopchop.commons.util.Pair;
import chopchop.testutil.TypicalRecipes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandResultTest {

    @Test
    public void test() {
        assertTrue(CommandResult.exit().shouldExit());
        assertTrue(CommandResult.message("owo").didSucceed());
        assertFalse(CommandResult.error("owo").didSucceed());

        assertFalse(CommandResult.message("uwu").isError());
        assertTrue(CommandResult.error("uwu").isError());

        assertEquals(1, CommandResult.message("asdf").getParts().size());

        assertEquals(1, CommandResult.exit().appending("asdf", false).getParts().size());
        assertEquals(1, CommandResult.exit().appendingLink("asdf", "", false).getParts().size());

        assertEquals("uwu owo", CommandResult.message("uwu").appendingLink("owo", "", true).toString());
        assertEquals("uwu owo", CommandResult.message("uwu").appendingLink("owo", "", false).toString());

        assertFalse(CommandResult.message("asdf").isStatsOutput());
        assertTrue(CommandResult.statsMessage(List.of(), "asdf").isStatsOutput());
        assertTrue(CommandResult.statsMessage(List.of(), "asdf").getStatsMessage().isEmpty());


    }

    @Test
    public void test_equals() {
        var c1 = CommandResult.message("asdf");
        var c2 = CommandResult.message("asdf");
        var c3 = CommandResult.message("uwu").appending("asdf", false);
        var c4 = CommandResult.error("asdf");
        var c5 = CommandResult.message("uwu").appendingLink("asdf", "owo.asdf", false);
        var c6 = CommandResult.exit().appending("asdf", false);

        assertEquals(c1, c1);
        assertEquals(c1, c2);

        assertNotEquals(c1, "asdf");
        assertNotEquals(c2, c4);
        assertNotEquals(c3, c5);
        assertNotEquals(c1, c6);

        var d4 = c1.showingRecipe(TypicalRecipes.APRICOT_SALAD);
        var d5 = c1.showingRecipeList();
        var d6 = c1.showingIngredientList();
        var d7 = c1.showingRecommendationList();
        var d8 = c1.showingRecipe(TypicalRecipes.CUSTARD_SALAD);

        assertNotEquals(c1, d4);
        assertNotEquals(c1, d5);
        assertNotEquals(c1, d6);
        assertNotEquals(c1, d7);
        assertNotEquals(d4, d8);

        assertEquals(c1.getSwitchedPanel(), CommandResult.Panel.NONE);
        assertEquals(d4.getSwitchedPanel(), CommandResult.Panel.RECIPE_DETAIL);
        assertEquals(d5.getSwitchedPanel(), CommandResult.Panel.RECIPE_LIST);
        assertEquals(d6.getSwitchedPanel(), CommandResult.Panel.INGREDIENT_LIST);
        assertEquals(d7.getSwitchedPanel(), CommandResult.Panel.RECOMMENDATION_LIST);

        assertEquals(d4.getDisplayedRecipe(), Optional.of(TypicalRecipes.APRICOT_SALAD));


        var e1 = CommandResult.statsMessage(List.of(Pair.of("A", "B")), "asdf");
        var e2 = CommandResult.statsMessage(List.of(Pair.of("C", "D")), "asdf");

        assertNotEquals(e1, e2);

        var f1 = c1.appending("asdf", false);
        var f2 = c1.appending("usdf", false);
        var f3 = c1.appending("asdf", false);

        assertEquals(f1, f3);
        assertNotEquals(f1, f2);
    }

    @Test
    public void test_parts() {
        var c = CommandResult
            .message("asdf")                    // 0
            .appending("asdf", false)           // 1
            .appending("aaa", false)            // 2
            .appending("aaa", false)            // 3
            .appending("aaa", false)            // 4
            .appending("aaa", true)             // 5
            .appendingLink("aaa", "x", false)   // 6
            .appendingLink("aaa", "y", false)   // 7
            .appendingLink("bbb", "x", false)   // 8
            .appendingLink("bbb", "x", false)   // 9
            .appendingLink("aaa", "", false);   // 10

        var parts = c.getParts();
        assertEquals(parts.get(0), parts.get(0));
        assertEquals(parts.get(0), parts.get(1));
        assertEquals(parts.get(2), parts.get(3));
        assertEquals(parts.get(8), parts.get(9));

        assertNotEquals(parts.get(3), parts.get(4));
        assertNotEquals(parts.get(6), parts.get(7));
        assertNotEquals(parts.get(6), parts.get(8));
        assertNotEquals(parts.get(5), parts.get(10));
        assertNotEquals(parts.get(0), "asdf");

        assertEquals("x", parts.get(6).getUrl());
        assertTrue(parts.get(6).isLink());

        assertTrue(parts.get(4).appendNewline());
        assertFalse(parts.get(5).appendNewline());
    }
}
