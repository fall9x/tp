package chopchop.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.EntryBook;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.recipe.Recipe;

public class JsonSerializableRecipeBook {
    public static final String MESSAGE_DUPLICATE_RECIPE = "Recipe list contains duplicate recipe(s).";

    private final List<JsonAdaptedRecipe> recipes;

    /**
     * Constructs a {@code JsonSerializableRecipeBook} with the given recipes.
     */
    @JsonCreator
    public JsonSerializableRecipeBook(@JsonProperty("recipes") List<JsonAdaptedRecipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    /**
     * Converts a given {@code ReadOnlyRecipeBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableRecipeBook}.
     */
    public JsonSerializableRecipeBook(ReadOnlyEntryBook<Recipe> source) {
        this.recipes = source.getEntryList().stream().map(JsonAdaptedRecipe::new).collect(Collectors.toList());
    }

    /**
     * Converts this recipe book into the model's {@code RecipeBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public EntryBook<Recipe> toModelType() throws IllegalValueException {
        EntryBook<Recipe> recipeBook = new EntryBook<>();
        for (JsonAdaptedRecipe jsonAdaptedRecipe : this.recipes) {
            Recipe recipe = jsonAdaptedRecipe.toModelType();
            if (recipeBook.has(recipe)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_RECIPE);
            }
            recipeBook.add(recipe);
        }
        return recipeBook;
    }
}
