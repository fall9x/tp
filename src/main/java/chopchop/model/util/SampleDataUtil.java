package chopchop.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Name;
import chopchop.model.attributes.Step;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Mass;
import chopchop.model.attributes.units.Volume;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.IngredientBook;
import chopchop.model.ingredient.IngredientReference;
import chopchop.model.ingredient.ReadOnlyIngredientBook;
import chopchop.model.recipe.ReadOnlyRecipeBook;
import chopchop.model.recipe.Recipe;
import chopchop.model.recipe.RecipeBook;

public class SampleDataUtil {
    public static Ingredient[] getSampleIngredients() {
        return new Ingredient[] {
            new Ingredient(new Name("Apple"),  Mass.grams(10), new ExpiryDate("2020-04-04")),
            new Ingredient(new Name("Blueberry"), Count.of(5)),
            new Ingredient(new Name("Custard"), Volume.millilitres(200.5)),
        };
    }

    public static ReadOnlyIngredientBook getSampleIngredientBook() {
        IngredientBook sampleIndBook = new IngredientBook();
        for (Ingredient sampleInd : getSampleIngredients()) {
            sampleIndBook.addIngredient(sampleInd);
        }
        return sampleIndBook;
    }

    public static Recipe[] getSampleRecipe() {
        return new Recipe[] {
            new Recipe(new Name("Apple blue mix"),
                Arrays.stream(getSampleIngredients())
                    .map((x)-> new IngredientReference(x.getName().toString(), x.getQuantity()))
                    .collect(Collectors.toList()),
                new ArrayList<>(Arrays.asList(new Step("Put them on a table."),
                    new Step("Cut and chop them."),
                    new Step("Mix them.")))),
            new Recipe(new Name("Blue custard cream"),
                Arrays.stream(getSampleIngredients())
                    .map((x)-> new IngredientReference(x.getName().toString(), x.getQuantity()))
                    .collect(Collectors.toList()),
                new ArrayList<>(Arrays.asList(new Step("Blend them."),
                    new Step("Stir them well."),
                    new Step("Leave it in the refrigerator for 8 hours"))))
        };
    }

    public static ReadOnlyRecipeBook getSampleRecipeBook() {
        RecipeBook sampleRecipeBook = new RecipeBook();
        for (Recipe sampleRec : getSampleRecipe()) {
            sampleRecipeBook.addRecipe(sampleRec);
        }
        return sampleRecipeBook;
    }
}
