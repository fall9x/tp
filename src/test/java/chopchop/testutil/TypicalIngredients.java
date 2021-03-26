package chopchop.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import chopchop.model.EntryBook;
import chopchop.model.attributes.Tag;
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

    public static final Tag TAG_APRICOT = new Tag("Sweet Apricot");
    public static final Tag TAG_BANANA = new Tag("Sweet Banana");
    public static final Tag TAG_ALL_ONE = new Tag("Sweet");
    public static final Tag TAG_ALL_TWO = new Tag("Fruit");

    public static final Ingredient APRICOT = new IngredientBuilder()
        .withName(VALID_INGREDIENT_NAME_APRICOT)
        .withDate(VALID_INGREDIENT_EXPIRY_APRICOT)
        .withQuantity(Count.of(VALID_INGREDIENT_QTY_APRICOT))
        .withTags(new HashSet<>(Arrays.asList(TAG_APRICOT, TAG_ALL_ONE, TAG_ALL_TWO)))
        .build();

    public static final Ingredient APRICOT_WITHOUT_TAGS = new IngredientBuilder()
            .withName(VALID_INGREDIENT_NAME_APRICOT)
            .withDate(VALID_INGREDIENT_EXPIRY_APRICOT)
            .withQuantity(Count.of(VALID_INGREDIENT_QTY_APRICOT))
            .build();

    public static final Ingredient BANANA = new IngredientBuilder()
        .withName(VALID_INGREDIENT_NAME_BANANA)
        .withDate(VALID_INGREDIENT_EXPIRY_BANANA)
        .withQuantity(Count.of(VALID_INGREDIENT_QTY_BANANA))
        .withTags(new HashSet<>(Arrays.asList(TAG_BANANA, TAG_ALL_ONE, TAG_ALL_TWO)))
        .build();

    public static final Ingredient CUSTARD = new IngredientBuilder()
        .withName(VALID_INGREDIENT_NAME_CUSTARD)
        .withDate(VALID_INGREDIENT_EXPIRY_CUSTARD)
        .withQuantity(Count.of(VALID_INGREDIENT_QTY_CUSTARD))
        .build();

    public static final Ingredient BAKED_BEANS = new IngredientBuilder()
        .withName("Baked beans")
        .withDate("2020-12-25")
        .withQuantity(Count.of(40))
        .build();


    public static final IngredientReference APRICOT_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_APRICOT,
        Count.of(3)
    );

    public static final IngredientReference BANANA_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_BANANA,
        Count.of(3)
    );

    public static final IngredientReference CUSTARD_REF = new IngredientReference(
        VALID_INGREDIENT_NAME_CUSTARD,
        Count.of(3)
    );

    public static final IngredientReference BAKED_BEANS_REF = new IngredientReference(
        "Baked beans",
        Count.of(10)
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
