package chopchop.testutil;

import chopchop.model.recipe.Recipe;
import chopchop.model.attributes.Step;
import chopchop.model.ingredient.IngredientReference;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class RecipeBuilder {

    public static final String DEFAULT_NAME = "Egg";
    public static final String DEFAULT_STEP = "Heat it for 15 minutes.";

    private String name;
    private List<IngredientReference> ingredients;
    private List<Step> steps;

    /**
     * Creates a {@code RecipeBuilder} with the default details.
     */
    public RecipeBuilder() {
        name = DEFAULT_NAME;
        ingredients = new ArrayList<>();
        steps = new ArrayList<>(Arrays.asList(new Step(DEFAULT_STEP)));
    }

    /**
     * Initializes the RecipeBuilder with the data of {@code recToCopy}.
     */
    public RecipeBuilder(Recipe recToCopy) {
        name = recToCopy.getName();
        ingredients = recToCopy.getIngredients();
        steps = recToCopy.getSteps();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public RecipeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public RecipeBuilder withIngredients(List<IngredientReference> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public RecipeBuilder withSteps(List<Step> steps) {
        this.steps = steps;
        return this;
    }

    public Recipe build() {
        return new Recipe(name, ingredients, steps);
    }

}
