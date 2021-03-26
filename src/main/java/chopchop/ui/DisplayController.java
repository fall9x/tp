//@@author fall9x

package chopchop.ui;

import static chopchop.model.Model.PREDICATE_SHOW_ALL_ENTRIES;

import chopchop.MainApp;
import chopchop.logic.Logic;
import chopchop.model.Model;
import chopchop.model.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Controller class for the swappable display region.
 */
public class DisplayController extends UiPart<Region> {
    private static final String FXML = "DisplayPanel.fxml";
    private static final String WELCOME_MESSAGE = "Welcome to ChopChop, a food recipe management system!";
    private static final String NOTIFICATION_MESSAGE = "Feature will be coming soon!!";

    private final Model model;
    private final Region welcomeMessage;
    private final RecipeViewPanel recipeViewPanel;
    private final IngredientViewPanel ingredientViewPanel;
    private final RecommendationViewPanel recommendationViewPanel;

    @FXML
    private StackPane displayAreaPlaceholder;

    @FXML
    private Button recipeButton;

    @FXML
    private Button ingredientButton;

    @FXML
    private Button recommendationButton;

    /**
     * Creates a {@code DisplayController} with the given {@code Logic}.
     * @param logic
     */
    public DisplayController(Logic logic, Model model) {
        super(FXML);
        this.model = model;
        this.recipeViewPanel = new RecipeViewPanel(logic.getFilteredRecipeList());
        this.ingredientViewPanel = new IngredientViewPanel(logic.getFilteredIngredientList());
        this.recommendationViewPanel = new RecommendationViewPanel(logic.getRecommendedRecipeList(),
                logic.getExpiringRecipeList());

        {
            var container = new ScrollPane();
            container.setFitToWidth(true);
            container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            container.setContent(new TextDisplay(WELCOME_MESSAGE).getRoot());

            this.welcomeMessage = container;
        }
    }

    /**
     * Loads the initial panel.
     */
    public void initialLoad(boolean isPresent) {
        if (isPresent) {
            this.displayRecipeList();
        } else {
            this.displayWelcomeMessage();
        }
    }

    /**
     * Displays the RecipeViewPanel on the swappable display region.
     */
    protected void displayWelcomeMessage() {
        this.resetButtons();
        this.displayAreaPlaceholder.getChildren().setAll(this.welcomeMessage);
    }

    /**
     * Displays the RecipeDisplay on the swappable display region.
     */
    protected void displayRecipe(Recipe recipe) {
        var recipeDisplay = new RecipeDisplay(recipe);
        this.displayAreaPlaceholder.getChildren().setAll(recipeDisplay.getRoot());
        this.selectRecipeButton();
    }

    /**
     * Displays the RecipeViewPanel on the swappable display region.
     */
    protected void displayRecipeList() {
        this.displayAreaPlaceholder.getChildren().setAll(this.recipeViewPanel.getRoot());
        this.recipeViewPanel.refresh();
        this.selectRecipeButton();
    }

    /**
     * Displays the IngredientViewPanel on the swappable display region.
     */
    protected void displayIngredientList() {
        this.displayAreaPlaceholder.getChildren().setAll(this.ingredientViewPanel.getRoot());
        this.ingredientViewPanel.refresh();
        this.selectIngredientButton();
    }

    /**
     * Displays the RecommendationViewPanel on the swappable display region.
     */
    protected void displayRecommendationList() {
        this.displayAreaPlaceholder.getChildren().setAll(this.recommendationViewPanel.getRoot());
        this.recommendationViewPanel.refresh();
        this.selectRecommendationButton();
    }

    /**
     * Opens the notification window or focuses on it if it's already opened.
     */
    public void handleNotification() {
        var image = new ImageView(MainApp.class.getResource("/images/timer.png").toExternalForm());
        image.setFitHeight(40);
        image.setFitWidth(40);

        var alert = new Alert(Alert.AlertType.INFORMATION, NOTIFICATION_MESSAGE);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setGraphic(image);
        alert.showAndWait();
    }

    /**
     * Resets buttons in navigation bar to default style.
     */
    private void resetButtons() {
        this.recipeButton.getStyleClass().remove("tab-button-selected");
        this.ingredientButton.getStyleClass().remove("tab-button-selected");
        this.recommendationButton.getStyleClass().remove("tab-button-selected");
    }

    /**
     * Changes the recipe button style to selected.
     */
    private void selectRecipeButton() {
        this.resetButtons();
        this.recipeButton.getStyleClass().add("tab-button-selected");
    }

    private void selectIngredientButton() {
        this.resetButtons();
        this.ingredientButton.getStyleClass().add("tab-button-selected");
    }

    private void selectRecommendationButton() {
        this.resetButtons();
        this.recommendationButton.getStyleClass().add("tab-button-selected");
    }

    /**
     * Displays the recipe panel.
     */
    @FXML
    public void handleRecipePanel() {
        this.model.updateFilteredRecipeList(PREDICATE_SHOW_ALL_ENTRIES);
        this.displayRecipeList();
    }

    /**
     * Displays the recipe panel.
     */
    @FXML
    public void handleIngredientPanel() {
        this.model.updateFilteredIngredientList(PREDICATE_SHOW_ALL_ENTRIES);
        this.displayIngredientList();
    }

    /**
     * Displays the recommendations panel.
     */
    @FXML
    public void handleRecommendations() {
        this.model.updateFilteredRecipeList(PREDICATE_SHOW_ALL_ENTRIES);
        this.displayRecommendationList();
    }
}
