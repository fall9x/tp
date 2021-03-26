package chopchop.logic.commands;

import java.util.Set;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import chopchop.model.EntryBook;
import chopchop.model.ModelStub;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Volume;
import chopchop.model.ingredient.Ingredient;

import org.junit.jupiter.api.Test;
import chopchop.testutil.IngredientBuilder;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddIngredientCommandTest {

    @Test
    public void execute_ingredientAcceptedByModel_addSuccessful() throws Exception {
        var modelStub = new ModelStubAcceptingIngredientAdded();
        var ingr = new IngredientBuilder().build();

        var result = new AddIngredientCommand(ingr.getName(), Optional.of(ingr.getQuantity()),
            ingr.getExpiryDate(), ingr.getTags())
                .execute(modelStub, new CommandTestUtil.HistoryManagerStub());


        assertTrue(result.didSucceed());
        assertEquals(Arrays.asList(ingr), modelStub.ingredientsAdded);
    }

    @Test
    public void add_ingredients_combine() throws Exception {
        var modelStub = new ModelStubAcceptingIngredientAdded();
        var historyStub = new CommandTestUtil.HistoryManagerStub();

        var out = new AddIngredientCommand("milk", Optional.of(Volume.litres(0.7)), Optional.empty(), Set.of())
            .execute(modelStub, historyStub);

        assertTrue(out.didSucceed());

        var c2 = new AddIngredientCommand("MILK", Optional.of(Volume.cups(1)), Optional.empty(), Set.of());
        var out2 = c2.execute(modelStub, historyStub);

        assertTrue(out2.didSucceed());
        c2.undo(modelStub);

        assertEquals(Volume.millilitres(700), modelStub.findIngredientWithName("milk").get().getQuantity());

        var out3 = new AddIngredientCommand("milk", Optional.of(Count.of(7)), Optional.empty(), Set.of())
            .execute(modelStub, historyStub);
        assertTrue(out3.isError());
    }

    /**
     * A Model stub that contains a single ingredient.
     */
    private class ModelStubWithIngredient extends ModelStub {
        private final Ingredient ingredient;

        ModelStubWithIngredient(Ingredient ingredient) {
            requireNonNull(ingredient);
            this.ingredient = ingredient;
        }

        @Override
        public boolean hasIngredient(Ingredient ingredient) {
            requireNonNull(ingredient);
            return this.ingredient.isSame(ingredient);
        }

        @Override
        public Optional<Ingredient> findIngredientWithName(String name) {
            requireNonNull(name);
            return this.ingredient.getName().equalsIgnoreCase(name)
                ? Optional.of(this.ingredient)
                : Optional.empty();
        }
    }

    /**
     * A Model stub that always accepts the ingredient being added.
     */
    private static class ModelStubAcceptingIngredientAdded extends ModelStub {
        final ArrayList<Ingredient> ingredientsAdded = new ArrayList<>();

        @Override
        public boolean hasIngredient(Ingredient ingredient) {
            requireNonNull(ingredient);
            return ingredientsAdded.stream().anyMatch(ingredient::isSame);
        }

        @Override
        public void addIngredient(Ingredient ingredient) {
            requireNonNull(ingredient);
            ingredientsAdded.add(ingredient);
        }

        @Override
        public Optional<Ingredient> findIngredientWithName(String name) {
            requireNonNull(name);
            return this.ingredientsAdded
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst();
        }

        @Override
        public void setIngredient(Ingredient target, Ingredient editedIngredient) {
            var i = this.ingredientsAdded.indexOf(target);
            if (i == -1) {
                throw new NoSuchElementException("ingredient not found");
            }

            this.ingredientsAdded.set(i, editedIngredient);
        }

        @Override
        public ReadOnlyEntryBook<Ingredient> getIngredientBook() {
            return new EntryBook<>();
        }
    }

}
