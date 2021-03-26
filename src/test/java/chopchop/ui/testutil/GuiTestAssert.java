package chopchop.ui.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import chopchop.model.attributes.Tag;
import chopchop.model.ingredient.Ingredient;
import chopchop.ui.GuiUnitTest;
import guitests.guihandles.CommandOutputHandle;
import guitests.guihandles.IngredientCardHandle;
import guitests.guihandles.RecipeCardHandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert extends GuiUnitTest {

    private static Comparator<Tag> tagComparator = new Comparator<Tag>() {
        @Override
        public int compare(Tag o1, Tag o2) {
            String tagName1 = o1.toString();
            String tagName2 = o2.toString();
            return tagName1.compareTo(tagName2);
        }
    };

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertRecipeCardEquals(RecipeCardHandle expectedCard, RecipeCardHandle actualCard) {
        assertEquals(expectedCard.getName(), actualCard.getName());
    }

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardDisplaysIngredient(Ingredient expectedCard, IngredientCardHandle actualCard) {
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getExpiryDate().get().toString(), actualCard.getExpiryDate());
        assertEquals(expectedCard.getQuantity().toString(), actualCard.getQty());
        List<Tag> expectedTags = new ArrayList<>(expectedCard.getTags());
        Collections.sort(expectedTags, tagComparator);
        assertEquals(expectedTags.toString(), actualCard.getTags().toString());
    }

    /**
     * Asserts the message shown in {@code commandOutputHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(CommandOutputHandle commandOutputHandle, String expected) {
        assertEquals(expected, commandOutputHandle.getText());
    }

}

