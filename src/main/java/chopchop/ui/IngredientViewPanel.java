//@@author fall9x

package chopchop.ui;

import java.util.Optional;

import com.jfoenix.controls.JFXMasonryPane;

import chopchop.model.ingredient.Ingredient;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public class IngredientViewPanel extends UiPart<Region> {
    private static final String FXML = "IngredientViewPanel.fxml";

    private static final String EMPTY_PROMPT = "You do not have any ingredients yet, add one today!";
    private static final String FILTER_NO_MATCH = "No matching ingredients found";

    private final ObservableList<Ingredient> ingredientObservableList;

    @FXML
    private ScrollPane ingredientPanel;

    @FXML
    private JFXMasonryPane ingredientGridView;

    /**
     * Creates a {@code RecipeView} with the given {@code ObservableList}.
     */
    public IngredientViewPanel(ObservableList<Ingredient> ingredientList) {
        super(FXML);

        this.ingredientObservableList = ingredientList;
        this.fillDisplay();

        this.ingredientObservableList.addListener((ListChangeListener<Ingredient>) c -> this.fillDisplay());
    }

    /**
     * Forces a refresh on the ScrollPane, which causes it to recompute its size
     * and show a scrollbar if necessary.
     */
    public void refresh() {
        this.ingredientPanel.layout();
    }

    /**
     * Checks if the display contains any recipes, and fills the recipe grid view.
     */
    private void fillDisplay() {
        this.getPlaceholderText().ifPresentOrElse(
            t -> this.ingredientPanel.setContent(new TextDisplay(t).getRoot()), this::populate
        );
    }

    /**
     * Populates the gridPane with recipes stored.
     */
    private void populate() {
        this.ingredientGridView.getChildren().clear();

        for (int i = 0; i < this.ingredientObservableList.size(); i++) {
            var ingredient = this.ingredientObservableList.get(i);
            var ingredientCard = new IngredientCard(ingredient, i + 1);
            this.ingredientGridView.getChildren().add(ingredientCard.getRoot());
        }

        this.ingredientPanel.setContent(this.ingredientGridView);
    }

    // TODO: copy-paste from RecipeViewPanel
    /**
     * Gets the appropriate text to show in the pane if there are no recipes to show.
     * If there are recipes to show, returns an empty optional.
     */
    private Optional<String> getPlaceholderText() {
        if (this.ingredientObservableList instanceof FilteredList<?>) {
            var src = ((FilteredList<?>) this.ingredientObservableList).getSource();

            // if the source was not empty but the filter view is empty,
            // then we have no results.
            if (!src.isEmpty() && this.ingredientObservableList.isEmpty()) {
                return Optional.of(FILTER_NO_MATCH);
            }
        }

        return this.ingredientObservableList.isEmpty()
            ? Optional.of(EMPTY_PROMPT)
            : Optional.empty();
    }
}
