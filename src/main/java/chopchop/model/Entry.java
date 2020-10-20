package chopchop.model;

import static java.util.Objects.requireNonNull;

import chopchop.model.attributes.Name;

public abstract class Entry {
    protected final Name name;

    protected Entry(String name) {
        requireNonNull(name);
        this.name = new Name(name);
    }

    public String getName() {
        return this.name.toString();
    }

    /**
     * Returns true if both entries have the same name.
     * This defines a weaker notion of equality between two entries.
     */
    public abstract boolean isSame(Entry other);

    /**
     * Returns true if both entries have the same fields.
     * This defines a stronger notion of equality between two entries.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Entry
                && this.name.equals(((Entry) other).name));
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
