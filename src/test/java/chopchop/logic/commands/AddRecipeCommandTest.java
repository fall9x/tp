package chopchop.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import chopchop.testutil.RecipeBuilder;
import chopchop.testutil.StubbedModel;
import chopchop.testutil.TypicalRecipes;
import chopchop.testutil.TypicalIngredients;
import org.junit.jupiter.api.Test;

public class AddRecipeCommandTest {

    @Test
    public void execute_recipeAcceptedByModel_addSuccessful() {

        var m = StubbedModel.empty();
        var validRecipe = new RecipeBuilder().build();

        var cmd = new AddRecipeCommand(validRecipe.getName(),
            validRecipe.getIngredients(),
            validRecipe.getSteps(),
            validRecipe.getTags()
        );

        var r1 = cmd.execute(m, new CommandTestUtil.HistoryManagerStub());

        assertTrue(r1.didSucceed());
        assertEquals(List.of(validRecipe), m.getRecipeBook().getEntryList());

        var r2 = cmd.undo(m);
        assertTrue(r2.didSucceed());
        assertTrue(m.getRecipeBook().getEntryList().isEmpty());
    }

    @Test
    public void test_duplicateRecipe() {

        var m = StubbedModel.filled();
        var validRecipe = TypicalRecipes.APRICOT_SALAD;

        var cmd = new AddRecipeCommand(validRecipe.getName(),
            validRecipe.getIngredients(),
            validRecipe.getSteps(),
            validRecipe.getTags()
        );

        var r1 = cmd.execute(m, new CommandTestUtil.HistoryManagerStub());
        assertTrue(r1.isError());
    }

    @Test
    public void test_duplicateIngredient() {

        var m = StubbedModel.filled();
        var validRecipe = new RecipeBuilder().withIngredients(List.of(
            TypicalIngredients.APRICOT_REF,
            TypicalIngredients.CUSTARD_REF,
            TypicalIngredients.APRICOT_REF
        )).build();

        var cmd = new AddRecipeCommand(validRecipe.getName(),
            validRecipe.getIngredients(),
            validRecipe.getSteps(),
            validRecipe.getTags()
        );

        var r1 = cmd.execute(m, new CommandTestUtil.HistoryManagerStub());
        assertTrue(r1.isError());
    }
}
