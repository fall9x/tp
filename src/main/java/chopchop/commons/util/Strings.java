// Strings.java

package chopchop.commons.util;

import java.util.List;

import chopchop.logic.parser.ArgName;

/**
 * Defines user-facing strings for the whole program.
 */
public class Strings {

    // misc things
    public static final String USER_GUIDE_BASE_URL  = "https://ay2021s1-cs2103t-t10-3.github.io/tp/UserGuide.html";

    // argument names
    public static final ArgName ARG_STEP            = new ArgName("step");
    public static final ArgName ARG_INGREDIENT      = new ArgName("ingredient");
    public static final ArgName ARG_QUANTITY        = new ArgName("qty");
    public static final ArgName ARG_EXPIRY          = new ArgName("expiry");
    public static final ArgName ARG_TAG             = new ArgName("tag");
    public static final ArgName ARG_NAME            = new ArgName("name");
    public static final ArgName ARG_AFTER           = new ArgName("after");
    public static final ArgName ARG_BEFORE          = new ArgName("before");

    // command names
    public static final String COMMAND_ADD          = "add";
    public static final String COMMAND_HELP         = "help";
    public static final String COMMAND_LIST         = "list";
    public static final String COMMAND_EDIT         = "edit";
    public static final String COMMAND_FIND         = "find";
    public static final String COMMAND_QUIT         = "quit";
    public static final String COMMAND_MAKE         = "make";
    public static final String COMMAND_UNDO         = "undo";
    public static final String COMMAND_REDO         = "redo";
    public static final String COMMAND_VIEW         = "view";
    public static final String COMMAND_STATS        = "stats";
    public static final String COMMAND_CLEAR        = "clear";
    public static final String COMMAND_FILTER       = "filter";
    public static final String COMMAND_DELETE       = "delete";


    public static final List<String> COMMAND_NAMES = List.of(
        COMMAND_ADD,
        COMMAND_VIEW,
        COMMAND_HELP,
        COMMAND_LIST,
        COMMAND_FIND,
        COMMAND_EDIT,
        COMMAND_QUIT,
        COMMAND_MAKE,
        COMMAND_UNDO,
        COMMAND_REDO,
        COMMAND_STATS,
        COMMAND_CLEAR,
        COMMAND_FILTER,
        COMMAND_DELETE
    );

    public static final String STATS_KIND_TOP       = "top";
    public static final String STATS_KIND_USED      = "used";
    public static final String STATS_KIND_MADE      = "made";
    public static final String STATS_KIND_CLEAR     = "clear";
    public static final String STATS_KIND_RECENT    = "recent";
}
