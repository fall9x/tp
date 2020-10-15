package chopchop.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientBook;
import chopchop.model.ingredient.ReadOnlyIngredientBook;

public class JsonSerializableIngredientBook {

    public static final String MESSAGE_DUPLICATE_INGREDIENT = "Ingredient list contains duplicate ingredient(s).";

    private final List<JsonAdaptedIngredient> ingredients = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableIngredientBook} with the given inds.
     */
    @JsonCreator
    public JsonSerializableIngredientBook(@JsonProperty("ingredients") List<JsonAdaptedIngredient> inds) {
        this.ingredients.addAll(inds);
    }

    /**
     * Converts a given {@code ReadOnlyIngredientBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableIngredientBook}.
     */
    public JsonSerializableIngredientBook(ReadOnlyIngredientBook source) {
        ingredients.addAll(source.getFoodEntryList().stream().map(JsonAdaptedIngredient::new)
            .collect(Collectors.toList()));
    }

    /**
     * Converts this ingredient book into the model's {@code IngredientBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public IngredientBook toModelType() throws IllegalValueException {
        IngredientBook indBook = new IngredientBook();
        for (JsonAdaptedIngredient jsonAdaptedIngredient : ingredients) {
            Ingredient ind = jsonAdaptedIngredient.toModelType();
            if (indBook.hasIngredient(ind)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_INGREDIENT);
            }
            indBook.addIngredient(ind);
        }
        return indBook;
    }
}
