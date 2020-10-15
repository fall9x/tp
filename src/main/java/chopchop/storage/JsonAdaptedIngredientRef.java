package chopchop.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Quantity;
import chopchop.model.ingredient.IngredientReference;
import chopchop.util.Result;

public class JsonAdaptedIngredientRef {

    public static final String REF_MISSING_FIELD_MESSAGE_FORMAT = "Ingredient ref's %s field is missing!";

    private final String name;
    private final String quantity;

    /**
     * Constructs a {@code JsonAdaptedIngredientRef} with the given details.
     *
     * @param name name of ingredient.
     * @param quantity just quantity to string.
     */
    @JsonCreator
    public JsonAdaptedIngredientRef(@JsonProperty("name") String name,
                                    @JsonProperty("quantity") String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * Converts a given {@code IngredientReference} into this class for json use.
     * @param source
     */
    public JsonAdaptedIngredientRef(IngredientReference source) {
        this.name = source.getName();
        this.quantity = source.getQuantity().toString();
    }

    /**
     * Converts this into json-friendly adapted IngredientReference object into the model's.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted reference.
     */
    public IngredientReference toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(REF_MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName()));
        }
        if (quantity == null) {
            throw new IllegalValueException(String.format(REF_MISSING_FIELD_MESSAGE_FORMAT,
                Quantity.class.getSimpleName()));
        }
        Result<Quantity> result = Quantity.parse(quantity);
        if (result.isError()) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new IngredientReference(name, result.getValue());
    }


}
