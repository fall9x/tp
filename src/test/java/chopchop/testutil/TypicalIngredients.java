package chopchop.testutil;

import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_EXPIRY_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_EXPIRY_BANANA;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_BANANA;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_QTY_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_QTY_BANANA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientBook;
import chopchop.model.ingredient.IngredientReference;

public class TypicalIngredients {

    public static final Ingredient APRICOT = new IngredientBuilder().withName(VALID_INGREDIENT_NAME_APRICOT)
        .withQuantity(VALID_INGREDIENT_QTY_APRICOT).withDate(VALID_INGREDIENT_EXPIRY_APRICOT)
        .build();
    public static final Ingredient BANANA = new IngredientBuilder().withName(VALID_INGREDIENT_NAME_BANANA)
        .withDate(VALID_INGREDIENT_EXPIRY_BANANA).withQuantity(VALID_INGREDIENT_QTY_BANANA)
        .build();

    public static final IngredientReference APRICOT_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_APRICOT,
        Count.of(VALID_INGREDIENT_QTY_APRICOT)
    );

    public static final IngredientReference BANANA_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_BANANA,
        Count.of(VALID_INGREDIENT_QTY_BANANA)
    );

    /**
     * Returns an {@code IngredientBook} with all the typical ingredients.
     */
    public static IngredientBook getTypicalIngredientBook() {
        IngredientBook ab = new IngredientBook();
        for (Ingredient ind : getTypicalIngredients()) {
            ab.addIngredient(ind);
        }
        return ab;
    }

    public static List<Ingredient> getTypicalIngredients() {
        return new ArrayList<>(Arrays.asList(APRICOT, BANANA));
    }

    public static List<IngredientReference> getTypicalIngredientReferences() {
        return new ArrayList<>(Arrays.asList(APRICOT_REF, BANANA_REF));
    }
}
