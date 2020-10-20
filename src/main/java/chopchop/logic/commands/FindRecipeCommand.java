package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import chopchop.commons.core.Messages;
import chopchop.model.Model;
import chopchop.model.attributes.NameContainsKeywordsPredicate;

/**
 * Finds and lists all recipes in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindRecipeCommand extends Command {

    public static final String COMMAND_WORD = "find recipe";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all recipes whose content contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " sugar tomato orange";

    private final NameContainsKeywordsPredicate predicate;

    /**
     * Constructs a command that finds the given recipe item.
     */
    public FindRecipeCommand(NameContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredRecipeList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_RECIPES_LISTED_OVERVIEW, model.getFilteredRecipeList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindRecipeCommand // instanceof handles nulls
                && predicate.equals(((FindRecipeCommand) other).predicate)); // state check
    }

}
