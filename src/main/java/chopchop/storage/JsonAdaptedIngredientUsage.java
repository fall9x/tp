package chopchop.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.Quantity;
import chopchop.model.usage.IngredientUsage;

public class JsonAdaptedIngredientUsage {
    public static final String USAGE_MISSING_FIELD_MESSAGE_FORMAT = "Usage's %s field is missing!";

    private final String name;
    private final String date;
    private final String qty;

    /**
     * Constructs a {@code JsonAdaptedIngredientUsage} with the given command
     */
    @JsonCreator
    public JsonAdaptedIngredientUsage(@JsonProperty("name") String name,
                                  @JsonProperty("date") String date,
                                  @JsonProperty("quantity") String qty) {
        this.name = name;
        this.date = date;
        this.qty = qty;
    }

    /**
     * Converts a given {@code IngredientUsage} into this class for Jackson use.
     */
    public JsonAdaptedIngredientUsage(IngredientUsage ingredientUsage) {
        this.name = ingredientUsage.getName();
        this.date = ingredientUsage.getDate().toString();
        this.qty = ingredientUsage.getQty().toString();
    }

    /**
     * Converts this Jackson-friendly adapted Ingredient Usage into its original object.
     */
    public IngredientUsage toModelType() throws IllegalValueException {
        if (this.name == null) {
            throw new IllegalValueException(String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        if (this.date == null) {
            throw new IllegalValueException(String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        if (this.qty == null) {
            throw new IllegalValueException(String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "quantity"));
        }

        var result = Quantity.parse(qty);
        if (result.isError()) {
            throw new IllegalValueException(result.getError());
        }
        return new IngredientUsage(this.name, LocalDateTime.parse(this.date), result.getValue());
    }
}
