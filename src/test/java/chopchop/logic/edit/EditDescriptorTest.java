// EditDescriptorTest.java

package chopchop.logic.edit;

import java.util.List;
import java.util.Optional;

import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.units.Volume;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;

public class EditDescriptorTest {

    private static EditOperationType opAdd = EditOperationType.ADD;
    private static EditOperationType opEdit = EditOperationType.EDIT;
    private static EditOperationType opDelete = EditOperationType.DELETE;

    private IngredientRefEditDescriptor ied(EditOperationType op, String name, Quantity q) {
        return new IngredientRefEditDescriptor(op, name, Optional.ofNullable(q));
    }

    private TagEditDescriptor ted(EditOperationType op, String name) {
        return new TagEditDescriptor(op, name);
    }

    private StepEditDescriptor sed(EditOperationType op, Optional<Integer> n, String t) {
        return new StepEditDescriptor(op, n, t);
    }


    @Test
    void test_ingredientEdits() {
        assertEquals("add", EditOperationType.ADD.toString());
        assertEquals("edit", EditOperationType.EDIT.toString());
        assertEquals("delete", EditOperationType.DELETE.toString());

        var ia1 = ied(opAdd, "custard", Volume.cups(1));
        var ia2 = ied(opAdd, "custard", Volume.cups(3));
        var ia3 = ied(opAdd, "milk", Volume.cups(1));
        var ia4 = ied(opAdd, "custard", Volume.cups(1));

        var ie1 = ied(opEdit, "custard", Volume.cups(1));
        var ie2 = ied(opEdit, "custard", Volume.cups(3));
        var ie3 = ied(opEdit, "milk", Volume.cups(1));
        var ie4 = ied(opEdit, "custard", Volume.cups(1));

        var id1 = ied(opDelete, "custard", null);
        var id2 = ied(opDelete, "milk", null);
        var id3 = ied(opDelete, "custard", null);

        assertThrows(AssertionError.class, () -> ied(opAdd, "", Volume.cups(1)));
        assertThrows(AssertionError.class, () -> ied(opAdd, "asdf", null));
        assertThrows(AssertionError.class, () -> ied(opEdit, "asdf", null));
        assertThrows(AssertionError.class, () -> ied(opDelete, "asdf", Volume.cups(1)));

        assertEquals(ia1, ia1);
        assertEquals(ia1, ia4);
        assertNotEquals(ia1, ia2);
        assertNotEquals(ia1, ia3);
        assertNotEquals(ia1, "custard");

        assertEquals(ie1, ie1);
        assertEquals(ie1, ie4);
        assertNotEquals(ie1, ie2);
        assertNotEquals(ie1, ie3);
        assertNotEquals(ie1, "custard");

        assertEquals(id1, id1);
        assertEquals(id1, id3);
        assertNotEquals(id1, id2);
        assertNotEquals(id1, "custard");

        assertNotEquals(ia1, ie1);
    }

    @Test
    void test_tagEdits() {
        var ta1 = ted(opAdd, "liquid");
        var ta2 = ted(opAdd, "liquid");
        var ta3 = ted(opAdd, "weird");

        var td1 = ted(opDelete, "liquid");
        var td2 = ted(opDelete, "liquid");
        var td3 = ted(opDelete, "weird");

        assertThrows(AssertionError.class, () -> ted(opEdit, "asdf"));
        assertThrows(AssertionError.class, () -> ted(opAdd, ""));
        assertThrows(AssertionError.class, () -> ted(opDelete, ""));

        assertEquals(ta1, ta1);
        assertEquals(ta1, ta2);
        assertNotEquals(ta1, ta3);
        assertNotEquals(ta1, "liquid");

        assertEquals(td1, td1);
        assertEquals(td1, td2);
        assertNotEquals(td1, td3);
        assertNotEquals(td1, "liquid");

        assertNotEquals(ta1, td1);
    }

    @Test
    void test_stepEdits() {

        var sa1 = sed(opAdd, Optional.of(1), "owo");
        var sa2 = sed(opAdd, Optional.empty(), "owo");
        var sa3 = sed(opAdd, Optional.of(7), "owo");
        var sa4 = sed(opAdd, Optional.of(1), "uwu");
        var sa5 = sed(opAdd, Optional.of(1), "owo");

        var se1 = sed(opEdit, Optional.of(1), "owo");
        var se2 = sed(opEdit, Optional.of(7), "owo");
        var se3 = sed(opEdit, Optional.of(1), "uwu");
        var se4 = sed(opEdit, Optional.of(1), "owo");

        var sd1 = sed(opDelete, Optional.of(1), "");
        var sd2 = sed(opDelete, Optional.of(7), "");
        var sd3 = sed(opDelete, Optional.of(1), "");

        assertThrows(AssertionError.class, () -> sed(opAdd, Optional.of(1), ""));
        assertThrows(AssertionError.class, () -> sed(opEdit, Optional.empty(), ""));
        assertThrows(AssertionError.class, () -> sed(opEdit, Optional.of(1), ""));
        assertThrows(AssertionError.class, () -> sed(opDelete, Optional.empty(), ""));
        assertThrows(AssertionError.class, () -> sed(opDelete, Optional.of(1), "asdf"));

        assertEquals(sa1, sa1);
        assertEquals(sa1, sa5);
        assertNotEquals(sa1, sa2);
        assertNotEquals(sa1, sa3);
        assertNotEquals(sa1, sa4);
        assertNotEquals(sa1, "custard");

        assertEquals(se1, se1);
        assertEquals(se1, se4);
        assertNotEquals(se1, se2);
        assertNotEquals(se1, se3);
        assertNotEquals(se1, "custard");

        assertEquals(sd1, sd1);
        assertEquals(sd1, sd3);
        assertNotEquals(sd1, sd2);
        assertNotEquals(sd1, "custard");

        assertNotEquals(sa1, se1);
    }


    @Test
    void test_recipeEdits() {
        var ieds1 = List.of(ied(opAdd, "custard", Volume.cups(1)), ied(opEdit, "milk", Volume.cups(1)));
        var ieds2 = List.of(ied(opAdd, "cream", Volume.cups(1)), ied(opEdit, "sugar", Volume.cups(1)));

        var teds1 = List.of(ted(opAdd, "wet"), ted(opDelete, "weird"));
        var teds2 = List.of(ted(opAdd, "dry"), ted(opDelete, "normal"));

        var seds1 = List.of(sed(opAdd, Optional.of(1), "custard"), sed(opEdit, Optional.of(2), "milk"));
        var seds2 = List.of(sed(opAdd, Optional.of(1), "cream"), sed(opEdit, Optional.of(2), "sugar"));

        var re1 = new RecipeEditDescriptor(Optional.of("asdf"), ieds1, seds1, teds1);
        var re2 = new RecipeEditDescriptor(Optional.of("asdf"), ieds1, seds1, teds1);
        var re3 = new RecipeEditDescriptor(Optional.of("asdf"), ieds2, seds2, teds2);
        var re4 = new RecipeEditDescriptor(Optional.of("asdf"), ieds1, seds1, teds2);
        var re5 = new RecipeEditDescriptor(Optional.of("asdf"), ieds1, seds2, teds1);
        var re6 = new RecipeEditDescriptor(Optional.of("asdf"), ieds2, seds1, teds1);

        assertEquals(re1, re1);
        assertEquals(re1, re2);
        assertNotEquals(re1, "asdf");
        assertNotEquals(re1, re3);
        assertNotEquals(re1, re4);
        assertNotEquals(re1, re5);
        assertNotEquals(re1, re6);
    }
}
