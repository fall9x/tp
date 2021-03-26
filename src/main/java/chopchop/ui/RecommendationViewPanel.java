package chopchop.ui;

import java.util.Optional;

import com.jfoenix.controls.JFXMasonryPane;

import chopchop.model.recipe.Recipe;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public class RecommendationViewPanel extends UiPart<Region> {
    private static final String FXML = "RecommendationViewPanel.fxml";
    private static final String EMPTY_PROMPT = "Start using ChopChop to get recommendations!";
    private static final String INSTRUCTION_MESSAGE = "These recipes all make use of ingredients that you currently "
            + "have in stock.";
    private static final String EXPIRING_MESSAGE = "Consider cooking this recipe to use ingredients that are about to "
            + "expire.";

    private final ObservableList<Recipe> recommendedRecipeObservableList;
    private final ObservableList<Recipe> expiringRecipeObservableList;

    @FXML
    private ScrollPane recommendationPanel;

    @FXML
    private JFXMasonryPane recommendationGridView;

    /**
     * Creates a {@code RecipeView} with the given {@code ObservableList}.
     */
    public RecommendationViewPanel(ObservableList<Recipe> recommendationList, ObservableList<Recipe> expiringList) {
        super(FXML);

        this.recommendedRecipeObservableList = recommendationList;
        this.expiringRecipeObservableList = expiringList;
        this.recommendationGridView.setLayoutMode(JFXMasonryPane.LayoutMode.BIN_PACKING);
        this.fillDisplay();

        this.recommendedRecipeObservableList.addListener((ListChangeListener<Recipe>) c -> this.fillDisplay());
        this.expiringRecipeObservableList.addListener((ListChangeListener<Recipe>) c -> this.fillDisplay());
    }

    /**
     * Forces a refresh on the ScrollPane, which causes it to recompute its size
     * and show a scrollbar if necessary.
     */
    public void refresh() {
        this.recommendationPanel.layout();
    }

    /**
     * Checks if the display contains any recipes, and fills the recipe grid view.
     */
    private void fillDisplay() {
        this.getPlaceholderText().ifPresentOrElse(
            t -> this.recommendationPanel.setContent(new TextDisplay(t).getRoot()), this::populate
        );
    }

    /**
     * Populates the gridPane with recipes stored.
     */
    private void populate() {
        var recommendedList = (FilteredList<?>) this.recommendedRecipeObservableList;
        this.recommendationGridView.getChildren().clear();
        this.recommendationGridView.getChildren().add(new RecommendationCard(INSTRUCTION_MESSAGE).getRoot());

        for (int i = 0; i < this.recommendedRecipeObservableList.size(); i++) {
            var recipe = this.recommendedRecipeObservableList.get(i);
            UiPart<Region> recipeCard;

            if (this.expiringRecipeObservableList.indexOf(recipe) == 0) {
                recipeCard = new RecommendationCard(recipe, recommendedList.getSourceIndex(i) + 1, EXPIRING_MESSAGE);
            } else {
                recipeCard = new RecipeCard(recipe, recommendedList.getSourceIndex(i) + 1);
            }

            this.recommendationGridView.getChildren().add(recipeCard.getRoot());
        }

        this.recommendationPanel.setContent(this.recommendationGridView);
    }

    /**
     * Gets the appropriate text to show in the pane if there are no recipes to show.
     * If there are recipes to show, returns an empty optional.
     */
    private Optional<String> getPlaceholderText() {
        return this.recommendedRecipeObservableList.isEmpty()
                ? Optional.of(EMPTY_PROMPT)
                : Optional.empty();
    }
}
