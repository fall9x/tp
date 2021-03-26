package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Entry;
import chopchop.model.Model;
import chopchop.model.attributes.IngredientsContainsKeywordsPredicate;
import chopchop.model.attributes.NameContainsKeywordsFilterPredicate;
import chopchop.model.attributes.TagContainsKeywordsPredicate;

/**
 * Filters and lists all recipes in recipe book that match all filtering criteria.
 * Keyword matching is case insensitive.
 */
public class FilterRecipeCommand extends Command {

    private final IngredientsContainsKeywordsPredicate ingredientPredicates;
    private final TagContainsKeywordsPredicate tagPredicates;
    private final NameContainsKeywordsFilterPredicate namePredicates;

    /**
     * Constructs a command that filters and finds the matching recipe items.
     * @param indPredicates
     * @param tagPredicates
     */
    public FilterRecipeCommand(TagContainsKeywordsPredicate tagPredicates,
            IngredientsContainsKeywordsPredicate indPredicates,
            NameContainsKeywordsFilterPredicate namePredicates) {
        this.tagPredicates = tagPredicates;
        this.ingredientPredicates = indPredicates;
        this.namePredicates = namePredicates;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);

        List<Predicate<Entry>> predicates = Arrays.asList(ingredientPredicates, tagPredicates, namePredicates);
        Predicate<Entry> p = x -> true;
        p = predicates.stream().filter(x -> x != null).reduce(p, (x, y) -> x.and(y));
        model.updateFilteredRecipeList(p);

        var sz = model.getFilteredRecipeList().size();
        return CommandResult.message("Found %d recipe%s", sz, sz == 1 ? "" : "s")
            .showingRecipeList();
    }

    @Override
    public String toString() {
        return String.format("FilterRecipeCommand(...)");
    }

    public static String getCommandString() {
        return "filter recipe";
    }

    public static String getCommandHelp() {
        return "Filters recipes by one or more criteria (tags and ingredients used)";
    }
}





