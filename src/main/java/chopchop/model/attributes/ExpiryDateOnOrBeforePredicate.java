package chopchop.model.attributes;

import chopchop.model.Entry;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Tests that an item's {@code ExpiryDate} is on or before the given expiry date
 */
public class ExpiryDateOnOrBeforePredicate implements Predicate<Entry> {
    private final ExpiryDate exd;

    public ExpiryDateOnOrBeforePredicate(ExpiryDate exd) {
        this.exd = exd;
    }

    @Override
    public boolean test(Entry entry) {
        Optional<ExpiryDate> currExp = entry.getExpiryDate();
        if (currExp.isPresent()) {
            return currExp.get().compareTo(exd) <= 0;
        }

        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpiryDateOnOrBeforePredicate // instanceof handles nulls
                && this.exd.equals(((ExpiryDateOnOrBeforePredicate) other).exd)); // state check
    }

}
