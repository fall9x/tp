package chopchop.testutil;

import chopchop.model.EntryBook;
import chopchop.model.recipe.Recipe;

public class RecipeBookBuilder {

    private EntryBook<Recipe> recipeBook;

    public RecipeBookBuilder() {
        recipeBook = new EntryBook<>();
    }

    public RecipeBookBuilder(EntryBook<Recipe> recipeBook) {
        this.recipeBook = recipeBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public RecipeBookBuilder withRecipe(Recipe recipe) {
        recipeBook.add(recipe);
        return this;
    }

    public EntryBook<Recipe> build() {
        return recipeBook;
    }
}
