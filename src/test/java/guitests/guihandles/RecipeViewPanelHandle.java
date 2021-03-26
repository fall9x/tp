package guitests.guihandles;

import java.util.Optional;
import java.util.Set;

import chopchop.model.recipe.Recipe;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Provides a handle for {@code RecipeViewPanel} containing the list of {@code RecipeCard}.
 */
public class RecipeViewPanelHandle extends NodeHandle<GridPane> {

    public static final String RECIPE_VIEW_PANEL_ID = "#recipeGridView";
    public static final String RECIPE_CARD_ID = "#recipeCard";

    private Optional<Recipe> lastRememberedSelectedRecipeCard;

    /**
     * Constructs a {@code RecipeViewPanelHandle} with a given {@code GridPane}.
     */
    public RecipeViewPanelHandle(GridPane recipeViewPanelNode) {
        super(recipeViewPanelNode);
    }

    private void test() {

    }

    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(RECIPE_CARD_ID).queryAll();
    }

    public int getSize() {
        return getRootNode().getChildren().size();
    }
}
