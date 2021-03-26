// IngredientCard.java
//@@author fall9x

package chopchop.ui;

import chopchop.model.ingredient.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class IngredientCard extends UiPart<Region> {

    private static final String FXML = "IngredientCard.fxml";

    public final Ingredient ingredient;

    @FXML
    private Label ingredientName;

    @FXML
    private Label quantity;

    @FXML
    private Label expiryDate;

    @FXML
    private FlowPane tagList;

    @FXML
    private Label index;

    @FXML
    private HBox expiryBox;

    /**
     * Creates a {@code RecipeCard} with the given {@code Recipe}.
     */
    public IngredientCard(Ingredient ingredient, int id) {
        super(FXML);
        this.ingredient = ingredient;

        this.index.setText(String.format("#%d", id));

        this.ingredientName.setText(ingredient.getName());
        this.quantity.setText(ingredient.getQuantity().toString());

        this.ingredient.getExpiryDate().ifPresentOrElse(exp -> this.expiryDate.setText(exp.toString()), () -> {
            this.expiryBox.setVisible(false);
            this.expiryBox.setManaged(false);
        });

        this.ingredient.getTags().stream()
            .map(Object::toString)
            .sorted()
            .map(tag -> {
                var label = new Label(tag);
                var rect = new Rectangle();
                rect.setArcWidth(6.0);
                rect.setArcHeight(6.0);
                rect.getStyleClass().add("ingredient-tag-rect");

                rect.widthProperty().bind(label.widthProperty());
                rect.heightProperty().bind(label.heightProperty());

                return new StackPane(rect, label);
            })
            .forEach(this.tagList.getChildren()::add);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof IngredientCard
                && this.ingredient.equals(((IngredientCard) other).ingredient));
    }
}
