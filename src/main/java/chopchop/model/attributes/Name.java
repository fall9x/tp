package chopchop.model.attributes;

import static chopchop.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents an item's name in the collection.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 * Comparisons are case insensitive.
 */
public class Name {
    public static final String MESSAGE_CONSTRAINTS = "Names should not be blank";

    private final String name;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        this.name = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return !test.isBlank() && test.equals(test.trim());
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Name
                && this.name.equalsIgnoreCase(((Name) other).name))
                || (other instanceof String
                && this.name.equalsIgnoreCase((String) other));
    }

    @Override
    public int hashCode() {
        return this.name.toLowerCase().hashCode();
    }
}
