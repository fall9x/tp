package chopchop.ui;

import chopchop.model.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * An UI component that displays information of a {@code Recipe}.
 */
public class RecommendationCard extends UiPart<Region> {
    private static final String FXML = "RecommendationCard.fxml";
    private static final String RECOMMENDED_MESSAGE = "These recommended recipes all contain ingredients that you "
            + "currently have.";
    private static final String EXPIRING_MESSAGE = "Consider cooking this recipe to use ingredients that are about to"
            + " expire.";
    private static final String OLD_MESSAGE = "Consider cooking this recipe that you haven't tried for a while.";

    @FXML
    private TextFlow recommendationText;

    @FXML
    private StackPane recipeCardContainer;

    /**
     * Creates a {@code RecommendationCard}.
     */
    public RecommendationCard(String message) {
        super(FXML);

        this.recipeCardContainer.setVisible(false);
        this.recipeCardContainer.setManaged(false);
        this.recommendationText.getChildren().add(new Text(message));
        this.getRoot().setPrefWidth(308);
    }

    /**
     * Creates a {@code RecommendationCard} with the given {@code Recipe}.
     */
    public RecommendationCard(Recipe recipe, int id, String message) {
        super(FXML);

        var recipeCard = new RecipeCard(recipe, id);
        this.recipeCardContainer.getChildren().add(recipeCard.getRoot());
        this.recommendationText.getChildren().add(new Text(message));
        this.getRoot().setPrefWidth(468);
    }
}
