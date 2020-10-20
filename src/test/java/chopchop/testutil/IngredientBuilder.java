package chopchop.testutil;

import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.attributes.units.Count;

public class IngredientBuilder {

    public static final String DEFAULT_NAME = "Egg";
    public static final int DEFAULT_QTY = 3;
    public static final String DEFAULT_EXPIRY = "2020-10-12";

    private String name;
    private Quantity qty;
    private ExpiryDate expDate;


    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public IngredientBuilder() {
        qty     = Count.of(DEFAULT_QTY);
        name    = DEFAULT_NAME;
        expDate = new ExpiryDate(DEFAULT_EXPIRY);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public IngredientBuilder(Ingredient indToCopy) {
        name = indToCopy.getName();
        qty = indToCopy.getQuantity();
        expDate = indToCopy.getExpiryDate().orElse(null);

    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public IngredientBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public IngredientBuilder withQuantity(Quantity qty) {
        this.qty = qty;
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public IngredientBuilder withDate(String date) {
        this.expDate = new ExpiryDate(date);
        return this;
    }

    public Ingredient build() {
        return new Ingredient(name, qty, expDate);
    }

}
