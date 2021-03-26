package chopchop.model.attributes;

import chopchop.model.Entry;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that an item's {@code Name} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Entry> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Entry entry) {
        if (entry.getTags().isEmpty()) {
            return false;
        }
        return this.keywords.stream()
                .map(kw -> kw.toLowerCase())
                .allMatch(keyword -> entry.getTags()
                    .stream()
                    .map(Tag::toString)
                    .anyMatch(tagName -> tagName.toLowerCase().contains(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
