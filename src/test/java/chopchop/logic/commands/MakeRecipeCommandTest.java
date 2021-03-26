// MakeRecipeCommandTest.java

package chopchop.logic.commands;

import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.CommandParser;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Mass;
import chopchop.testutil.StubbedModel;
import chopchop.testutil.TypicalIngredients;
import chopchop.testutil.TypicalRecipes;
import org.junit.jupiter.api.Test;

import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_CUSTARD;
import static chopchop.logic.commands.CommandTestUtil.VALID_RECIPE_NAME_APRICOT_SALAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MakeRecipeCommandTest {

    private CommandResult runCommand(Model m, String str) {
        var parser = new CommandParser();
        var c = parser.parse(str);

        if (c.isError()) {
            assertEquals(1, c.getError());
        }

        assertTrue(c.hasValue());
        return c.getValue().execute(m, new HistoryManager());
    }

    private CommandResult runCommand(Command c, Model m) {
        return c.execute(m, new HistoryManager());
    }

    @Test
    void test() {
        var m = StubbedModel.filled();
        runCommand(m, "add ingredient sprinkles /qty 400g");
        runCommand(m, "add ingredient rainbows /qty 400g");
        runCommand(m, "add recipe uwu salad /ingredient sprinkles /qty 50g /step sprinkle it");
        runCommand(m, "add recipe asdf salad /ingredient rainbows /qty 50ml /step uwu");
        runCommand(m, "add recipe rainbow salad /ingredient rainbows /qty 400g /step uwu");
        runCommand(m, "delete ingredient sprinkles");

        var c1 = runCommand(m, "make recipe apricot salad");
        assertTrue(c1.didSucceed());

        var c2 = runCommand(m, "make recipe owo salad");
        assertTrue(c2.isError());
        assertEquals("Error: No recipe named 'owo salad'", c2.toString());

        var c3 = runCommand(m, "make recipe uwu salad");
        assertTrue(c3.isError());
        assertEquals("Error: Missing ingredient 'sprinkles' (not found)", c3.toString());

        var c4 = runCommand(m, "make recipe asdf salad");
        assertTrue(c4.isError());
        assertEquals("Error: Could not make recipe: Cannot compare '50mL' with '400g' "
            + "(incompatible units) (for ingredient 'rainbows')", c4.toString());

        {
            var p = new CommandParser();
            var c = p.parse("make recipe rainbow salad").getValue();

            var r1 = runCommand(c, m);
            assertTrue(r1.didSucceed());

            var u1 = ((MakeRecipeCommand) c).undo(m);
            assertTrue(u1.didSucceed());

            assertTrue(m.findIngredientWithName("rainbows").get().getQuantity().equals(Mass.grams(400)));
        }

        {
            var p = new CommandParser();
            var c = p.parse("make recipe apricot salad").getValue();

            var r1 = runCommand(c, m);
            assertTrue(r1.didSucceed());

            var r2 = ((MakeRecipeCommand) c).undo(m);
            assertTrue(r2.didSucceed());
        }
    }

    @Test
    void test_usages() {
        var m = new ModelManager();
        m.addRecipe(TypicalRecipes.APRICOT_SALAD);
        m.addIngredient(TypicalIngredients.APRICOT);
        m.addIngredient(TypicalIngredients.CUSTARD);
        runCommand(m, "make recipe apricot salad");
        assertEquals(m.getRecipeUsageList().getUsageList().get(0).getName(), VALID_RECIPE_NAME_APRICOT_SALAD);
        assertEquals(m.getIngredientUsageList().getUsageList().get(0).getName(), VALID_INGREDIENT_NAME_APRICOT);
        assertEquals(m.getIngredientUsageList().getUsageList().get(0).getQty(),         Count.of(3));
        assertEquals(m.getIngredientUsageList().getUsageList().get(1).getName(), VALID_INGREDIENT_NAME_CUSTARD);
        assertEquals(m.getIngredientUsageList().getUsageList().get(1).getQty(),         Count.of(3));
    }
}
