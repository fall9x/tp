package chopchop.testutil;

import java.util.HashSet;
import java.util.Set;

import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Tag;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.units.Count;
import chopchop.model.ingredient.Ingredient;

public class IngredientBuilder {

    public static final String DEFAULT_NAME = "Egg";
    public static final int DEFAULT_QTY = 3;
    public static final String DEFAULT_EXPIRY = "2020-10-12";

    private String name;
    private Quantity qty;
    private ExpiryDate expDate;
    private Set<Tag> tags;

    /**
     * Creates a {@code IngredientBuilder} with the default details.
     */
    public IngredientBuilder() {
        qty     = Count.of(DEFAULT_QTY);
        name    = DEFAULT_NAME;
        expDate = new ExpiryDate(DEFAULT_EXPIRY);
        tags    = new HashSet<>();
    }

    /**
     * Initializes the IngredientBuilder with the data of {@code ingredientToCopy}.
     */
    public IngredientBuilder(Ingredient indToCopy) {
        name = indToCopy.getName();
        qty = indToCopy.getQuantity();
        expDate = indToCopy.getExpiryDate().orElse(null);
        tags = indToCopy.getTags();
    }

    /**
     * Sets the {@code Name} of the {@code Ingredient} that we are building.
     */
    public IngredientBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the {@code Quantity} of the {@code Ingredient} that we are building.
     */
    public IngredientBuilder withQuantity(Quantity qty) {
        this.qty = qty;
        return this;
    }

    /**
     * Sets the {@code ExpiryDate} of the {@code Ingredient} that we are building.
     */
    public IngredientBuilder withDate(String date) {
        if (date == null || date.isEmpty()) {
            this.expDate = null;
        } else {
            this.expDate = new ExpiryDate(date);
        }
        return this;
    }

    /**
     * Sets the {@code tags} of the {@code Ingredient} that we are building.
     */
    public IngredientBuilder withTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Ingredient build() {
        return new Ingredient(name, qty, expDate, tags);
    }

}
