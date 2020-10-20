package chopchop.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import chopchop.model.EntryBook;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Step;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Mass;
import chopchop.model.attributes.units.Volume;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.recipe.Recipe;

public class SampleDataUtil {
    public static Ingredient[] getSampleIngredients() {
        return new Ingredient[] {
            new Ingredient("Apple", Mass.grams(10), new ExpiryDate("2020-04-04")),
            new Ingredient("Blueberry", Count.of(5)),
            new Ingredient("Custard", Volume.millilitres(200.5)),
        };
    }

    public static ReadOnlyEntryBook<Ingredient> getSampleIngredientBook() {
        EntryBook<Ingredient> sampleIngredientBook = new EntryBook<>();
        for (Ingredient sampleIngredient : getSampleIngredients()) {
            sampleIngredientBook.add(sampleIngredient);
        }
        return sampleIngredientBook;
    }

    public static Recipe[] getSampleRecipe() {
        return new Recipe[] {
            new Recipe("Apple blue mix",
                Arrays.stream(getSampleIngredients())
                    .map((x)-> new IngredientReference(x.getName(), x.getQuantity()))
                    .collect(Collectors.toList()),
                new ArrayList<>(Arrays.asList(new Step("Put them on a table."),
                    new Step("Cut and chop them."),
                    new Step("Mix them.")))),
            new Recipe("Blue custard cream",
                Arrays.stream(getSampleIngredients())
                    .map((x)-> new IngredientReference(x.getName(), x.getQuantity()))
                    .collect(Collectors.toList()),
                new ArrayList<>(Arrays.asList(new Step("Blend them."),
                    new Step("Stir them well."),
                    new Step("Leave it in the refrigerator for 8 hours"))))
        };
    }

    public static ReadOnlyEntryBook<Recipe> getSampleRecipeBook() {
        EntryBook<Recipe> sampleRecipeBook = new EntryBook<>();
        for (Recipe sampleRecipe : getSampleRecipe()) {
            sampleRecipeBook.add(sampleRecipe);
        }
        return sampleRecipeBook;
    }
}
