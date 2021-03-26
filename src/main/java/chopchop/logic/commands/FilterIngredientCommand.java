package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Entry;
import chopchop.model.Model;
import chopchop.model.attributes.ExpiryDateOnOrBeforePredicate;
import chopchop.model.attributes.NameContainsKeywordsFilterPredicate;
import chopchop.model.attributes.TagContainsKeywordsPredicate;

/**
 * Filters and lists all ingredients in ingredient book that match all filtering criteria.
 * Keyword matching is case insensitive.
 */
public class FilterIngredientCommand extends Command {

    private final TagContainsKeywordsPredicate tagPredicates;
    private final NameContainsKeywordsFilterPredicate namePredicates;
    private final ExpiryDateOnOrBeforePredicate expPredicate;

    /**
     * Constructs a command that finds the given ingredient item.
     * @param expPredicate
     * @param tagPredicates
     */
    public FilterIngredientCommand(ExpiryDateOnOrBeforePredicate expPredicate,
                                   TagContainsKeywordsPredicate tagPredicates,
                                   NameContainsKeywordsFilterPredicate namePredicates) {
        this.tagPredicates = tagPredicates;
        this.expPredicate = expPredicate;
        this.namePredicates = namePredicates;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);

        List<Predicate<Entry>> predicates = Arrays.asList(expPredicate, tagPredicates, namePredicates);
        Predicate<Entry> p = x -> true;
        p = predicates.stream().filter(x -> x != null).reduce(p, (x, y) -> x.and(y));
        model.updateFilteredIngredientList(p);

        var sz = model.getFilteredIngredientList().size();
        return CommandResult.message("Found %d ingredient%s", sz, sz == 1 ? "" : "s")
            .showingIngredientList();
    }

    @Override
    public String toString() {
        return String.format("FilterIngredientCommand(...)");
    }


    public static String getCommandString() {
        return "filter ingredient";
    }

    public static String getCommandHelp() {
        return "Filters ingredients by one or more criteria (names, tags and expiry dates)";
    }
}
