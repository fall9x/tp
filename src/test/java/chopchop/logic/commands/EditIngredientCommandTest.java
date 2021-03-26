package chopchop.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chopchop.commons.util.Pair;
import chopchop.commons.util.Result;
import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.CommandParser;
import chopchop.model.Model;
import chopchop.model.attributes.Tag;
import chopchop.testutil.StubbedModel;
import org.junit.jupiter.api.Test;

public class EditIngredientCommandTest {

    private CommandResult runCommand(Model m, String str) {
        var parser = new CommandParser();
        var c = parser.parse(str);

        if (c.isError()) {
            assertEquals(1, c.getError());
        }

        assertTrue(c.hasValue());

        return c.getValue().execute(m, new HistoryManager());
    }

    private CommandResult runCommand(String str) {
        return runCommand(StubbedModel.filled(), str);
    }

    @Test
    void test_nameEdits() {
        {
            var c = runCommand("edit ingredient owo");
            assertTrue(c.isError());
            assertEquals("Error: No ingredient named 'owo'", c.toString());
        }

        {
            var c = runCommand("edit ingredient custard");
            assertTrue(c.didSucceed());
            assertEquals("No edits provided; ingredient 'Custard' was not modified", c.toString());
        }
    }

    // @Test
    void test_equals() {
        var p = new CommandParser();
        var c1 = p.parse("edit ingredient custard /tag:add tag 1");
        var c2 = p.parse("edit ingredient custard /tag:add tag 1");
        var c3 = p.parse("edit ingredient custard /tag:add tag 2");
        var c4 = p.parse("edit ingredient cream /tag:add tag 1");

        assertEquals(c1, c1);
        assertEquals(c1, c2);

        assertNotEquals(c1, Result.of("asdf"));
        assertNotEquals(c1, c3);
        assertNotEquals(c3, c4);
    }

    @Test
    void test_undo() {
        var m = StubbedModel.filled();
        var p = new CommandParser();
        p.parse("edit ingredient custard /tag:add tag 1")
            .map(c -> Pair.of(c, c.execute(m, new HistoryManager())))
            .map(cr -> {
                assertTrue(cr.snd().didSucceed());
                return cr;
            })
            .map(cr -> {
                var erc = ((EditIngredientCommand) cr.fst());
                erc.undo(m);
                return cr;
            });

        var i = m.findIngredientWithName("custard");
        assertTrue(i.isPresent());

        var ingredient = i.get();
        assertFalse(ingredient.getTags().contains(new Tag("tag 1")));
    }

    @Test
    void test_tagEdits() {
        var m = StubbedModel.filled();

        {
            var c = runCommand(m, "edit ingredient custard /tag:add tag 1");
            assertTrue(c.didSucceed());

            var i = m.findIngredientWithName("custard");
            assertTrue(i.isPresent());

            var ingredient = i.get();
            assertTrue(ingredient.getTags().contains(new Tag("tag 1")));
        }

        {
            var c = runCommand(m, "edit ingredient custard /tag:delete tag 1");
            assertTrue(c.didSucceed());

            var i = m.findIngredientWithName("custard");
            assertTrue(i.isPresent());

            var ingredient = i.get();
            assertFalse(ingredient.getTags().contains(new Tag("tag 1")));
        }

        {
            var c = runCommand(m, "edit ingredient custard /tag:delete owowowo");
            assertTrue(c.isError());
            assertEquals("Error: Ingredient 'Custard' does not have tag 'owowowo'", c.toString());
        }
    }
}
