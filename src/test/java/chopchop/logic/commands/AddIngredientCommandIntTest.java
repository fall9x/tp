package chopchop.logic.commands;

import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UserPrefs;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.recipe.RecipeBook;
import chopchop.testutil.IngredientBuilder;

public class AddIngredientCommandIntTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new RecipeBook(), getTypicalIngredientBook(), new UserPrefs());
    }

    @Test
    public void execute_newIngredient_success() {
        Ingredient validIngredient = new IngredientBuilder().build();

        Model expectedModel = new ModelManager(new RecipeBook(), model.getIngredientBook(), new UserPrefs());
        expectedModel.addIngredient(validIngredient);

        assertCommandSuccess(new AddIngredientCommand(validIngredient), model,
            String.format(AddIngredientCommand.MESSAGE_SUCCESS, validIngredient), expectedModel);
    }
}
