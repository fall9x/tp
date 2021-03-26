package chopchop.model;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;
import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.TAG_APRICOT;
import static chopchop.testutil.TypicalIngredients.TAG_ALL_ONE;
import static chopchop.testutil.TypicalIngredients.TAG_ALL_TWO;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_QTY_BANANA;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_EXPIRY_BANANA;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import chopchop.model.exceptions.DuplicateEntryException;
import chopchop.testutil.IngredientBuilder;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.attributes.units.Count;

public class IngredientBookTest {

    private final EntryBook<Ingredient> ingredientBook = new EntryBook<>();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), ingredientBook.getEntryList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ingredientBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyIngredientBook_replacesData() {
        EntryBook<Ingredient> newData = getTypicalIngredientBook();
        ingredientBook.resetData(newData);
        assertEquals(newData, ingredientBook);
    }

    @Test
    public void resetData_withDuplicateIngredients_throwsDuplicateIngredientException() {
        // Two ingredients with the same identity fields
        Ingredient editedAlice = new IngredientBuilder(APRICOT)
            .withQuantity(Count.of(VALID_INGREDIENT_QTY_BANANA))
            .build();
        List<Ingredient> newIngredients = Arrays.asList(APRICOT, editedAlice);
        IngredientBookTest.IngredientBookStub newData = new IngredientBookTest.IngredientBookStub(newIngredients);

        assertThrows(DuplicateEntryException.class, () -> ingredientBook.resetData(newData));
    }

    @Test
    public void hasIngredient_nullIngredient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ingredientBook.has(null));
    }

    @Test
    public void hasIngredient_ingredientNotInIngredientBook_returnsFalse() {
        assertFalse(ingredientBook.has(APRICOT));
    }

    @Test
    public void hasIngredient_ingredientInIngredientBook_returnsTrue() {
        ingredientBook.add(APRICOT);
        assertTrue(ingredientBook.has(APRICOT));
    }

    @Test
    public void hasIngredient_ingredientWithSameIdentityFieldsInIngredientBook_returnsTrue() {
        ingredientBook.add(APRICOT);
        Ingredient editedAlice = new IngredientBuilder(APRICOT)
            .withQuantity(Count.of(VALID_INGREDIENT_QTY_BANANA))
            .withDate(VALID_INGREDIENT_EXPIRY_BANANA)
            .withTags(new HashSet<>(Arrays.asList(TAG_APRICOT, TAG_ALL_ONE, TAG_ALL_TWO)))
            .build();
        assertTrue(ingredientBook.has(editedAlice)); //Both identity fields must be equal
    }

    @Test
    public void getIngredientList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> ingredientBook.getEntryList().remove(0));
    }

    /**
     * A stub ReadOnlyIngredientBook whose persons list can violate interface constraints.
     */
    private static class IngredientBookStub implements ReadOnlyEntryBook<Ingredient> {
        private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

        IngredientBookStub(Collection<Ingredient> ingredients) {
            this.ingredients.setAll(ingredients);
        }

        @Override
        public ObservableList<Ingredient> getEntryList() {
            return this.ingredients;
        }
    }
}
