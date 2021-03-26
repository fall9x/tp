// ClearCommandTest.java

package chopchop.logic.commands;

import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.CommandParser;
import chopchop.model.Model;
import chopchop.testutil.StubbedModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearCommandTest {

    private CommandResult runCommand(Model m, String str) {
        var parser = new CommandParser();
        var c = parser.parse(str);

        if (c.isError()) {
            assertEquals(1, c.getError());
        }

        assertTrue(c.hasValue());
        return c.getValue().execute(m, new HistoryManager());
    }

    @Test
    void test() {
        var m = StubbedModel.empty();
        runCommand(m, "add ingredient sprinkles /qty 400g");
        runCommand(m, "add ingredient rainbows /qty 400g");
        runCommand(m, "add recipe uwu salad /ingredient sprinkles /qty 50g /step sprinkle it");
        runCommand(m, "add recipe asdf salad /ingredient rainbows /qty 50ml /step uwu");
        runCommand(m, "add recipe rainbow salad /ingredient rainbows /qty 400g /step uwu");

        assertEquals(3, m.getRecipeBook().getEntryList().size());
        assertEquals(2, m.getIngredientBook().getEntryList().size());

        var c1 = (Undoable) new CommandParser().parse("clear").getValue();
        var r1 = c1.execute(m, new HistoryManager());

        assertTrue(r1.didSucceed());
        assertTrue(m.getRecipeBook().getEntryList().isEmpty());
        assertTrue(m.getIngredientBook().getEntryList().isEmpty());

        var r2 = c1.undo(m);
        assertTrue(r2.didSucceed());

        assertEquals(3, m.getRecipeBook().getEntryList().size());
        assertEquals(2, m.getIngredientBook().getEntryList().size());
    }
}
