package chopchop.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.usage.RecipeUsage;

public class JsonAdaptedRecipeUsage {
    public static final String USAGE_MISSING_FIELD_MESSAGE_FORMAT = "Usage's %s field is missing!";

    private final String name;
    private final String date;

    /**
     * Constructs a {@code JsonAdaptedRecipeUsage} with the given command
     */
    @JsonCreator
    public JsonAdaptedRecipeUsage(@JsonProperty("name") String name,
                                  @JsonProperty("date") String date) {
        this.name = name;
        this.date = date;
    }

    /**
     * Converts a given {@code RecipeUsage} into this class for Jackson use.
     */
    public JsonAdaptedRecipeUsage(RecipeUsage recipeUsage) {
        this.name = recipeUsage.getName();
        this.date = recipeUsage.getDate().toString();
    }

    /**
     * Converts this Jackson-friendly adapted Recipe Usage into its original object.
     */
    public RecipeUsage toModelType() throws IllegalValueException {
        if (this.name == null) {
            throw new IllegalValueException(String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        if (this.date == null) {
            throw new IllegalValueException(String.format(USAGE_MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        return new RecipeUsage(this.name, LocalDateTime.parse(this.date));
    }
}
