package chopchop.ui.testutil;

import chopchop.model.attributes.Step;
import chopchop.model.ingredient.IngredientReference;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of utils for parsing Lists.
 */
public class GuiTestUtil {
    /**
     * Parse a List of {@code Text} into a {@code String}.
     */
    public static String parseTextToString(List<Node> nodeList) {
        StringBuilder str = new StringBuilder();
        for (Node node : nodeList) {
            if (node instanceof Text) {
                str.append(((Text) node).getText());
            }
        }
        return str.toString();
    }

    /**
     * Parse a List of {@code IngredientReference} into a {@code String}.
     */
    public static String parseIngredientsToString(List<IngredientReference> ingredientReferenceList) {
        return ingredientReferenceList.stream()
                .map(IngredientReference::toString)
                .collect(Collectors.joining());
    }

    /**
     * Parse a List of {@code Step} into a {@code String}.
     */
    public static String parseStepList(List<Step> stepList) {
        return stepList.stream()
                .map(Step::toString)
                .collect(Collectors.joining());
    }
}
