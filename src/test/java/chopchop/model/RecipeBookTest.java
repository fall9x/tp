package chopchop.model;

import chopchop.model.exceptions.DuplicateEntryException;
import chopchop.model.recipe.Recipe;
import chopchop.testutil.RecipeBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import static chopchop.testutil.Assert.assertThrows;
import static chopchop.testutil.TypicalIngredients.BANANA_REF;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;
import static chopchop.testutil.TypicalRecipes.STEP_BANANA_SALAD;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class RecipeBookTest {

    private final EntryBook<Recipe> recipeBook = new EntryBook<>();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), recipeBook.getEntryList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recipeBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyRecipeBook_replacesData() {
        EntryBook<Recipe> newData = getTypicalRecipeBook();
        recipeBook.resetData(newData);
        assertEquals(newData, recipeBook);
    }

    @Test
    public void resetData_withDuplicateRecipes_throwsDuplicateRecipeException() {
        // Two recipes with the same identity fields
        Recipe editedRecipe = new RecipeBuilder(APRICOT_SALAD).build();
        List<Recipe> newRecipes = Arrays.asList(APRICOT_SALAD, editedRecipe);
        RecipeBookTest.RecipeBookStub newData = new RecipeBookTest.RecipeBookStub(newRecipes);

        assertThrows(DuplicateEntryException.class, () -> recipeBook.resetData(newData));
    }

    @Test
    public void hasRecipe_nullRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recipeBook.has(null));
    }

    @Test
    public void hasRecipe_recipeNotInRecipeBook_returnsFalse() {
        assertFalse(recipeBook.has(APRICOT_SALAD));
    }

    @Test
    public void hasRecipe_recipeInRecipeBook_returnsTrue() {
        recipeBook.add(APRICOT_SALAD);
        assertTrue(recipeBook.has(APRICOT_SALAD));
    }

    @Test
    public void hasRecipe_recipeWithSameIdentityFieldsInRecipeBook_returnsTrue() {
        recipeBook.add(APRICOT_SALAD);
        Recipe editedRecipe = new RecipeBuilder(APRICOT_SALAD)
            .withIngredients(new ArrayList<>(Arrays.asList(BANANA_REF)))
            .withSteps(new ArrayList<>(Arrays.asList(STEP_BANANA_SALAD)))
            .build();
        assertTrue(recipeBook.has(editedRecipe)); //Both identity fields must be equal
    }

    @Test
    public void getRecipeList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> recipeBook.getEntryList().remove(0));
    }

    /**
     * A stub ReadOnlyRecipeBook whose recipes list can violate interface constraints.
     */
    private static class RecipeBookStub implements ReadOnlyEntryBook<Recipe> {
        private final ObservableList<Recipe> recipes = FXCollections.observableArrayList();

        RecipeBookStub(Collection<Recipe> recipes) {
            this.recipes.setAll(recipes);
        }

        @Override
        public ObservableList<Recipe> getEntryList() {
            return recipes;
        }
    }

}
