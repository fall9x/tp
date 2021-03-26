package chopchop.model.ingredient;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.units.Count;

/**
 * A reference to an ingredient. Not the actual {@code Ingredient}, and knows nothing about it.
 */
public class IngredientReference {

    private final String name;
    private final Quantity quantity;

    /**
     * Constructs an ingredient reference with the given name and quantity.
     */
    public IngredientReference(String name, Quantity quantity) {
        requireNonNull(name);
        requireNonNull(quantity);

        this.name = name;
        this.quantity = quantity;
    }

    /**
     * Constructs an ingredient reference with the given name and quantity.
     */
    public IngredientReference(String name, Optional<Quantity> quantity) {
        this(name, quantity.orElse(Count.of(1)));
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.quantity);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
            || (other instanceof IngredientReference
            && this.name.equals(((IngredientReference) other).name)
            && this.quantity.equals(((IngredientReference) other).quantity));
    }
}
