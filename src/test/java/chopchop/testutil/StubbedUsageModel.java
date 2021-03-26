package chopchop.testutil;

import java.time.LocalDateTime;
import java.util.List;
import chopchop.commons.util.Pair;
import chopchop.model.EntryBook;
import chopchop.model.ModelStub;
import chopchop.model.UsageList;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.recipe.Recipe;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;

/**
 * Stubbed model that deal solely with stats methods.
 */
public class StubbedUsageModel extends ModelStub {
    private final EntryBook<Recipe> recipes;
    private final EntryBook<Ingredient> ingredients;
    private final UsageList<RecipeUsage> recipeUsageList = new UsageList<>();
    private final UsageList<IngredientUsage> ingredientUsageList = new UsageList<>();

    private StubbedUsageModel(boolean isEmpty) {
        this.recipes = new EntryBook<>();
        this.ingredients = new EntryBook<>();


        if (!isEmpty) {
            this.recipes.setAll(List.of(
                TypicalRecipes.APRICOT_SALAD,
                TypicalRecipes.BANANA_SALAD
            ));

            this.ingredients.setAll(List.of(
                TypicalIngredients.APRICOT,
                TypicalIngredients.BANANA
            ));

            this.recipeUsageList.setAll(TypicalUsages.getRecipeUsageList());

            this.ingredientUsageList.setAll(TypicalUsages.getIngredientUsageList());
        }
    }

    /**
     * Constructs a totally empty model.
     */
    public static StubbedUsageModel empty() {
        return new StubbedUsageModel(true);
    }

    /**
     * Constructs a stubbed model with Ingredients, Recipes and both usages.
     * Usages are in chronological order. (Expected to be this way cuz they are
     * supposed to be stacks.
     */
    public static StubbedUsageModel filled() {
        return new StubbedUsageModel(false);
    }

    /**
     * Return the List sorted by most made recipe.
     */
    @Override
    public List<Pair<String, String>> getMostMadeRecipeList() {
        return this.recipeUsageList.getMostUsed();
    }

    public void addRecipeUsage(RecipeUsage ru) {
        this.recipeUsageList.add(ru);
    }

    @Override
    public void addRecipeUsage(Recipe recipe) {
    }

    @Override
    public void removeRecipeUsage(Recipe recipe) {
        this.recipeUsageList.pop(recipe.getName());
    }

    @Override
    public void addIngredientUsage(IngredientReference ingredient) {
    }

    public void addIngredientUsage(IngredientUsage iu) {
        this.ingredientUsageList.add(iu);
    }

    @Override
    public void removeIngredientUsage(IngredientReference ingredient) {
        this.ingredientUsageList.pop(ingredient.getName());
    }

    @Override
    public UsageList<RecipeUsage> getRecipeUsageList() {
        return this.recipeUsageList;
    }

    @Override
    public UsageList<IngredientUsage> getIngredientUsageList() {
        return this.ingredientUsageList;
    }

    @Override
    public void setRecipeUsageList(UsageList<RecipeUsage> ul) {
        this.recipeUsageList.setAll(ul);
    }

    @Override
    public void setIngredientUsageList(UsageList<IngredientUsage> ul) {
        this.ingredientUsageList.setAll(ul);
    }

    @Override
    public List<Pair<String, String>> getRecentlyUsedRecipes(int n) {
        return this.recipeUsageList.getRecentlyUsed(n);
    }

    @Override
    public List<Pair<String, String>> getRecentlyUsedIngredients(int n) {
        return this.ingredientUsageList.getRecentlyUsed(n);
    }

    @Override
    public List<Pair<String, String>> getRecipesMadeBetween(LocalDateTime after, LocalDateTime before) {
        return this.recipeUsageList.getUsagesBetween(after, before);
    }

    @Override
    public List<Pair<String, String>> getIngredientsUsedBetween(LocalDateTime after, LocalDateTime before) {
        return this.ingredientUsageList.getUsagesBetween(after, before);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof StubbedUsageModel)) {
            return false;
        }

        StubbedUsageModel other = (StubbedUsageModel) obj;
        return this.recipes.equals(other.recipes)
            && this.ingredients.equals(other.ingredients)
            && this.ingredientUsageList.equals(other.ingredientUsageList)
            && this.recipeUsageList.equals(other.recipeUsageList);
    }
}
