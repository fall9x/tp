package chopchop.ui;

import chopchop.model.ingredient.Ingredient;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class IngredientViewPanel extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "IngredientViewPanel.fxml";
    private static final String EMPTY_PROMPT = "You do not have any ingredients yet.\nAdd one today:)";
    private static final int ROWS = 3;
    private static final int START_COL = -1;

    private final TextDisplay textDisplay;
    // Only 3 rows of recipes will be displayed.
    private ObservableList<Ingredient> ingredientObservableList;


    @FXML
    private ScrollPane ingredientPanel;

    @FXML
    private GridPane ingredientGridView;

    /**
     * Creates a {@code RecipeView} with the given {@code ObservableList}.
     */
    public IngredientViewPanel(ObservableList<Ingredient> ingredientList) {
        super(FXML);
        ingredientObservableList = ingredientList;
        textDisplay = new TextDisplay(EMPTY_PROMPT);
        ingredientObservableList.addListener(new ListChangeListener<Ingredient>() {
            @Override
            public void onChanged(Change<? extends Ingredient> c) {
                fillDisplay();
            }
        });
        fillDisplay();
    }

    private int calculate_row(int index) {
        return index % ROWS;
    }

    /**
     * Populates the gridPane with recipes stored.
     */
    private void populate() {
        int row;
        int col = START_COL;
        for (int i = 0; i < ingredientObservableList.size(); i++) {
            Ingredient ingredient = ingredientObservableList.get(i);
            IngredientCard ingredientCard = new IngredientCard(ingredient);
            row = calculate_row(i);
            if (row == 0) {
                col++;
            }
            ingredientGridView.add(ingredientCard.getRoot(), col, row);
        }
    }

    /**
     * Checks if the display contains any recipes, and fills the recipe grid view.
     */
    private void fillDisplay() {
        ingredientGridView.getChildren().clear();
        if (isEmpty()) {
            displayPrompt();
        } else {
            populate();
        }
    }

    private void displayPrompt() {
        ingredientGridView.add(textDisplay.getRoot(), 0, 0);
    }

    private boolean isEmpty() {
        return ingredientGridView.getChildren().contains(textDisplay) || ingredientObservableList.isEmpty();
    }
}
