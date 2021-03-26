package chopchop.logic.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Function;

import chopchop.commons.core.Log;
import chopchop.commons.util.StringView;
import chopchop.commons.util.Strings;
import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    private static final String METHOD_NAME_GET_CMD = "getCommandString";
    private static final String METHOD_NAME_GET_HELP = "getCommandHelp";
    private static final Log logger = new Log(HelpCommand.class);

    private final Optional<String> helpCommand;
    private final Optional<String> helpTarget;

    /**
     * Constructs a new HelpCommand for the given command and target.
     */
    public HelpCommand(Optional<String> command, Optional<String> target) {
        this.helpCommand = command;
        this.helpTarget = target;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {

        if (helpCommand.isEmpty() || helpCommand.get().isEmpty()) {
            return CommandResult.message("Refer to the")
                .appendingLink("User Guide", Strings.USER_GUIDE_BASE_URL, /* newline: */ false)
                .appending("for more detailed help", /* newline: */ false);
        }

        var cmd = helpCommand.get();

        var cls = getCommandClassFor(cmd, this.helpTarget.orElse(""));
        if (cls == null) {
            return CommandResult.error("Unknown command '%s'; see the", cmd)
                .appendingLink("User Guide", Strings.USER_GUIDE_BASE_URL + "#CommandSummary", false)
                .appending("for a list of commands", false);
        }

        var cmdStr = invokeMethod(cls, METHOD_NAME_GET_CMD);
        var cmdHelp = invokeMethod(cls, METHOD_NAME_GET_HELP);

        if (cmdStr == null || cmdHelp == null) {
            return CommandResult.error("No help available for command '%s'", cmd);
        }

        var ugSection = cls.getSimpleName();
        var ret = CommandResult.message("%s: %s", cmdStr, cmdHelp);

        if (!ugSection.endsWith("Dummy")) {
            return ret.appending("See the", /* newline: */ true)
                .appendingLink("User Guide", Strings.USER_GUIDE_BASE_URL + "#" + ugSection,
                    /* newline: */ false);
        } else {
            return ret;
        }
    }





    private Class<?> getCommandClassFor(String cmd, String target) {

        var camelCasing = (Function<String, String>) s -> {
            return s.isEmpty()
                ? ""
                : (s.substring(0, 1).toUpperCase() + s.substring(1));
        };

        var pkg = "chopchop.logic.commands.";

        var extraTarget = "";
        var xs = new StringView(target).words();

        if (xs.size() > 1) {
            target = xs.get(0);
            extraTarget = xs.get(1);
        }

        if (target.equals("recipes")) {
            target = "recipe";
        } else if (target.equals("ingredients")) {
            target = "ingredient";
        }

        for (var cmdName : Strings.COMMAND_NAMES) {

            if (!cmdName.equals(cmd)) {
                continue;
            }

            var className = pkg
                + camelCasing.apply(cmdName)
                + camelCasing.apply(target)
                + camelCasing.apply(extraTarget)
                + "Command";

            logger.debug("searching for class '%s'", className);
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                try {
                    // try to find the dummy class.
                    var dummyPkg = "chopchop.logic.commands.HelpCommand$";
                    className = dummyPkg
                        + camelCasing.apply(cmdName)
                        + "CommandDummy";

                    logger.debug("not found, searching for dummy class '%s'", className);
                    return Class.forName(className);

                } catch (ClassNotFoundException e1) {
                    logger.debug("dummy class not found either");
                    break;
                }
            }
        }

        return null;
    }

    private String invokeMethod(Class<?> cls, String methodName) {

        try {
            var method = cls.getMethod(methodName);
            var ret = method.invoke(null);

            if (ret instanceof String) {
                return (String) ret;
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // do nothing, but exit the branch and return the error.
            logger.error("command class '%s' had no method '%s': %s", cls, methodName, e);
        }

        return null;
    }



    @Override
    public String toString() {
        return String.format("HelpCommand");
    }

    public static String getCommandString() {
        return "help";
    }

    public static String getCommandHelp() {
        return "Shows a link to the user guide for ChopChop, and offers help for individual commands";
    }




    // internal fake classes for commands requiring targets.
    private static class AddCommandDummy {
        public static String getCommandString() {
            return "add";
        }
        public static String getCommandHelp() {
            return "Adds an item; see 'add recipe' or 'add ingredient'";
        }
    }

    private static class DeleteCommandDummy {
        public static String getCommandString() {
            return "delete";
        }
        public static String getCommandHelp() {
            return "Deletes an item; see 'delete recipe' or 'delete ingredient'";
        }
    }

    private static class ListCommandDummy {
        public static String getCommandString() {
            return "list";
        }
        public static String getCommandHelp() {
            return "Lists items; see 'list recipes', 'list ingredients', or 'list recommendations'";
        }
    }

    private static class FindCommandDummy {
        public static String getCommandString() {
            return "find";
        }
        public static String getCommandHelp() {
            return "Finds items; see 'find recipes' or 'find ingredients'";
        }
    }

    private static class FilterCommandDummy {
        public static String getCommandString() {
            return "filter";
        }
        public static String getCommandHelp() {
            return "Filters items; see 'filter recipes' or 'filter ingredients'";
        }
    }

    private static class ViewCommandDummy {
        public static String getCommandString() {
            return "view";
        }
        public static String getCommandHelp() {
            return "Views a recipe; see 'view recipe'";
        }
    }

    private static class MakeCommandDummy {
        public static String getCommandString() {
            return "make";
        }
        public static String getCommandHelp() {
            return "Makes a recipe; see 'make recipe'";
        }
    }

    private static class EditCommandDummy {
        public static String getCommandString() {
            return "edit";
        }
        public static String getCommandHelp() {
            return "Edits an item; see 'edit recipe' or 'edit ingredient'";
        }
    }

    private static class StatsCommandDummy {
        public static String getCommandString() {
            return "stats";
        }
        public static String getCommandHelp() {
            return "Lists recipe and ingredient statistics; see 'stats recipe made' or 'stats ingredient used'";
        }
    }
}
