package chopchop.model.attributes;

import static chopchop.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Tag for both Recipe and Ingredient.
 * E.G. "Halal", "Vegetarian" for Recipe. "Carbs", "Vitamin E" for Ingredient.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTag(String)} (String)}.
 */
public class Tag {
    public static final String MESSAGE_CONSTRAINTS = "Tags should not be blank";

    private final String tag;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tag A valid tag name.
     */
    public Tag(String tag) {
        requireNonNull(tag);
        checkArgument(isValidTag(tag), MESSAGE_CONSTRAINTS);
        this.tag = tag;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTag(String test) {
        return !test.isBlank() && test.equals(test.trim());
    }

    @Override
    public String toString() {
        return this.tag;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Tag
                && this.tag.equalsIgnoreCase(((Tag) other).tag)
                || (other instanceof String
                && this.tag.equalsIgnoreCase((String) other)));
    }

    @Override
    public int hashCode() {
        return this.tag.toLowerCase().hashCode();
    }
}
