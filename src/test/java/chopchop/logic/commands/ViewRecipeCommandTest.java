package chopchop.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.logic.commands.CommandTestUtil.assertCommandFailure;
import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.logic.commands.CommandTestUtil.showRecipeAtIndex;
import static chopchop.testutil.TypicalReferences.INDEXED_FIRST;
import static chopchop.testutil.TypicalReferences.INDEXED_SECOND;
import static chopchop.testutil.TypicalRecipes.getTypicalRecipeBook;

import org.junit.jupiter.api.Test;

import chopchop.logic.parser.ItemReference;
import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;
import chopchop.model.attributes.NameContainsKeywordsPredicate;
import chopchop.model.recipe.Recipe;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;

import java.util.Arrays;


public class ViewRecipeCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new EntryBook<>(), new UsageList<RecipeUsage>(),
            new UsageList<IngredientUsage>(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        var recToView = model.getFilteredRecipeList().get(INDEXED_FIRST.getZeroIndex());
        var viewCommand = new ViewRecipeCommand(INDEXED_FIRST);

        var expectedModel = new ModelManager(model.getRecipeBook(), new EntryBook<>(), new UsageList<RecipeUsage>(),
                new UsageList<IngredientUsage>(), new UserPrefs());
        expectedModel.findRecipeWithName(recToView.getName());

        assertCommandSuccess(viewCommand, model, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_returnsError() {
        var outOfBoundIndex = ItemReference.ofOneIndex(model.getFilteredRecipeList().size() + 1);
        var viewCommand = new ViewRecipeCommand(outOfBoundIndex);

        assertCommandFailure(viewCommand, model);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showRecipeAtIndex(model, INDEXED_FIRST);

        var recToView = model.getFilteredRecipeList().get(INDEXED_FIRST.getZeroIndex());
        var viewCommand = new ViewRecipeCommand(INDEXED_FIRST);

        var expectedModel = new ModelManager(model.getRecipeBook(), new EntryBook<>(), new UsageList<RecipeUsage>(),
                new UsageList<IngredientUsage>(), new UserPrefs());
        showSpecificRecipe(expectedModel, recToView);
        expectedModel.findRecipeWithName(recToView.getName());

        assertCommandSuccess(viewCommand, model, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_returnsError() {
        showRecipeAtIndex(model, INDEXED_FIRST);

        var outOfBoundIndex = INDEXED_SECOND;
        // ensures that outOfBoundIndex is still in bounds of recipe book list
        assertTrue(outOfBoundIndex.getZeroIndex() < model.getRecipeBook().getEntryList().size());

        var viewCommand = new ViewRecipeCommand(outOfBoundIndex);

        assertCommandFailure(viewCommand, model);
    }

    /**
     * Updates {@code model}'s filtered list to show all recipe.
     * @param model
     */
    private void showSpecificRecipe(Model model, Recipe recipe) {
        model.updateFilteredRecipeList(p -> true);
        final String[] splitName = recipe.getName().split("\\s+");
        model.updateFilteredRecipeList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));
        assertTrue(!model.getFilteredRecipeList().isEmpty());
    }
}
