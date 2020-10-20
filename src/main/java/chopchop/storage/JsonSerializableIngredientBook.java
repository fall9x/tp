package chopchop.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.EntryBook;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.ingredient.Ingredient;

public class JsonSerializableIngredientBook {
    public static final String MESSAGE_DUPLICATE_INGREDIENT = "Ingredient list contains duplicate ingredient(s).";

    private final List<JsonAdaptedIngredient> ingredients;

    /**
     * Constructs a {@code JsonSerializableIngredientBook} with the given inds.
     */
    @JsonCreator
    public JsonSerializableIngredientBook(@JsonProperty("ingredients") List<JsonAdaptedIngredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    /**
     * Converts a given {@code ReadOnlyIngredientBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableIngredientBook}.
     */
    public JsonSerializableIngredientBook(ReadOnlyEntryBook<Ingredient> source) {
        this.ingredients = source.getEntryList().stream().map(JsonAdaptedIngredient::new).collect(Collectors.toList());
    }

    /**
     * Converts this ingredient book into the model's {@code IngredientBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public EntryBook<Ingredient> toModelType() throws IllegalValueException {
        EntryBook<Ingredient> ingredientBook = new EntryBook<>();
        for (JsonAdaptedIngredient jsonAdaptedIngredient : this.ingredients) {
            Ingredient ingredient = jsonAdaptedIngredient.toModelType();
            if (ingredientBook.has(ingredient)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_INGREDIENT);
            }
            ingredientBook.add(ingredient);
        }
        return ingredientBook;
    }
}
