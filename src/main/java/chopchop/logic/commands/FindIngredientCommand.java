package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.attributes.NameContainsKeywordsPredicate;

/**
 * Finds and lists all ingredients in ingredient book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindIngredientCommand extends Command {

    private final NameContainsKeywordsPredicate predicate;

    /**
     * Constructs a command that finds the given ingredient item.
     */
    public FindIngredientCommand(NameContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredIngredientList(predicate);

        var sz = model.getFilteredIngredientList().size();
        return CommandResult.message("Found %d ingredient%s", sz, sz == 1 ? "" : "s")
            .showingIngredientList();
    }

    @Override
    public String toString() {
        return String.format("FindIngredientCommand(keywords: %s)", this.predicate.getKeywords());
    }

    public static String getCommandString() {
        return "find ingredient";
    }

    public static String getCommandHelp() {
        return "Finds ingredients by searching for keywords in their names";
    }
}
