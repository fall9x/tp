package chopchop.testutil;

import chopchop.model.EntryBook;
import chopchop.model.ingredient.Ingredient;

public class IngredientBookBuilder {

    private EntryBook<Ingredient> ingredientBook;

    public IngredientBookBuilder() {
        ingredientBook = new EntryBook<>();
    }

    public IngredientBookBuilder(EntryBook<Ingredient> ingredientBook) {
        this.ingredientBook = ingredientBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public IngredientBookBuilder withIngredient(Ingredient ind) {
        ingredientBook.add(ind);
        return this;
    }

    public EntryBook<Ingredient> build() {
        return ingredientBook;
    }
}
