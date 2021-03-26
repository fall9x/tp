package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.attributes.NameContainsKeywordsPredicate;

/**
 * Finds and lists all recipes whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindRecipeCommand extends Command {

    private final NameContainsKeywordsPredicate predicate;

    /**
     * Constructs a command that finds the given recipe item.
     */
    public FindRecipeCommand(NameContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredRecipeList(predicate);

        var sz = model.getFilteredRecipeList().size();
        return CommandResult.message("Found %d recipe%s", sz, sz == 1 ? "" : "s")
            .showingRecipeList();
    }

    @Override
    public String toString() {
        return String.format("FindRecipeCommand(keywords: %s)", this.predicate.getKeywords());
    }

    public static String getCommandString() {
        return "find recipe";
    }

    public static String getCommandHelp() {
        return "Finds recipes by searching for keywords in their names";
    }
}
