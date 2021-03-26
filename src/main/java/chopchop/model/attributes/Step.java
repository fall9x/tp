package chopchop.model.attributes;

import static chopchop.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

public class Step {
    public static final String MESSAGE_CONSTRAINTS = "Steps should not be blank";

    public final String step;

    /**
     * Constructs a {@code Step}.
     *
     * @param step A valid step.
     */
    public Step(String step) {
        requireNonNull(step);
        checkArgument(isValidStep(step), MESSAGE_CONSTRAINTS);
        this.step = step;
    }

    /**
     * Returns true if a given string is a valid step.
     */
    public static boolean isValidStep(String test) {
        return !test.isBlank() && test.equals(test.trim());
    }

    @Override
    public String toString() {
        return this.step;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Step
                && this.step.equals(((Step) other).step));
    }

    @Override
    public int hashCode() {
        return this.step.hashCode();
    }

}

