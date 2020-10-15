package chopchop.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Quantity;
import chopchop.model.ingredient.Ingredient;
import chopchop.util.Result;

public class JsonAdaptedIngredient {
    public static final String IND_MISSING_FIELD_MESSAGE_FORMAT = "Ingredients's %s field is missing!";

    private final String name;
    private final String expiry;
    private final String qty;

    /**
     * Constructs a {@code JsonAdaptedIngredient} with the given ingredient details.
     */
    @JsonCreator
    public JsonAdaptedIngredient(@JsonProperty("name") String name, @JsonProperty("quantity") String qty,
                             @JsonProperty("expiry") String expiry) {
        this.name = name;
        this.qty = qty;
        this.expiry = expiry;
    }

    /**
     * Converts a given {@code Ingredient} into this class for Jackson use.
     */
    public JsonAdaptedIngredient(Ingredient source) {
        name = source.getName().fullName;
        qty = source.getQuantity().toString();
        expiry = source.getExpiryDate().isPresent()
            ? source.getExpiryDate().get().toString()
            : null;
    }

    /**
     * Converts this Jackson-friendly adapted ingredient object into the model's {@code Ingredient} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted ingredient.
     */
    public Ingredient toModelType() throws IllegalValueException {

        if (name == null) {
            throw new IllegalValueException(String.format(IND_MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (expiry == null) {
            throw new IllegalValueException(String.format(IND_MISSING_FIELD_MESSAGE_FORMAT,
                ExpiryDate.class.getSimpleName()));
        }
        if (!ExpiryDate.isValidDate(expiry)) {
            throw new IllegalValueException(ExpiryDate.MESSAGE_CONSTRAINTS);
        }
        final ExpiryDate modelExpiry = new ExpiryDate(expiry);

        if (qty == null) {
            throw new IllegalValueException(String.format(IND_MISSING_FIELD_MESSAGE_FORMAT,
                Quantity.class.getSimpleName()));
        }
        Result<Quantity> qtyResult = Quantity.parse(qty);
        if (qtyResult.isError()) {
            throw new IllegalValueException(qtyResult.getError());
        }

        return new Ingredient(modelName, qtyResult.getValue(), modelExpiry);
    }
}
