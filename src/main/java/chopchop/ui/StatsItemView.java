// StatsItemView.java

package chopchop.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;


public class StatsItemView extends UiPart<Region> {

    private static final String FXML = "StatsItemView.fxml";

    @FXML
    private Label recipeName;

    @FXML
    private Label recipeDate;

    /**
     * Creates a {@code RecipeCard} with the given {@code Recipe}.
     */
    public StatsItemView(String name, String date) {
        super(FXML);

        this.recipeName.setText(name);
        this.recipeDate.setText(date);
    }
}
