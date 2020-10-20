package chopchop.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chopchop.model.EntryBook;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.attributes.units.Count;
import chopchop.model.ingredient.IngredientReference;

import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_EXPIRY_CUSTARD;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_CUSTARD;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_QTY_BANANA;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_QTY_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_BANANA;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_NAME_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_EXPIRY_BANANA;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_EXPIRY_APRICOT;
import static chopchop.logic.commands.CommandTestUtil.VALID_INGREDIENT_QTY_CUSTARD;

public class TypicalIngredients {

    public static final Ingredient APRICOT = new IngredientBuilder()
        .withName(VALID_INGREDIENT_NAME_APRICOT)
        .withDate(VALID_INGREDIENT_EXPIRY_APRICOT)
        .withQuantity(Count.of(VALID_INGREDIENT_QTY_APRICOT))
        .build();

    public static final Ingredient BANANA = new IngredientBuilder()
        .withName(VALID_INGREDIENT_NAME_BANANA)
        .withDate(VALID_INGREDIENT_EXPIRY_BANANA)
        .withQuantity(Count.of(VALID_INGREDIENT_QTY_BANANA))
        .build();
    public static final Ingredient CUSTARD = new IngredientBuilder()
        .withName(VALID_INGREDIENT_NAME_CUSTARD)
        .withDate(VALID_INGREDIENT_EXPIRY_CUSTARD)
        .withQuantity(Count.of(VALID_INGREDIENT_QTY_CUSTARD))
        .build();

    public static final IngredientReference APRICOT_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_APRICOT,
        Count.of(VALID_INGREDIENT_QTY_APRICOT)
    );

    public static final IngredientReference BANANA_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_BANANA,
        Count.of(VALID_INGREDIENT_QTY_BANANA)
    );

    public static final IngredientReference CUSTARD_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_CUSTARD,
        Count.of(VALID_INGREDIENT_QTY_CUSTARD)
    );

    /**
     * Returns an {@code IngredientBook} with all the typical ingredients.
     */
    public static EntryBook<Ingredient> getTypicalIngredientBook() {
        EntryBook<Ingredient> ab = new EntryBook<>();
        for (Ingredient ind : getTypicalIngredients()) {
            ab.add(ind);
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
