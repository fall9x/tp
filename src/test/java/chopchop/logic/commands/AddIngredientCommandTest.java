package chopchop.logic.commands;

import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import chopchop.logic.commands.exceptions.CommandException;

import chopchop.model.ModelStub;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.units.Volume;

import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientBook;
import chopchop.model.ingredient.ReadOnlyIngredientBook;

import org.junit.jupiter.api.Test;
import chopchop.testutil.IngredientBuilder;

import static java.util.Objects.requireNonNull;
import static chopchop.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddIngredientCommandTest {

    @Test
    public void constructor_nullIngredient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddIngredientCommand(null));
    }

    @Test
    public void execute_ingredientAcceptedByModel_addSuccessful() throws Exception {
        var modelStub = new ModelStubAcceptingIngredientAdded();
        var validIngredient = new IngredientBuilder().build();

        var commandResult = new AddIngredientCommand(validIngredient).execute(modelStub);

        assertEquals(String.format(AddIngredientCommand.MESSAGE_SUCCESS, validIngredient),
            commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validIngredient), modelStub.ingredientsAdded);
    }

    @Test
    public void execute_duplicateIngredient_throwsCommandException() {
        var validIngredient = new IngredientBuilder().build();
        var addCommand = new AddIngredientCommand(validIngredient);
        var modelStub = new ModelStubWithIngredient(validIngredient);

        assertThrows(CommandException.class,
            AddIngredientCommand.MESSAGE_DUPLICATE_INGREDIENT, () -> addCommand.execute(modelStub)
        );
    }

    @Test
    public void add_ingredients_combine() throws Exception {
        var milk1 = new IngredientBuilder().withName("milk").withQuantity(Volume.litres(0.7)).build();
        var milk2 = new IngredientBuilder().withName("MILK").withQuantity(Volume.cups(1)).build();
        var milk3 = new IngredientBuilder().withName("milk").withQuantity(Volume.millilitres(950)).build();

        var modelStub = new ModelStubAcceptingIngredientAdded();

        assertEquals(String.format(AddIngredientCommand.MESSAGE_SUCCESS, milk1),
            new AddIngredientCommand(milk1).execute(modelStub).getFeedbackToUser());

        var out2 = new AddIngredientCommand(milk2).execute(modelStub).getFeedbackToUser();
        System.err.println(out2);

        assertEquals(String.format(AddIngredientCommand.MESSAGE_COMBINED, milk3), out2);
    }

    @Test
    public void equals() {
        var apple = new IngredientBuilder().withName("Apple").build();
        var banana = new IngredientBuilder().withName("Banana").build();
        var addAppleCommand = new AddIngredientCommand(apple);
        var addBananaCommand = new AddIngredientCommand(banana);

        // same object -> returns true
        assertTrue(addAppleCommand.equals(addAppleCommand));

        // same values -> returns true
        var addAliceCommandCopy = new AddIngredientCommand(apple);
        assertTrue(addAppleCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAppleCommand.equals(1));

        // null -> returns false
        assertFalse(addAppleCommand.equals(null));

        // different ingredient -> returns false
        assertFalse(addAppleCommand.equals(addBananaCommand));
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
            return this.ingredient.equals(ingredient);
        }

        @Override
        public Optional<Ingredient> findIngredientWithName(Name name) {
            requireNonNull(name);
            return this.ingredient.getName().equals(name)
                ? Optional.of(this.ingredient)
                : Optional.empty();
        }
    }

    /**
     * A Model stub that always accept the ingredient being added.
     */
    private class ModelStubAcceptingIngredientAdded extends ModelStub {
        final ArrayList<Ingredient> ingredientsAdded = new ArrayList<>();

        @Override
        public boolean hasIngredient(Ingredient ingredient) {
            requireNonNull(ingredient);
            return ingredientsAdded.stream().anyMatch(ingredient::equals);
        }

        @Override
        public void addIngredient(Ingredient ingredient) {
            requireNonNull(ingredient);
            ingredientsAdded.add(ingredient);
        }

        @Override
        public Optional<Ingredient> findIngredientWithName(Name name) {
            requireNonNull(name);
            return this.ingredientsAdded
                .stream()
                .filter(i -> i.getName().equals(name))
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
        public ReadOnlyIngredientBook getIngredientBook() {
            return new IngredientBook();
        }
    }

}
