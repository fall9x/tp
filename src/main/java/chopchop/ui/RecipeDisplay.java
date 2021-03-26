// RecipeDisplay.java
//@@author fall9x

package chopchop.ui;

import java.util.List;

import chopchop.commons.util.StreamUtils;
import chopchop.model.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RecipeDisplay extends UiPart<Region> {
    private static final String FXML = "RecipeDisplay.fxml";

    private final Recipe recipe;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label recipeName;

    @FXML
    private Label ingredientHeader;

    @FXML
    private TextFlow ingredientList;

    @FXML
    private Label stepHeader;

    @FXML
    private TextFlow stepList;

    @FXML
    private FlowPane tagList;


    /**
     * Creates a {@code RecipeDisplay} with a {@code Recipe}.
     * @param recipe
     */
    public RecipeDisplay(Recipe recipe) {
        super(FXML);

        this.recipe = recipe;
        this.display();

        // Implements responsive percentage widths for columns
        this.getRoot().widthProperty().addListener((observable, oldValue, newValue) -> {
            var col1 = new ColumnConstraints();
            var col2 = new ColumnConstraints();

            this.gridPane.getColumnConstraints().clear();

            if (newValue.doubleValue() < 768) {
                col1.setPercentWidth(50);
                col2.setPercentWidth(50);
            } else if (newValue.doubleValue() >= 992) {
                col1.setPercentWidth(30);
                col2.setPercentWidth(70);
            } else {
                col1.setPercentWidth(40);
                col2.setPercentWidth(60);
            }

            this.gridPane.getColumnConstraints().addAll(col1, col2);
        });
    }

    /**
     * Displays the recipe on the recipeDisplay.
     */
    private void display() {
        this.recipeName.setText(this.recipe.getName());

        this.stepHeader.setText("Steps");
        this.ingredientHeader.setText("Ingredients");

        this.tagList.getChildren().clear();
        this.stepList.getChildren().clear();
        this.ingredientList.getChildren().clear();

        if (this.recipe.getIngredients().isEmpty()) {
            this.ingredientList.getChildren().add(new Text("Recipe uses no ingredients"));
        } else {
            this.recipe.getIngredients().stream()
                .map(Object::toString)
                .map(s -> new Text(s + "\n"))
                .forEach(this.ingredientList.getChildren()::add);
        }

        if (this.recipe.getSteps().isEmpty()) {
            this.stepList.getChildren().add(new Text("Recipe has no steps"));
        } else {
            StreamUtils.indexed(this.recipe.getSteps().stream())
                .flatMap(s -> {
                    var label = new Label(String.format("%d.", 1 + s.fst()));
                    label.setPrefWidth(20);
                    label.getStyleClass().add("recipe-steps-numbering");

                    return List.of(
                        label, new Text(String.format("%s\n", s.snd()))
                    ).stream();
                })
                .forEach(this.stepList.getChildren()::add);
        }

        if (this.recipe.getTags().isEmpty()) {
            this.tagList.getChildren().add(new Text("Recipe has no tags"));
        } else {
            this.recipe.getTags().stream()
                .map(Object::toString)
                .sorted()
                .map(s -> {
                    var t = new Label(s);
                    t.getStyleClass().add("recipe-tag");
                    return t;
                })
                .forEach(this.tagList.getChildren()::add);
        }
    }
}
