// RecipeDisplay.java
//@@author fall9x

package chopchop.ui;

import java.util.List;
import java.util.stream.Collectors;

import chopchop.commons.util.StreamUtils;
import chopchop.model.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RecipeDisplay extends UiPart<Region> {

    private static final String FXML = "RecipeDisplay.fxml";

    private final Recipe recipe;

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


    /**
     * Creates a {@code RecipeDisplay} with a {@code Recipe}.
     * @param recipe
     */
    public RecipeDisplay(Recipe recipe) {
        super(FXML);

        this.recipe = recipe;
        display();
    }

    /**
     * Displays the recipe on the recipeDisplay.
     */
    private void display() {

        this.recipeName.setText(this.recipe.getName());

        this.stepHeader.setText("Steps");
        this.ingredientHeader.setText("Ingredients");

        this.stepList.getChildren().clear();
        this.ingredientList.getChildren().clear();


        if (this.recipe.getIngredients().isEmpty()) {
            this.ingredientList.getChildren().add(new Text("Recipe uses no ingredients"));
        } else {
            this.ingredientList.getChildren().setAll(
                this.recipe.getIngredients().stream()
                    .map(Object::toString)
                    .map(s -> new Text(s + "\n"))
                    .collect(Collectors.toList())
            );
        }


        if (this.recipe.getSteps().isEmpty()) {
            this.stepList.getChildren().add(new Text("Recipe has no steps"));
        } else {
            this.stepList.getChildren().setAll(
                StreamUtils.indexed(this.recipe.getSteps().stream())
                    .flatMap(s -> {
                        var label = new Label(String.format("%d.", 1 + s.fst()));
                        label.setPrefWidth(20);

                        return List.of(
                            label, new Text(String.format("%s\n", s.snd()))
                        ).stream();
                    })
                    .collect(Collectors.toList())
            );
        }
    }
}
