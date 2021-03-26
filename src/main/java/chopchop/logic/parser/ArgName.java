// ArgName.java

package chopchop.logic.parser;

import java.util.Objects;

import java.util.ArrayList;
import java.util.List;

import chopchop.commons.util.StringView;

/**
 * A helper class to abstract away the menial task of handling '/' when printing argument names
 */
public class ArgName {

    private final String name;
    private final List<String> components;

    /**
     * Constructs a new argument name from the given string. Note that it *should not* include
     * the leading slash.
     *
     * @param name the name of the argument.
     */
    public ArgName(String name) {
        Objects.requireNonNull(name);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("argument name cannot be empty");
        } else if (name.startsWith("/")) {
            throw new IllegalArgumentException("argument name cannot start with '/'");
        }

        this.components = new ArrayList<>();

        var sv = new StringView(name);

        // check for components
        if (sv.find(':') != -1) {

            var parts = sv.splitBy(c -> c == ':');

            this.name = parts.get(0);
            this.components.addAll(parts.subList(1, parts.size()));

        } else {
            this.name = name;
        }
    }

    public List<String> getComponents() {
        return this.components;
    }

    /**
     * Returns the un-decorated prefix name.
     */
    public String name() {
        return this.name;
    }

    /**
     * Returns the decorated (ie. with a slash) prefix name.
     */
    @Override
    public String toString() {
        return String.format("/%s%s%s", this.name,
            this.components.isEmpty() ? "" : ":",
            String.join(":", this.components));
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ArgName)
            && ((ArgName) obj).name.equals(this.name)
            && ((ArgName) obj).components.equals(this.components);
    }

    /**
     * Returns true iff the name of the argument matches.
     */
    public boolean nameEquals(String s) {
        return this.name.equals(s);
    }

    /**
     * Returns true iff the name of the two arguments matche.
     */
    public boolean nameEquals(ArgName s) {
        return this.name.equals(s.name);
    }
}
