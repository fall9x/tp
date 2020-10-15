// CommandTarget.java

package chopchop.logic.parser.commands;

/**
 * Just a very simple enumeration to represent the "target" of a command, which is either
 * recipes (eg. add recipe) or ingredients (eg. add ingredient).
 */
public enum CommandTarget {
    RECIPE,
    INGREDIENT
}
