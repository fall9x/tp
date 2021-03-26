package chopchop.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Tag;
import chopchop.model.ingredient.Ingredient;

public class JsonAdaptedIngredient {
    public static final String INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT = "Ingredient's %s field is missing!";

    private final String name;
    private final JsonAdaptedIngredientSet sets;
    private final List<String> tags;

    /**
     * Constructs a {@code JsonAdaptedIngredient} with the given ingredient details.
     */
    @JsonCreator
    public JsonAdaptedIngredient(@JsonProperty("name") String name,
                                 @JsonProperty("sets") JsonAdaptedIngredientSet sets,
                                 @JsonProperty("tags") List<String> tags) {
        this.name = name;
        this.sets = sets;
        this.tags = tags == null ? null : new ArrayList<>(tags);
    }

    /**
     * Converts a given {@code Ingredient} into this class for Jackson use.
     */
    public JsonAdaptedIngredient(Ingredient source) {
        this.name = source.getName();
        this.sets = new JsonAdaptedIngredientSet(source.getIngredientSets());
        this.tags = source.getTags().stream().map(Tag::toString).collect(Collectors.toList());
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

        if (this.tags == null) {
            throw new IllegalValueException(String.format(INGREDIENT_MISSING_FIELD_MESSAGE_FORMAT,
                    Tag.class.getSimpleName()));
        }
        Set<Tag> modelTags = new HashSet<>();
        for (String tag : this.tags) {
            modelTags.add(new Tag(tag));
        }

        return new Ingredient(this.name, this.sets.toModelType(), modelTags);
    }
}
