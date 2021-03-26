package chopchop.model.attributes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chopchop.testutil.IngredientBuilder;
import chopchop.testutil.TypicalRecipes;
import org.junit.jupiter.api.Test;

public class ExpiryDateOnOrBeforePredicateTest {

    @Test
    public void equals() {
        ExpiryDate firstPredicateKeyword = new ExpiryDate("2020-12-31");
        ExpiryDate secondPredicateKeyword = new ExpiryDate("2021-01-01");

        var firstPredicate = new ExpiryDateOnOrBeforePredicate(firstPredicateKeyword);
        var secondPredicate = new ExpiryDateOnOrBeforePredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        var firstPredicateCopy = new ExpiryDateOnOrBeforePredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different values -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_expiryDateMatchesKeyword_returnsTrue() {
        var predicate = new ExpiryDateOnOrBeforePredicate(new ExpiryDate("2020-12-31"));
        assertTrue(predicate.test(new IngredientBuilder().withDate("2020-10-10").build()));
        assertTrue(predicate.test(new IngredientBuilder().withDate("2020-11-11").build()));
        assertTrue(predicate.test(new IngredientBuilder().withDate("2020-12-31").build()));
    }

    @Test
    public void test_expiryDateNotMatchingKeyword_returnsFalse() {
        var predicate = new ExpiryDateOnOrBeforePredicate(new ExpiryDate("2020-12-30"));
        assertFalse(predicate.test(new IngredientBuilder().withDate("2020-12-31").build()));
        assertFalse(predicate.test(new IngredientBuilder().withDate("2021-01-01").build()));
        assertFalse(predicate.test(new IngredientBuilder().withDate("2103-10-03").build()));
        assertFalse(predicate.test(TypicalRecipes.APRICOT_SALAD));
    }
}
