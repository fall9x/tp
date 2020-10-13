package chopchop.ui;

import chopchop.logic.Logic;
import chopchop.model.recipe.Recipe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Controller class for the swappable display region.
 */
public class DisplayController extends UiPart<Region> {

    private static final String FXML = "DisplayPanel.fxml";
    private static final String WELCOME_MESSAGE = "Welcome to ChopChop! If you need any help, press 'F1'";

    private final TextDisplay textDisplay;
    private Logic logic;
    private NotificationWindow notificationWindow;

    @FXML
    private StackPane displayAreaPlaceholder;

    /**
     * Creates a {@code DisplayController} with the given {@code Logic}.
     * @param logic
     */
    public DisplayController(Logic logic) {
        super(FXML);
        this.logic = logic;
        textDisplay = new TextDisplay(WELCOME_MESSAGE);
        notificationWindow = new NotificationWindow();
        displayAreaPlaceholder.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    displayWelcomeMessage();
                }
            }
        });
        displayWelcomeMessage();
    }

    /**
     * Displays the RecipeViewPanel on the swappable display region.
     */
    protected void displayWelcomeMessage() {
        displayAreaPlaceholder.getChildren().setAll(textDisplay.getRoot());
    }

    /**
     * Displays the RecipeViewPanel on the swappable display region.
     */
    protected void displayRecipeList() {
        RecipeViewPanel recipeViewPanel = new RecipeViewPanel(logic.getFilteredRecipeList());
        displayAreaPlaceholder.getChildren().setAll(recipeViewPanel.getRoot());
    }

    /**
     * Displays the RecipeDisplay on the swappable display region.
     */
    protected void displayRecipe(Recipe recipe) {
        RecipeDisplay recipeDisplay = new RecipeDisplay(recipe);
        displayAreaPlaceholder.getChildren().setAll(recipeDisplay.getRoot());
    }

    /**
     * Displays the IngredientViewPanel on the swappable display region.
     */
    protected void displayIngredientList() {
        IngredientViewPanel ingredientViewPanel = new IngredientViewPanel(logic.getFilteredIngredientList());
        displayAreaPlaceholder.getChildren().setAll(ingredientViewPanel.getRoot());
    }

    /**
     * Opens the notification window or focuses on it if it's already opened.
     */
    public void handleNotification() {
        if (!notificationWindow.isShowing()) {
            notificationWindow.show();
        } else {
            notificationWindow.focus();
        }
    }

    /**
     * Displays the recipe panel.
     */
    @FXML
    public void handleRecipePanel(ActionEvent event) {
        displayRecipeList();
    }


    /**
     * Displays the recipe panel.
     */
    @FXML
    public void handleIngredientPanel(ActionEvent event) {
        displayIngredientList();
    }

    /**
     * Displays the recommendations panel.
     */
    @FXML
    public void handleRecommendations(ActionEvent event) {
        // To add more code.
        handleNotification();
    }

    /**
     * Displays the favourites panel.
     */
    @FXML
    public void handleFavourites(ActionEvent event) {
        // To add more code.
        handleNotification();
    }
}
