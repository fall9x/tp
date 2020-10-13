package chopchop.model.ingredient;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import chopchop.model.FoodEntry;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.ExpiryDate;

import chopchop.model.ingredient.exceptions.IncompatibleIngredientsException;

/**
 * Represents an Ingredient in the recipe manager.
 */
public class Ingredient extends FoodEntry {

    // Identity fields
    private final ExpiryDate expiryDate;

    // Data fields
    private final Quantity quantity;

    /**
     * Every field must be present and not null. Use this constructor if expiry date is not present.
     * Guarantees: details are present and not null, field values are validated, immutable.
     */
    public Ingredient(Name name, Quantity quantity) {
        super(name);
        requireNonNull(quantity);
        this.quantity = quantity;
        this.expiryDate = null;
    }

    /**
     * Every field must be present and not null. If expiry date is not present, use other constructor.
     * Guarantees: details are present and not null, field values are validated, immutable.
     */
    public Ingredient(Name name, Quantity quantity, ExpiryDate expiryDate) {
        super(name);
        requireAllNonNull(quantity, expiryDate);
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Optional<ExpiryDate> getExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    /**
     * Combines the quantities of this ingredient and the provided ingredient.
     *
     * @param other the other ingredient
     * @return      a new {@code Ingredient} with the combined quantities
     * @throws IncompatibleIngredientsException if the units of both ingredients were not compatible
     */
    public Ingredient combine(Ingredient other) throws IncompatibleIngredientsException {

        if (!this.name.equals(other.name)) {
            throw new IncompatibleIngredientsException(String.format("cannot combine '%s' with '%s'",
                this.name, other.name));
        }

        // TODO: expiry date handling! see #58
        return this.quantity.add(other.quantity)
            .map(newQty -> new Ingredient(this.name, newQty, this.expiryDate))
            .orElseThrow(err -> new IncompatibleIngredientsException(err));
    }

    /**
     * Returns true if both ingredients of the same name and expiry date.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Ingredient)) {
            return false;
        }

        Ingredient otherInd = (Ingredient) other;

        return otherInd.getName().equals(getName())
            && otherInd.getExpiryDate().equals(getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, expiryDate);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(getName())
                .append(" Quantity: ")
                .append(getQuantity());

        getExpiryDate().ifPresent(expiryDate -> builder.append(" Expiry Date: ").append(expiryDate));
        return builder.toString();
    }

}
