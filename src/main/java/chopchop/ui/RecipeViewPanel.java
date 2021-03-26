//@@author fall9x

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

public class RecipeViewPanel extends UiPart<Region> {
    private static final String FXML = "RecipeViewPanel.fxml";
    private static final String EMPTY_PROMPT = "You do not have any recipes yet, add one today!";
    private static final String FILTER_NO_MATCH = "No matching recipes found";

    private final ObservableList<Recipe> recipeObservableList;

    @FXML
    private ScrollPane recipePanel;

    @FXML
    private JFXMasonryPane recipeGridView;

    /**
     * Creates a {@code RecipeView} with the given {@code ObservableList}.
     */
    public RecipeViewPanel(ObservableList<Recipe> filteredList) {
        super(FXML);

        this.recipeObservableList = filteredList;
        this.fillDisplay();

        this.recipeObservableList.addListener((ListChangeListener<Recipe>) c -> this.fillDisplay());
    }

    /**
     * Forces a refresh on the ScrollPane, which causes it to recompute its size
     * and show a scrollbar if necessary.
     */
    public void refresh() {
        this.recipePanel.layout();
    }

    /**
     * Checks if the display contains any recipes, and fills the recipe grid view.
     */
    private void fillDisplay() {
        this.getPlaceholderText().ifPresentOrElse(
            t -> this.recipePanel.setContent(new TextDisplay(t).getRoot()), this::populate
        );
    }

    /**
     * Populates the gridPane with recipes stored.
     */
    private void populate() {
        this.recipeGridView.getChildren().clear();

        for (int i = 0; i < this.recipeObservableList.size(); i++) {
            var recipe = this.recipeObservableList.get(i);
            var recipeCard = new RecipeCard(recipe, i + 1);
            this.recipeGridView.getChildren().add(recipeCard.getRoot());
        }

        this.recipePanel.setContent(this.recipeGridView);
    }

    /**
     * Gets the appropriate text to show in the pane if there are no recipes to show.
     * If there are recipes to show, returns an empty optional.
     */
    private Optional<String> getPlaceholderText() {
        if (this.recipeObservableList instanceof FilteredList<?>) {
            var src = ((FilteredList<?>) this.recipeObservableList).getSource();

            // if the source was not empty but the filter view is empty,
            // then we have no results.
            if (!src.isEmpty() && this.recipeObservableList.isEmpty()) {
                return Optional.of(FILTER_NO_MATCH);
            }
        }

        return this.recipeObservableList.isEmpty()
            ? Optional.of(EMPTY_PROMPT)
            : Optional.empty();
    }
}
