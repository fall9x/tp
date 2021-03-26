package chopchop.testutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import chopchop.model.UsageList;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Mass;
import chopchop.model.attributes.units.Volume;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;

public class TypicalUsages {

    /*
    Objects are named this way. <RECIPE/INGREDIENT>_<NAME BY ALPHA ORDER>_<DATE, A BEING THE EARLIEST>
     */
    public static final RecipeUsage RECIPE_A_A =
        new RecipeUsage("A", Date.USAGE_DATE_A);
    public static final RecipeUsage RECIPE_A_B =
        new RecipeUsage("A", Date.USAGE_DATE_B);
    public static final RecipeUsage RECIPE_A_C =
        new RecipeUsage("A", Date.USAGE_DATE_C);
    public static final RecipeUsage RECIPE_A_D =
        new RecipeUsage("A", Date.USAGE_DATE_D);
    public static final RecipeUsage RECIPE_A_E =
        new RecipeUsage("A", Date.USAGE_DATE_E);

    public static final RecipeUsage RECIPE_B_A =
        new RecipeUsage("B", Date.USAGE_DATE_A);
    public static final RecipeUsage RECIPE_B_B =
        new RecipeUsage("B", Date.USAGE_DATE_B);
    public static final RecipeUsage RECIPE_B_C =
        new RecipeUsage("B", Date.USAGE_DATE_C);
    public static final RecipeUsage RECIPE_B_D =
        new RecipeUsage("B", Date.USAGE_DATE_D);
    public static final RecipeUsage RECIPE_B_E =
        new RecipeUsage("B", Date.USAGE_DATE_E);

    public static final RecipeUsage RECIPE_C_A =
        new RecipeUsage("C", Date.USAGE_DATE_A);

    public static final IngredientUsage INGREDIENT_A_A =
        new IngredientUsage("A", Date.USAGE_DATE_A,
            Count.of(1));
    public static final IngredientUsage INGREDIENT_A_B =
        new IngredientUsage("A", Date.USAGE_DATE_B,
            Mass.milligrams(1));
    public static final IngredientUsage INGREDIENT_A_C =
        new IngredientUsage("A", Date.USAGE_DATE_C,
            Mass.grams(1));
    public static final IngredientUsage INGREDIENT_A_D =
        new IngredientUsage("A", Date.USAGE_DATE_D,
            Mass.kilograms(1));
    public static final IngredientUsage INGREDIENT_A_E =
        new IngredientUsage("A", Date.USAGE_DATE_E,
            Volume.cups(1));

    public static final IngredientUsage INGREDIENT_B_A =
        new IngredientUsage("B", Date.USAGE_DATE_A,
            Volume.litres(1));
    public static final IngredientUsage INGREDIENT_B_B =
        new IngredientUsage("B", Date.USAGE_DATE_B,
            Volume.millilitres(1));
    public static final IngredientUsage INGREDIENT_B_C =
        new IngredientUsage("B", Date.USAGE_DATE_C,
            Volume.tablespoons(1));
    public static final IngredientUsage INGREDIENT_B_D =
        new IngredientUsage("B", Date.USAGE_DATE_D,
            Volume.teaspoons(1));
    public static final IngredientUsage INGREDIENT_B_E =
        new IngredientUsage("B", Date.USAGE_DATE_E,
            Mass.grams(1));

    /**
     * Returns a UsageList with its usages in Chronological order, first item has the lowest date. (i.e. like a stack)
     */
    public static UsageList<RecipeUsage> getRecipeUsageList() {
        return new UsageList<>(Arrays.asList(RECIPE_B_A, RECIPE_A_A, RECIPE_B_B, RECIPE_A_B, RECIPE_B_C, RECIPE_A_C,
            RECIPE_B_D, RECIPE_A_D, RECIPE_B_E, RECIPE_A_E));
    }

    public static ArrayList<RecipeUsage> getRecipeList() {
        return new ArrayList<>(Arrays.asList(RECIPE_B_A, RECIPE_A_A, RECIPE_B_B, RECIPE_A_B, RECIPE_B_C, RECIPE_A_C,
            RECIPE_B_D, RECIPE_A_D, RECIPE_B_E, RECIPE_A_E));
    }

    public static ArrayList<RecipeUsage> getListViewRecipeList() {
        return new ArrayList<>(Arrays.asList(RECIPE_A_E, RECIPE_B_E, RECIPE_A_D, RECIPE_B_D,
            RECIPE_A_C, RECIPE_B_C, RECIPE_A_B, RECIPE_B_B, RECIPE_A_A, RECIPE_B_A));
    }

    public static ArrayList<RecipeUsage> getUnsortedRecipeList() {
        return new ArrayList<>(Arrays.asList(RECIPE_A_A, RECIPE_B_A, RECIPE_B_D, RECIPE_A_D, RECIPE_A_B, RECIPE_B_B,
            RECIPE_A_C, RECIPE_B_C, RECIPE_A_E, RECIPE_B_E));
    }

    public static ArrayList<RecipeUsage> getDuplicateRecipeList() {
        return new ArrayList<>(Arrays.asList(RECIPE_B_E, RECIPE_B_E, RECIPE_B_E, RECIPE_A_A, RECIPE_A_A, RECIPE_A_A));
    }

    public static UsageList<IngredientUsage> getIngredientUsageList() {
        return new UsageList<>(Arrays.asList(INGREDIENT_B_A, INGREDIENT_A_A, INGREDIENT_B_B, INGREDIENT_A_B,
            INGREDIENT_B_C, INGREDIENT_A_C, INGREDIENT_B_D, INGREDIENT_A_D, INGREDIENT_B_E, INGREDIENT_A_E));
    }

    public static ArrayList<IngredientUsage> getIngredientList() {
        return new ArrayList<>(Arrays.asList(INGREDIENT_B_A, INGREDIENT_A_A, INGREDIENT_B_B, INGREDIENT_A_B,
            INGREDIENT_B_C, INGREDIENT_A_C, INGREDIENT_B_D, INGREDIENT_A_D, INGREDIENT_B_E, INGREDIENT_A_E));
    }

    public static ArrayList<IngredientUsage> getListViewIngredientList() {
        return new ArrayList<>(Arrays.asList(INGREDIENT_A_E, INGREDIENT_B_E, INGREDIENT_A_D, INGREDIENT_B_D,
            INGREDIENT_A_C, INGREDIENT_B_C, INGREDIENT_A_B, INGREDIENT_B_B, INGREDIENT_A_A, INGREDIENT_B_A));
    }

    public static ArrayList<IngredientUsage> getUnsortedIngredientList() {
        return new ArrayList<>(Arrays.asList(INGREDIENT_A_A, INGREDIENT_B_A, INGREDIENT_B_D, INGREDIENT_A_D,
            INGREDIENT_A_B, INGREDIENT_B_B, INGREDIENT_A_C, INGREDIENT_B_C, INGREDIENT_A_E, INGREDIENT_B_E));
    }

    public static ArrayList<IngredientUsage> getDuplicateIngredientList() {
        return new ArrayList<>(Arrays.asList(INGREDIENT_B_E, INGREDIENT_B_E, INGREDIENT_B_E, INGREDIENT_A_A,
            INGREDIENT_A_A, INGREDIENT_A_A));
    }

    public static class Date {
        public static final DateTimeFormatter ON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        public static final LocalDateTime USAGE_DATE_A0 = LocalDateTime.of(1, 1, 1, 1, 0);
        public static final LocalDateTime USAGE_DATE_A = LocalDateTime.of(1, 1, 1, 1, 1);
        public static final LocalDateTime USAGE_DATE_B = LocalDateTime.of(200, 1, 1, 1, 1);
        public static final LocalDateTime USAGE_DATE_C = LocalDateTime.of(2019, 1, 1, 1, 1);
        public static final LocalDateTime USAGE_DATE_D = LocalDateTime.of(2020, 1, 1, 1, 1);
        public static final LocalDateTime USAGE_DATE_E = LocalDateTime.of(2021, 1, 1, 1, 1);
        public static final LocalDateTime USAGE_DATE_E0 = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
    }
}
