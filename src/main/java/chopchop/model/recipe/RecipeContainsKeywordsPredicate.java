package chopchop.model.recipe;

import chopchop.commons.util.StringUtil;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Recipe}'s Content matches any of the keywords given.
 */
public class RecipeContainsKeywordsPredicate implements Predicate<Recipe> {
    private final List<String> keywords;

    public RecipeContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Recipe recipe) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(recipe.toString(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RecipeContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((RecipeContainsKeywordsPredicate) other).keywords)); // state check
    }

}
