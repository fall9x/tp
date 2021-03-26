package chopchop.model.attributes;

import chopchop.model.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that an item's {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsFilterPredicate implements Predicate<Entry> {
    private final List<String> keywords;

    public NameContainsKeywordsFilterPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywords() {
        return new ArrayList<>(this.keywords);
    }

    @Override
    public boolean test(Entry entry) {
        return this.keywords.stream()
                .map(kw -> kw.toLowerCase())
                .allMatch(keyword -> entry.getName().toLowerCase().contains(keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof NameContainsKeywordsFilterPredicate
                && this.keywords.equals(((NameContainsKeywordsFilterPredicate) other).keywords));
    }
}
