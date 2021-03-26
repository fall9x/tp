package chopchop.model.attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import chopchop.commons.util.StringUtil;
import chopchop.model.Entry;

/**
 * Tests that an item's {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Entry> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywords() {
        return new ArrayList<>(this.keywords);
    }

    @Override
    public boolean test(Entry entry) {
        return this.keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(entry.getName(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof NameContainsKeywordsPredicate
                && this.keywords.equals(((NameContainsKeywordsPredicate) other).keywords));
    }
}
