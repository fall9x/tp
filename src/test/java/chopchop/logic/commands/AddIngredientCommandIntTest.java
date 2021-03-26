package chopchop.logic.commands;

import static chopchop.logic.commands.CommandTestUtil.assertCommandSuccess;
import static chopchop.testutil.TypicalIngredients.getTypicalIngredientBook;

import java.util.Optional;

import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;
import chopchop.testutil.IngredientBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AddIngredientCommandIntTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new EntryBook<>(), getTypicalIngredientBook(), new UsageList<RecipeUsage>(),
            new UsageList<IngredientUsage>(), new UserPrefs());
    }

    @Test
    public void execute_newIngredient_success() {
        var validIngredient = new IngredientBuilder().build();

        Model expectedModel = new ModelManager(new EntryBook<>(), model.getIngredientBook(),
            new UsageList<RecipeUsage>(), new UsageList<IngredientUsage>(), new UserPrefs());
        expectedModel.addIngredient(validIngredient);

        assertCommandSuccess(new AddIngredientCommand(
            validIngredient.getName(),
            Optional.of(validIngredient.getQuantity()),
            validIngredient.getExpiryDate(),
            validIngredient.getTags()
        ), model, expectedModel);
    }
}
