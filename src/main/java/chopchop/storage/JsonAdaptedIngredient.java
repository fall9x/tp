package chopchop.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.Name;
import chopchop.model.ingredient.Ingredient;

public class JsonAdaptedIngredient {
    public static final String INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT = "Ingredient's %s field is missing!";

    private final String name;
    private final JsonAdaptedIngredientSet sets;

    /**
     * Constructs a {@code JsonAdaptedIngredient} with the given ingredient details.
     */
    @JsonCreator
    public JsonAdaptedIngredient(@JsonProperty("name") String name,
                                 @JsonProperty("sets") JsonAdaptedIngredientSet sets) {
        this.name = name;
        this.sets = sets;
    }

    /**
     * Converts a given {@code Ingredient} into this class for Jackson use.
     */
    public JsonAdaptedIngredient(Ingredient source) {
        this.name = source.getName();
        this.sets = new JsonAdaptedIngredientSet(source.getIngredientSets());
    }

    /**
     * Converts this Jackson-friendly adapted ingredient object into the model's {@code Ingredient} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted ingredient.
     */
    public Ingredient toModelType() throws IllegalValueException {
        if (this.name == null) {
            throw new IllegalValueException(String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }

        if (this.sets == null) {
            throw new IllegalValueException(String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT, "sets"));
        }

        return new Ingredient(this.name, this.sets.toModelType());
    }
}
