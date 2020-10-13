// Count.java

package chopchop.model.attributes.units;

import chopchop.util.Result;
import chopchop.model.attributes.Quantity;

public class Count implements Quantity {

    private final double value;

    private Count(double value) {
        this.value = value;
    }

    @Override
    public Result<Count> add(Quantity qty) {

        if (!(qty instanceof Count)) {
            return Result.error("cannot add '%s' to '%s' (incompatible units)", qty, this);
        } else {
            var cnt = (Count) qty;
            return Result.of(new Count(this.value + cnt.value));
        }
    }

    @Override
    public String toString() {
        return Quantity.formatDecimalValue(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Count)
            && (((Count) obj).value == this.value);
    }

    /**
     * Returns representation of the count.
     *
     * @param value the numerical value of the quantity
     * @param unit  the unit string, which should be empty.
     * @return      the quantity, if the unit was valid.
     */
    public static Result<Quantity> of(double value, String unit) {
        if (unit.isEmpty()) {
            return Result.of(new Count(value));
        } else {
            return Result.error("count should not have units (found '%s')", unit);
        }
    }

    /**
     * Returns a unitless count
     *
     * @param value the number of things
     * @return      the quantity
     */
    public static Count of(double value) {
        return new Count(value);
    }
}
