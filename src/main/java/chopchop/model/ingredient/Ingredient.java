package chopchop.model.ingredient;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.commons.util.Pair;
import chopchop.model.Entry;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.Tag;
import chopchop.model.attributes.units.Count;
import chopchop.model.exceptions.IncompatibleIngredientsException;

/**
 * Represents an Ingredient in the recipe manager.
 */
public class Ingredient extends Entry {
    // comparator that compares expiry dates, and puts empty expiries at the end.
    public static final Comparator<Optional<ExpiryDate>> SET_COMPARATOR = (a, b) -> {
        if (a.isEmpty() && b.isEmpty()) {
            return 0;
        } else if (a.isEmpty()) {
            return 1;
        } else if (b.isEmpty()) {
            return -1;
        } else {
            return a.get().compareTo(b.get());
        }
    };

    private final TreeMap<Optional<ExpiryDate>, Quantity> sets;
    private final Set<Tag> tags;

    /**
     * Every field (less expiry date) must be present and not null.
     * Guarantees: details (less expiry date) are present and not null, field values are validated, immutable.
     */
    public Ingredient(String name, Quantity quantity, ExpiryDate expiryDate, Set<Tag> tags) {
        super(name);
        requireAllNonNull(quantity, tags);

        this.sets = new TreeMap<>(SET_COMPARATOR);
        this.sets.put(Optional.ofNullable(expiryDate), quantity);
        this.tags = new HashSet<>(tags);
    }

    /**
     * Constructor that accepts optionals for quantity and expiry date for convenience.
     */
    public Ingredient(String name, Optional<Quantity> quantity, Optional<ExpiryDate> expiryDate, Set<Tag> tags) {
        super(name);
        requireAllNonNull(quantity, expiryDate, tags);

        this.sets = new TreeMap<>(SET_COMPARATOR);
        this.sets.put(expiryDate, quantity.orElse(Count.of(1)));
        this.tags = new HashSet<>(tags);
    }

    /**
     * Constructs a set of ingredients directly from the map of expiry dates and quantities.
     */
    public Ingredient(String name, TreeMap<Optional<ExpiryDate>, Quantity> sets, Set<Tag> tags) {
        super(name);
        requireAllNonNull(sets, tags);

        this.sets = new TreeMap<>(SET_COMPARATOR);
        this.sets.putAll(sets);
        this.tags = new HashSet<>(tags);
    }

    public Quantity getQuantity() {
        assert !this.sets.isEmpty();

        // we *COULD* make an "identity" for Quantity, but that's too much effort, and each
        // class of Quantity would need to explicitly handle that. Sadge.

        // if we have at least 2, then we can get() the optional.
        if (this.sets.size() >= 2) {
            return this.sets.values()
                .stream()
                .reduce((a, b) -> a.add(b).getValue())
                .get();
        } else {
            // well. then it's just the first one.
            return this.sets.firstEntry().getValue();
        }
    }

    public Quantity getUnexpiredQuantity() {
        assert !this.sets.isEmpty();

        return this.sets.tailMap(Optional.of(new ExpiryDate(LocalDate.now())))
                .values()
                .stream()
                .reduce((a, b) -> a.add(b).getValue())
                .orElseThrow(() -> new IncompatibleIngredientsException("No unexpired ingredients"));
    }

    public Optional<ExpiryDate> getExpiryDate() {
        assert !this.sets.isEmpty();

        // just return the first expiry date.
        return this.sets.firstKey();
    }

    public TreeMap<Optional<ExpiryDate>, Quantity> getIngredientSets() {
        // i want const correctness dammit
        var ret = new TreeMap<Optional<ExpiryDate>, Quantity>(SET_COMPARATOR);
        ret.putAll(this.sets);

        return ret;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    /**
     * Combines the quantities of this ingredient and the provided ingredient.
     *
     * @param other the other ingredient
     * @return      a new {@code Ingredient} with the combined quantities
     * @throws IncompatibleIngredientsException if the units of both ingredients were not compatible
     */
    public Ingredient combine(Ingredient other) throws IncompatibleIngredientsException {
        if (!this.isSame(other)) {
            throw new IncompatibleIngredientsException(String.format("cannot combine '%s' with '%s'",
                this.name, other.name));
        }

        // the validity of quantities is kinda like induction. Assume that we start with a series of valid sets;
        // if each new addition was compatible, then the final set will be compatible.
        var thisQty = this.getQuantity();

        // there's no constructor that takes both an existing map and the comparator...
        var newSets = new TreeMap<Optional<ExpiryDate>, Quantity>(SET_COMPARATOR);
        newSets.putAll(this.sets);

        // because of exceptions, we cannot do this using nice lambdas and stuff.
        // so write some dirty imperative code to merge the ingredients.
        for (var entry : other.sets.entrySet()) {
            var exp = entry.getKey();
            var qty = entry.getValue();

            if (qty.isZero()) {
                continue;
            }

            Quantity newQty = null;

            // get the existing quantity of ingredient with the given expiry date
            var existingQty = newSets.get(exp);
            if (existingQty != null) {
                // it exists; time to combine them using Quantity::add()
                // (assuming they are compatible, of course)
                newQty = existingQty.add(qty).orElseThrow(IncompatibleIngredientsException::new);
            } else {
                newQty = qty;
            }

            if (!thisQty.compatibleWith(newQty)) {
                throw new IncompatibleIngredientsException(
                    String.format("incompatible units ('%s' and '%s')", thisQty, newQty)
                );
            }

            newSets.put(exp, newQty);
        }

        // here we combine the tags.
        // TODO: investigate if this is something we should actually do
        var newTags = new HashSet<>(this.tags);
        newTags.addAll(other.tags);

        return new Ingredient(this.name.toString(), newSets, newTags);
    }

    /**
     * Splits this ingredient into two ingredients, one containing the given quantity,
     * and one containing the remaining quantity. Ingredients which expire earlier will be
     * split first.
     *
     * @param quantity the quantity to split by
     * @return a {@code Pair} of {@code Ingredient}s split by the given quantity
     * @throws IllegalValueException if the quantity provided is larger than the total quantity
     * of the ingredient
     */
    public Pair<Ingredient, Ingredient> split(Quantity quantity)
        throws IllegalValueException, IncompatibleIngredientsException {

        {
            var existingQty = this.getQuantity();
            try {
                if (existingQty.compareTo(quantity) < 0 || quantity.isNegative()) {
                    throw new IllegalValueException(String.format(
                        "insufficient '%s' to remove given quantity (need %s, have only %s)",
                        this.name.toString(), quantity, existingQty
                    ));
                }
                // this is thrown by compareTo
            } catch (IncompatibleIngredientsException e) {
                throw new IncompatibleIngredientsException(String.format(
                    "%s (for ingredient '%s')", e.getMessage(), this.getName()
                ));
            }
        }

        var firstSets = new TreeMap<Optional<ExpiryDate>, Quantity>(SET_COMPARATOR);
        var secondSets = new TreeMap<Optional<ExpiryDate>, Quantity>(SET_COMPARATOR);
        var currQuantity = quantity;
        var splitKey = this.sets.firstKey();

        var incompatibleException = (Function<String, IncompatibleIngredientsException>) (e -> {
            return new IncompatibleIngredientsException(String.format(
                "%s (for ingredient '%s')", e, this.getName()
            ));
        });

        for (var entry : this.sets.entrySet()) {
            if (entry.getValue().compareTo(currQuantity) < 0) {
                currQuantity = currQuantity.subtract(entry.getValue())
                    .orElseThrow(incompatibleException);
            } else {
                splitKey = entry.getKey();
                break;
            }
        }

        firstSets.putAll(this.sets.subMap(this.sets.firstKey(), splitKey));
        firstSets.put(splitKey, currQuantity);
        secondSets.putAll(this.sets.subMap(splitKey, false, this.sets.lastKey(), true));

        var remainingQuantity = this.sets.get(splitKey).subtract(currQuantity)
            .orElseThrow(incompatibleException);

        if (!remainingQuantity.isZero()) {
            secondSets.put(splitKey, remainingQuantity);
        }

        return new Pair<>(new Ingredient(this.name.toString(), firstSets, this.tags),
                new Ingredient(this.name.toString(), secondSets, this.tags));
    }

    @Override
    public boolean isSame(Entry other) {
        return other == this
                || (other instanceof Ingredient
                && this.name.equals(((Ingredient) other).name));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Ingredient
                && this.name.equals(((Ingredient) other).name)
                && this.sets.equals(((Ingredient) other).sets))
                && this.tags.equals(((Ingredient) other).tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.sets, this.tags);
    }

    @Override
    public String toString() {
        var tagJoiner = new StringJoiner(", ", "<Tags: ", ">");
        tagJoiner.setEmptyValue("");
        this.getTags().forEach(tag -> tagJoiner.add(tag.toString()));

        return Stream.of(String.format("%s (%s)", this.getName(), this.getQuantity()),
                this.getExpiryDate().map(expiryDate -> String.format("<Expiry Date: %s>", expiryDate)).orElse(""),
                tagJoiner.toString())
                .filter(field -> !field.isEmpty())
                .collect(Collectors.joining(" "));
    }
}
