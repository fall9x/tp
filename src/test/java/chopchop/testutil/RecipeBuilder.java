package chopchop.testutil;

import chopchop.model.attributes.Tag;
import chopchop.model.recipe.Recipe;
import chopchop.model.attributes.Step;
import chopchop.model.ingredient.IngredientReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RecipeBuilder {

    public static final String DEFAULT_NAME = "Egg";
    public static final String DEFAULT_INGREDIENT_NAME = "Egg";
    public static final String DEFAULT_STEP = "Heat it for 15 minutes.";
    public static final String DEFAULT_TAG = "simple";

    private String name;
    private List<IngredientReference> ingredients;
    private List<Step> steps;
    private Set<Tag> tags;

    /**
     * Creates a {@code RecipeBuilder} with the default details.
     */
    public RecipeBuilder() {
        name = DEFAULT_NAME;
        ingredients = new ArrayList<>(
            Arrays.asList(new IngredientReference(DEFAULT_INGREDIENT_NAME, Optional.empty())));
        steps = new ArrayList<>(Arrays.asList(new Step(DEFAULT_STEP)));
        tags = new HashSet<>(Arrays.asList(new Tag(DEFAULT_TAG)));
    }

    /**
     * Initializes the RecipeBuilder with the data of {@code recToCopy}.
     */
    public RecipeBuilder(Recipe recToCopy) {
        name = recToCopy.getName();
        ingredients = recToCopy.getIngredients();
        steps = recToCopy.getSteps();
        tags = recToCopy.getTags();
    }

    /**
     * Sets the name of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the lists of ingredients of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withIngredients(List<IngredientReference> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    /**
     * Sets the list of steps of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withSteps(List<Step> steps) {
        this.steps = steps;
        return this;
    }

    /**
     * Sets the list of tags of the {@code Recipe} that we are building.
     */
    public RecipeBuilder withTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Recipe build() {
        return new Recipe(name, ingredients, steps, tags);
    }

}
