package guitests.guihandles;

import chopchop.model.ingredient.Ingredient;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A handle to the {@code IngredientCard} in the GUI.
 */
public class IngredientCardHandle extends NodeHandle<Node> {
    private static final String NAME_FIELD_ID = "#ingredientName";
    private static final String QTY_FIELD_ID = "#quantity";
    private static final String EXPIRY_DATE_FIELD_ID = "#expiryDate";
    private static final String TAG_LIST_FIELD_ID = "#tagList";

    private final Label nameLabel;
    private final Label qtyLabel;
    private final Label expLabel;
    private final List<Label> tagLabels;

    private int id;


    /**
     * Constructs a {@code IngredientCardHandle} with the given {@code ingredientCardNode}.
     */
    public IngredientCardHandle(Node ingredientCardNode, int id) {
        super(ingredientCardNode);
        nameLabel = getChildNode(NAME_FIELD_ID);
        qtyLabel = getChildNode(QTY_FIELD_ID);
        expLabel = getChildNode(EXPIRY_DATE_FIELD_ID);

        this.id = id;

        Region tagsContainer = getChildNode(TAG_LIST_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(StackPane.class::cast)
                .map(s -> s.getChildren().get(1))
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the name of the ingredient in the ingredient card.
     */
    public String getName() {
        return nameLabel.getText();
    }

    /**
     * Retrieves the quantity of the ingredient in the ingredient card.
     */
    public String getQty() {
        return qtyLabel.getText();
    }

    /**
     * Retrieves the expiry date of the ingredient in the ingredient card.
     */
    public String getExpiryDate() {
        return expLabel.getText();
    }

    /**
     * Retrieves the list of tags of the ingredient in the ingredient card.
     */
    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the id of the ingredient in the ingredient card.
     */
    public int getId() {
        return id;
    }

    /**
     * Compares an ingredient card to verify if it is equal to an ingredient. Ingredient names are unique.
     */
    public boolean equals(Ingredient ingredient) {
        // Well we gotta change this depending on how we make our ingredient card more presentable uwu
        return getName().equals(ingredient.getName());
    }
}
