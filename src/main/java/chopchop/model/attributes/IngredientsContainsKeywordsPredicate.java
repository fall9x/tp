package chopchop.model.attributes;

import chopchop.model.Entry;
import chopchop.model.recipe.Recipe;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that an item's {@code Name} matches any of the keywords given.
 */
public class IngredientsContainsKeywordsPredicate implements Predicate<Entry> {
    private final List<String> keywords;

    public IngredientsContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Entry entry) {
        if (!(entry instanceof Recipe)) {
            return false;
        }
        Recipe recipe = (Recipe) entry;

        return this.keywords.stream()
                .map(kw -> kw.toLowerCase())
                .allMatch(keyword -> recipe.getIngredients()
                .stream()
                .map(indRef -> indRef.getName())
                .anyMatch(indName -> indName.toLowerCase().contains(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IngredientsContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((IngredientsContainsKeywordsPredicate) other).keywords)); // state check
    }

}
