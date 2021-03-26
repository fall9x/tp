package chopchop.testutil;

import static chopchop.logic.commands.CommandTestUtil.VALID_RECIPE_NAME_APRICOT_SALAD;
import static chopchop.logic.commands.CommandTestUtil.VALID_RECIPE_NAME_BANANA_SALAD;
import static chopchop.logic.commands.CommandTestUtil.VALID_RECIPE_NAME_CUSTARD_SALAD;
import static chopchop.testutil.TypicalIngredients.APRICOT_REF;
import static chopchop.testutil.TypicalIngredients.BANANA_REF;
import static chopchop.testutil.TypicalIngredients.CUSTARD_REF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import chopchop.model.EntryBook;
import chopchop.model.attributes.Step;
import chopchop.model.attributes.Tag;
import chopchop.model.recipe.Recipe;

public class TypicalRecipes {

    public static final Step STEP_APRICOT_SALAD = new Step(
            "Cut the apricot, add salad dressing, and mix well in a bowl.");
    public static final Step STEP_BANANA_SALAD = new Step(
            "Cut the banana, add salad dressing, and mix well in a bowl.");
    public static final Step STEP_CUSTARD_SALAD = new Step(
            "Cut the custard, add salad dressing, and mix well in a bowl.");

    public static final Tag TAG_APRICOT_SALAD = new Tag("Sweet Apricot");
    public static final Tag TAG_BANANA_SALAD = new Tag("Sweet Banana");
    public static final Tag TAG_CUSTARD_SALAD = new Tag("Sweet Custard");
    public static final Tag TAG_ALL_ONE = new Tag("Salad");
    public static final Tag TAG_ALL_TWO = new Tag("Cold Food");

    public static final Recipe APRICOT_SALAD = new RecipeBuilder().withName(VALID_RECIPE_NAME_APRICOT_SALAD)
        .withIngredients(new ArrayList<>(Arrays.asList(APRICOT_REF, CUSTARD_REF)))
        .withSteps(new ArrayList<>(Arrays.asList(STEP_APRICOT_SALAD)))
        .withTags(new HashSet<>(Arrays.asList(TAG_APRICOT_SALAD, TAG_ALL_ONE, TAG_ALL_TWO)))
        .build();

    public static final Recipe BANANA_SALAD = new RecipeBuilder().withName(VALID_RECIPE_NAME_BANANA_SALAD)
        .withIngredients(new ArrayList<>(Arrays.asList(BANANA_REF, CUSTARD_REF)))
        .withSteps(new ArrayList<>(Arrays.asList(STEP_BANANA_SALAD)))
        .withTags(new HashSet<>(Arrays.asList(TAG_BANANA_SALAD, TAG_ALL_ONE, TAG_ALL_TWO)))
        .build();

    public static final Recipe CUSTARD_SALAD = new RecipeBuilder().withName(VALID_RECIPE_NAME_CUSTARD_SALAD)
        .withIngredients(new ArrayList<>(Arrays.asList(CUSTARD_REF)))
        .withSteps(new ArrayList<>(Arrays.asList(STEP_CUSTARD_SALAD)))
        .withTags(new HashSet<>(Arrays.asList(TAG_CUSTARD_SALAD, TAG_ALL_ONE, TAG_ALL_TWO)))
        .build();

    /**
     * Returns an {@code IngredientBook} with all the typical ingredients.
     */
    public static EntryBook<Recipe> getTypicalRecipeBook() {
        EntryBook<Recipe> rb = new EntryBook<>();
        for (Recipe recipe : getTypicalRecipes()) {
            rb.add(recipe);
        }
        return rb;
    }

    public static List<Recipe> getTypicalRecipes() {
        return new ArrayList<>(Arrays.asList(APRICOT_SALAD, BANANA_SALAD));
    }

}
