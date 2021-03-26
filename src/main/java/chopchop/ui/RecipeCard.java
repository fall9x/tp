//@@author fall9x

package chopchop.ui;

import chopchop.model.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Recipe}.
 */
public class RecipeCard extends UiPart<Region> {
    private static final String FXML = "RecipeCard.fxml";

    private final Recipe recipe;

    @FXML
    private Button recipeButton;

    @FXML
    private Label recipeIndex;

    /**
     * Creates a {@code RecipeCard} with the given {@code Recipe}.
     */
    public RecipeCard(Recipe recipe, int id) {
        super(FXML);
        this.recipe = recipe;
        recipeIndex.setText(String.format("#%d", id));
        this.recipeButton.setText(recipe.getName());
    }

    @FXML
    public void handleSelectRecipe() {
        DisplayNavigator.loadRecipeDisplay(this.recipe);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RecipeCard
                && this.recipe.equals(((RecipeCard) other).recipe));
    }
}
