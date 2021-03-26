// HelpCommandTest.java

package chopchop.logic.commands;

import java.util.HashMap;
import java.util.Optional;

import chopchop.commons.util.Pair;
import chopchop.model.ModelStub;
import chopchop.logic.history.HistoryManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HelpCommandTest {

    private void test(HashMap<Pair<String, String>, String> cases) {

        var model = new ModelStub();
        var history = new HistoryManager();

        cases.forEach((k, v) -> {
            var cmd = new HelpCommand(Optional.ofNullable(k.fst()), Optional.ofNullable(k.snd()));

            assertEquals(v, cmd.execute(model, history).toString());
        });
    }

    @Test
    public void test_helpCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("help", ""),
            "help: Shows a link to the user guide for ChopChop, and offers help for individual commands"
                + " See the User Guide");

        test(cases);
    }

    @Test
    public void test_addCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("add", ""),
            String.format("add: Adds an item; see 'add recipe' or 'add ingredient'"));

        cases.put(Pair.of("add", "recipe"),
            String.format("%s: %s %s",
                AddRecipeCommand.getCommandString(),
                AddRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("add", "ingredient"),
            String.format("%s: %s %s",
                AddIngredientCommand.getCommandString(),
                AddIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }

    @Test
    public void test_deleteCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("delete", ""),
            "delete: Deletes an item; see 'delete recipe' or 'delete ingredient'");

        cases.put(Pair.of("delete", "recipe"),
            String.format("%s: %s %s",
                DeleteRecipeCommand.getCommandString(),
                DeleteRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("delete", "ingredient"),
            String.format("%s: %s %s",
                DeleteIngredientCommand.getCommandString(),
                DeleteIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }



    @Test
    public void test_viewCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("view", ""),
            "view: Views a recipe; see 'view recipe'");

        cases.put(Pair.of("view", "recipe"),
            String.format("%s: %s %s",
                ViewRecipeCommand.getCommandString(),
                ViewRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }


    @Test
    public void test_makeCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("make", ""),
            "make: Makes a recipe; see 'make recipe'");

        cases.put(Pair.of("make", "recipe"),
            String.format("%s: %s %s",
                MakeRecipeCommand.getCommandString(),
                MakeRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }

    @Test
    public void test_editCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("edit", ""),
            "edit: Edits an item; see 'edit recipe' or 'edit ingredient'");

        cases.put(Pair.of("edit", "recipe"),
            String.format("%s: %s %s",
                EditRecipeCommand.getCommandString(),
                EditRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }

    @Test
    public void test_listCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("list", ""),
            "list: Lists items; see 'list recipes', 'list ingredients', or 'list recommendations'");

        cases.put(Pair.of("list", "recipe"),
            String.format("%s: %s %s",
                ListRecipeCommand.getCommandString(),
                ListRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("list", "recipes"),
            String.format("%s: %s %s",
                ListRecipeCommand.getCommandString(),
                ListRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );


        cases.put(Pair.of("list", "ingredient"),
            String.format("%s: %s %s",
                ListIngredientCommand.getCommandString(),
                ListIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("list", "ingredients"),
            String.format("%s: %s %s",
                ListIngredientCommand.getCommandString(),
                ListIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }

    @Test
    public void test_findCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("find", ""),
            "find: Finds items; see 'find recipes' or 'find ingredients'");

        cases.put(Pair.of("find", "recipe"),
            String.format("%s: %s %s",
                FindRecipeCommand.getCommandString(),
                FindRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("find", "recipes"),
            String.format("%s: %s %s",
                FindRecipeCommand.getCommandString(),
                FindRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("find", "ingredient"),
            String.format("%s: %s %s",
                FindIngredientCommand.getCommandString(),
                FindIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("find", "ingredients"),
            String.format("%s: %s %s",
                FindIngredientCommand.getCommandString(),
                FindIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );
        test(cases);
    }

    @Test
    public void test_filterCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("filter", ""),
            "filter: Filters items; see 'filter recipes' or 'filter ingredients'");

        cases.put(Pair.of("filter", "recipe"),
            String.format("%s: %s %s",
                FilterRecipeCommand.getCommandString(),
                FilterRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("filter", "recipes"),
            String.format("%s: %s %s",
                FilterRecipeCommand.getCommandString(),
                FilterRecipeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("filter", "ingredient"),
            String.format("%s: %s %s",
                FilterIngredientCommand.getCommandString(),
                FilterIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("filter", "ingredients"),
            String.format("%s: %s %s",
                FilterIngredientCommand.getCommandString(),
                FilterIngredientCommand.getCommandHelp(),
                "See the User Guide")
        );
        test(cases);
    }

    @Test
    public void test_statsCommand() {

        var cases = new HashMap<Pair<String, String>, String>();

        var msg = "stats: Lists recipe and ingredient statistics; see 'stats recipe made' or 'stats ingredient used'";
        cases.put(Pair.of("stats", ""), msg);
        cases.put(Pair.of("stats", "recipe"), msg);
        cases.put(Pair.of("stats", "recipes"), msg);
        cases.put(Pair.of("stats", "ingredient"), msg);
        cases.put(Pair.of("stats", "ingredients"), msg);

        cases.put(Pair.of("stats", "recipe recent"),
            String.format("%s: %s %s",
                StatsRecipeRecentCommand.getCommandString(),
                StatsRecipeRecentCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("stats", "recipe top"),
            String.format("%s: %s %s",
                StatsRecipeTopCommand.getCommandString(),
                StatsRecipeTopCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("stats", "recipe made"),
            String.format("%s: %s %s",
                StatsRecipeMadeCommand.getCommandString(),
                StatsRecipeMadeCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("stats", "recipe clear"),
            String.format("%s: %s %s",
                StatsRecipeClearCommand.getCommandString(),
                StatsRecipeClearCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("stats", "ingredient recent"),
            String.format("%s: %s %s",
                StatsIngredientRecentCommand.getCommandString(),
                StatsIngredientRecentCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("stats", "ingredient used"),
            String.format("%s: %s %s",
                StatsIngredientUsedCommand.getCommandString(),
                StatsIngredientUsedCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("stats", "ingredient clear"),
            String.format("%s: %s %s",
                StatsIngredientClearCommand.getCommandString(),
                StatsIngredientClearCommand.getCommandHelp(),
                "See the User Guide")
        );


        test(cases);
    }


    @Test
    public void test_miscCommands() {

        var cases = new HashMap<Pair<String, String>, String>();

        cases.put(Pair.of("clear", ""),
            String.format("%s: %s %s",
                ClearCommand.getCommandString(),
                ClearCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("undo", ""),
            String.format("%s: %s %s",
                UndoCommand.getCommandString(),
                UndoCommand.getCommandHelp(),
                "See the User Guide")
        );

        cases.put(Pair.of("redo", ""),
            String.format("%s: %s %s",
                RedoCommand.getCommandString(),
                RedoCommand.getCommandHelp(),
                "See the User Guide")
        );

        test(cases);
    }


    @Test
    public void test_badCommand() throws Exception {
        var cases = new HashMap<Pair<String, String>, String>();


        cases.put(Pair.of("", ""), "Refer to the User Guide for more detailed help");
        cases.put(Pair.of(null, ""), "Refer to the User Guide for more detailed help");
        cases.put(Pair.of("add", "omo"),
            "add: Adds an item; see 'add recipe' or 'add ingredient'");

        cases.put(Pair.of("owo", ""),
            "Error: Unknown command 'owo'; see the User Guide for a list of commands"
        );

        test(cases);
    }
}
