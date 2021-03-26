package chopchop.model.ingredient;

import java.util.Optional;
import java.util.Set;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.commons.util.Pair;
import chopchop.testutil.IngredientBuilder;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.Tag;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Mass;
import chopchop.model.exceptions.IncompatibleIngredientsException;

import org.junit.jupiter.api.Test;

import static chopchop.testutil.TypicalIngredients.APRICOT;
import static chopchop.testutil.TypicalIngredients.BANANA;
import static chopchop.testutil.TypicalRecipes.APRICOT_SALAD;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static chopchop.testutil.Assert.assertThrows;

public class IngredientTest {

    @Test
    public void test_equals() {
        // same values -> returns true
        Ingredient apricotCopy = new IngredientBuilder(APRICOT).build();
        assertTrue(APRICOT.equals(apricotCopy));

        // same object -> returns true
        assertTrue(APRICOT.equals(APRICOT));

        // null -> returns false
        assertFalse(APRICOT.equals(null));

        // different type -> returns false
        assertFalse(APRICOT.equals(5));

        // different ingredient -> returns false
        assertFalse(APRICOT.equals(BANANA));

        // different name -> returns false
        Ingredient editedApricot = new IngredientBuilder(APRICOT).withName("DD").build();
        assertFalse(APRICOT.equals(editedApricot));

        // different date -> returns false. Different ingredients can be of the same name but different expiry
        editedApricot = new IngredientBuilder(APRICOT).withDate("2020-12-02").build();
        assertFalse(APRICOT.equals(editedApricot));


        // different date -> returns false. Different ingredients can be of the same name but different expiry
        var i1 = new IngredientBuilder(APRICOT).withTags(Set.of(new Tag("a"), new Tag("b"))).build();
        var i2 = new IngredientBuilder(APRICOT).withTags(Set.of(new Tag("c"), new Tag("d"))).build();
        assertNotEquals(i1, i2);

        assertTrue(APRICOT.isSame(APRICOT));
        assertFalse(APRICOT.isSame(APRICOT_SALAD));
    }


    @Test
    public void test_reference() {

        var i1 = new IngredientReference("milk", Quantity.parse("77ml").getValue());
        var i2 = new IngredientReference("milk", Quantity.parse("0.077l").getValue());
        var i3 = new IngredientReference("bread", Quantity.parse("500g").getValue());
        var i4 = new IngredientReference("milk", Quantity.parse("500g").getValue());

        assertEquals(i1, i1);
        assertEquals(i1, i2);

        assertNotEquals(i1, "asdf");
        assertNotEquals(i1, i3);
        assertNotEquals(i1, i4);
    }


    @Test
    public void test_combine() {

        var i1 = new IngredientBuilder(APRICOT).withDate("2020-12-01")
            .withQuantity(Quantity.parse("30g").getValue()).build();

        var i2 = new IngredientBuilder(APRICOT).withDate("2019-11-09")
            .withQuantity(Quantity.parse("40g").getValue()).build();

        var i3 = new IngredientBuilder(APRICOT).withDate("2021-02-03")
            .withQuantity(Quantity.parse("50g").getValue()).build();

        var i4 = new IngredientBuilder(APRICOT).withDate("2021-02-03")
            .withQuantity(Quantity.parse("60g").getValue()).build();

        var i5 = new IngredientBuilder(APRICOT)
            .withQuantity(Quantity.parse("6.9kg").getValue()).build();

        var xx = i1.combine(i2).combine(i3).combine(i4).combine(i5);

        assertEquals(Optional.of(new ExpiryDate("2019-11-09")), xx.getExpiryDate());
        assertEquals(Mass.kilograms(7.08), xx.getQuantity());

        var j1 = new IngredientBuilder(BANANA).withQuantity(Quantity.parse("300g").getValue())
            .withDate("2021-02-03").build();

        var j2 = new IngredientBuilder(BANANA).withQuantity(Quantity.parse("7").getValue())
            .withDate("2021-02-03").build();

        var j3 = new IngredientBuilder(BANANA).withQuantity(Quantity.parse("8").getValue())
            .withDate("2021-11-09").build();

        assertThrows(IncompatibleIngredientsException.class, () -> i2.combine(j3));
        assertThrows(IncompatibleIngredientsException.class, () -> j1.combine(j2));
        assertThrows(IncompatibleIngredientsException.class, () -> j1.combine(j3));

        j3.hashCode();
    }

    @Test
    public void test_split() {

        var i1 = new IngredientBuilder(APRICOT).withDate("2020-12-25")
            .withQuantity(Quantity.parse("30g").getValue()).build();

        var i2 = new IngredientBuilder(APRICOT).withDate("2019-11-09")
            .withQuantity(Quantity.parse("40g").getValue()).build();

        var i3 = new IngredientBuilder(APRICOT).withDate("2021-02-03")
            .withQuantity(Quantity.parse("50g").getValue()).build();

        var i4 = new IngredientBuilder(APRICOT).withDate("2021-02-03")
            .withQuantity(Quantity.parse("60g").getValue()).build();

        var i5 = new IngredientBuilder().withName("Apricot").withDate(null)
            .withQuantity(Quantity.parse("6.9kg").getValue()).build();

        var xx = i1.combine(i2).combine(i3).combine(i4).combine(i5);

        assertThrows(IllegalValueException.class, () -> xx.split(Mass.kilograms(69)));
        assertThrows(IllegalValueException.class, () -> xx.split(Mass.kilograms(-7)));

        Pair<Ingredient, Ingredient> p = null;
        try {
            p = xx.split(Mass.kilograms(1));
        } catch (IllegalValueException e) {
            assertTrue(false);
        }

        var p1 = p.fst();
        var p2 = p.snd();

        var pp = i1.combine(i2).combine(i3).combine(i4).combine(
            new IngredientBuilder()
                .withName("Apricot")
                .withDate(null)
                .withQuantity(Mass.kilograms(0.82))
                .build()
        );

        assertEquals(pp, p1);

        try {
            var ii = new IngredientBuilder().withName("owo")
                .withQuantity(Count.of(40)).build();

            var ppp = ii.split(Count.of(40));
            var ppp1 = ppp.fst();
            var ppp2 = ppp.snd();

            // System.err.printf("ppp1 = %s / %s\n", ppp1, ppp1.getIngredientSets());
            assertTrue(ppp2.getIngredientSets().isEmpty());

        } catch (IllegalValueException e) {
            assertTrue(false);
        }
    }
}
