package chopchop.model.attributes;

import static chopchop.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents an item's name in the collection.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 * Comparisons are case insensitive.
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must be a alphanumeric character, otherwise " " (a blank string) becomes a
     * valid input. No restrictions on subsequent characters.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{ASCII}]*";

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
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                    && this.name.equalsIgnoreCase(((Name) other).name))
                || (other instanceof String
                    && this.name.equalsIgnoreCase((String) other));
    }

    @Override
    public int hashCode() {
        return this.name.toLowerCase().hashCode();
    }
}
