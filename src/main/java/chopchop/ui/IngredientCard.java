package chopchop.ui;

import chopchop.model.ingredient.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class IngredientCard extends UiPart<Region> {

    private static final String FXML = "IngredientCard.fxml";

    public final Ingredient ingredient;

    @FXML
    private Button cardPane;

    @FXML
    private Label name;

    /**
     * Creates a {@code RecipeCard} with the given {@code Recipe}.
     */
    public IngredientCard(Ingredient ingredient) {
        super(FXML);
        this.ingredient = ingredient;
        name.setText(displayFormatter(ingredient.toString()));
    }

    /**
     * Adds a new line to separate the ingredient name and the quantity.
     */
    private String displayFormatter(String string) {
        String[] temp = string.split(" ", 2);
        return String.join("\n", temp);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof IngredientCard)) {
            return false;
        }
        // state check
        IngredientCard card = (IngredientCard) other;
        return ingredient.equals(card.ingredient);
    }
}
