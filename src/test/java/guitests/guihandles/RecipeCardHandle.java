package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * A handle to the {@code RecipeCard} in the GUI.
 */
public class RecipeCardHandle extends NodeHandle<Node> {

    private static final String NAME_FIELD_ID = "#recipeName";

    private final Button recipeButton;

    /**
     * Constructs a {@code RecipeCardHandle} with the given {@code recipeCardNode}.
     */
    public RecipeCardHandle(Node recipeCardNode) {
        super(recipeCardNode);

        recipeButton = getChildNode(NAME_FIELD_ID);
    }

    /**
     * Retrieves the name of the recipe in the recipe card.
     */
    public String getName() {
        return recipeButton.getText();
    }
}
