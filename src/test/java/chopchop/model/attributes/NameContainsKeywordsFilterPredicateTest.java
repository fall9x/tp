package chopchop.model.attributes;

import chopchop.testutil.IngredientBuilder;
import chopchop.testutil.RecipeBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static chopchop.testutil.TypicalIngredients.BANANA_REF;
import static chopchop.testutil.TypicalRecipes.STEP_BANANA_SALAD;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NameContainsKeywordsFilterPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsFilterPredicate firstPredicate = new NameContainsKeywordsFilterPredicate(
                firstPredicateKeywordList);
        NameContainsKeywordsFilterPredicate secondPredicate = new NameContainsKeywordsFilterPredicate(
                secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsFilterPredicate firstPredicateCopy = new NameContainsKeywordsFilterPredicate(
                                                                     firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        NameContainsKeywordsFilterPredicate predicate = new NameContainsKeywordsFilterPredicate(Collections
                                                            .singletonList("Apricot"));
        assertTrue(predicate.test(new IngredientBuilder().withName("Apricot").build()));
        assertTrue(predicate.test(new RecipeBuilder().withName("Apricot").build()));
        assertTrue(predicate.test(new IngredientBuilder().withName("Apricot Caramel").build()));
        assertTrue(predicate.test(new RecipeBuilder().withName("Apricot Caramel").build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsFilterPredicate(Arrays.asList("Apricot", "Banana"));
        assertTrue(predicate.test(new IngredientBuilder().withName("Apricot Banana").build()));
        assertTrue(predicate.test(new RecipeBuilder().withName("Apricot Banana").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsFilterPredicate(Arrays.asList("aPRIcot", "bAnAnA"));
        assertTrue(predicate.test(new IngredientBuilder().withName("Apricot banana").build()));
        assertTrue(predicate.test(new RecipeBuilder().withName("Apricot Banana").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Non-matching keyword
        NameContainsKeywordsFilterPredicate predicate = new NameContainsKeywordsFilterPredicate(Arrays
                                                            .asList("Caramel"));
        assertFalse(predicate.test(new IngredientBuilder().withName("Apricot Banana").build()));
        assertFalse(predicate.test(new RecipeBuilder().withName("Apricot Banana").build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsFilterPredicate(Arrays.asList("Banana", "Caramel"));
        assertFalse(predicate.test(new IngredientBuilder().withName("Alice Caramel").build()));
        assertFalse(predicate.test(new RecipeBuilder().withName("Apricot Caramel").build()));

        // Keywords match other fields, but do not match name
        predicate = new NameContainsKeywordsFilterPredicate(Arrays.asList("2020-05-10", "tagOne", "tagTwo"));
        assertFalse(predicate.test(new IngredientBuilder().withName("apricot").withDate("2020-05-10").withTags(
                new HashSet<>(Arrays.asList(new Tag("tagOne"), new Tag("tagTwo")))).build()));

        // For Recipe, keywords match ingredient name and step content, but do not match name
        predicate = new NameContainsKeywordsFilterPredicate(Arrays.asList(
                "Banana"));
        assertFalse(predicate.test(new RecipeBuilder().withName("Apricot Salad")
            .withIngredients(new ArrayList<>(Arrays.asList(BANANA_REF)))
            .withSteps(new ArrayList<>(Arrays.asList(STEP_BANANA_SALAD)))
            .build()));
    }

}

