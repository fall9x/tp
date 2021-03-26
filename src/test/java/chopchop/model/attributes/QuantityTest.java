// QuantityTest.java

package chopchop.model.attributes;

import java.util.Map;

import chopchop.commons.util.Pair;
import chopchop.commons.util.Result;
import chopchop.model.attributes.units.Mass;
import chopchop.model.attributes.units.Count;
import chopchop.model.attributes.units.Volume;
import chopchop.model.exceptions.IncompatibleIngredientsException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static chopchop.testutil.Assert.assertThrows;

public class QuantityTest {

    @Test
    void equals_sameUnits_success() {

        assertEquals(Mass.grams(133),       Mass.kilograms(0.133));
        assertEquals(Mass.kilograms(23),    Mass.grams(23000));

        assertEquals(Volume.cups(3.2),      Volume.millilitres(800));
        assertEquals(Volume.litres(67.33),  Volume.millilitres(67330));
        assertEquals(Volume.teaspoons(3),   Volume.litres(0.015));

        assertEquals(Count.of(3),           Count.of(3));
    }

    @Test
    void equals_notSameUnits_failure() {
        assertNotEquals(Mass.grams(133),        Mass.kilograms(133));
        assertNotEquals(Mass.kilograms(23),     Mass.grams(23));

        assertNotEquals(Volume.cups(3.2),       Volume.millilitres(3.2));
        assertNotEquals(Volume.teaspoons(3),    Volume.litres(2));

        assertNotEquals(Count.of(3),            Count.of(1));

        assertNotEquals(Mass.grams(133),        Count.of(133));
        assertNotEquals(Mass.kilograms(23),     Volume.tablespoons(23));
        assertNotEquals(Volume.litres(133),     Count.of(133));
    }

    @Test
    void parse_validUnits_success() {

        var tests = Map.of(
            "10 g",     Mass.grams(10),
            "3.1kg",    Mass.grams(3100),
            "60kg",     Mass.grams(60000),

            "40ml",     Volume.millilitres(40),
            "3.7tbsp",  Volume.tablespoons(3.7),
            "1 cup",    Volume.teaspoons(50),
            "2 cups",   Volume.litres(0.5),

            "3",        Count.of(3)
        );

        tests.forEach((k, v) -> {
            var parsed = Quantity.parse(k);

            assertTrue(parsed.hasValue());
            assertEquals(v, parsed.getValue());
        });
    }

    @Test
    void parse_invalidUnits_failure() {
        var tests = Map.of(
            "10 lg",        "Unknown unit 'lg' (from '10 lg')",
            "3.1.4 cups",   "Couldn't parse number from quantity '3.1.4 cups': multiple points",
            "",             "Quantity string cannot be empty"
        );

        tests.forEach((k, v) -> {
            assertEquals(Result.error(v), Quantity.parse(k));
        });
    }

    @Test
    void add_compatibleUnits_success() {
        var tests = Map.of(
            Pair.of("185 g", "3.7 kg"),     Mass.kilograms(3.885),
            Pair.of("185 g", "-53g"),       Mass.grams(132),
            Pair.of("45ml", "3tsp"),        Volume.tablespoons(4),
            Pair.of("3 cups", "1 cup"),     Volume.litres(1),
            Pair.of("1", "7"),              Count.of(8)
        );

        tests.forEach((k, v) -> {
            var a = k.fst();
            var b = k.snd();
            assertEquals(Result.of(v), Quantity.parse(a)
                .then(x -> Quantity.parse(b).then(y -> x.add(y)))
            );
        });
    }

    @Test
    void add_incompatibleUnits_failure() {
        var tests = Map.of(
            Pair.of("185 g", "3.7 mL"),     "Cannot add '3.7mL' to '185g' (incompatible units)",
            Pair.of("1", "7g"),             "Cannot add '7g' to '1' (incompatible units)",
            Pair.of("33ml", "400"),         "Cannot add '400' to '33mL' (incompatible units)"
        );

        tests.forEach((k, v) -> {
            var a = k.fst();
            var b = k.snd();
            assertEquals(Result.error(v), Quantity.parse(a)
                .then(x -> Quantity.parse(b).then(y -> x.add(y)))
            );
        });
    }

    @Test
    void test_values() {
        assertTrue(Count.of(0).isZero());
        assertTrue(Count.of(-1).isNegative());

        assertFalse(Count.of(-3).isZero());
        assertFalse(Count.of(7).isNegative());


        assertTrue(Mass.grams(0).isZero());
        assertTrue(Mass.grams(-1).isNegative());

        assertFalse(Mass.grams(-3).isZero());
        assertFalse(Mass.grams(7).isNegative());


        assertTrue(Volume.cups(0).isZero());
        assertTrue(Volume.cups(-1).isNegative());

        assertFalse(Volume.cups(-3).isZero());
        assertFalse(Volume.cups(7).isNegative());
    }

    @Test
    void test_arithmetic() {
        assertTrue(Count.of(1).add(Count.of(1).negate()).getValue().isZero());
        assertTrue(Mass.kilograms(1).add(Mass.kilograms(1).negate()).getValue().isZero());
        assertTrue(Volume.litres(1).add(Volume.litres(1).negate()).getValue().isZero());

        assertTrue(Count.of(1).subtract(Count.of(1.1)).getValue().isNegative());
        assertTrue(Mass.kilograms(1).subtract(Mass.kilograms(1.1)).getValue().isNegative());
        assertTrue(Volume.litres(1).subtract(Volume.litres(1.1)).getValue().isNegative());
    }

    @Test
    void test_compatibility() {
        assertTrue(Count.of(1).compatibleWith(Count.of(70)));
        assertTrue(Mass.milligrams(1).compatibleWith(Mass.milligrams(70)));
        assertTrue(Volume.tablespoons(1).compatibleWith(Volume.tablespoons(70)));

        assertFalse(Count.of(1).compatibleWith(Mass.milligrams(70)));
        assertFalse(Mass.milligrams(1).compatibleWith(Volume.tablespoons(1)));
        assertFalse(Volume.tablespoons(1).compatibleWith(Count.of(1)));
    }

    @Test
    void test_of() {
        assertEquals(Result.of(Mass.milligrams(37)), Mass.of(37, "mg"));
        assertEquals(Result.of(Mass.grams(100)), Mass.of(100, "g"));
        assertEquals(Result.of(Mass.kilograms(48)), Mass.of(48, "kg"));

        assertEquals(Result.of(Volume.millilitres(37)), Volume.of(37, "ml"));
        assertEquals(Result.of(Volume.litres(100)), Volume.of(100, "L"));
        assertEquals(Result.of(Volume.cups(48)), Volume.of(48, "cups"));
        assertEquals(Result.of(Volume.teaspoons(31)), Volume.of(31, "tsp"));
        assertEquals(Result.of(Volume.tablespoons(19)), Volume.of(19, "tbsp"));
    }

    @Test
    void test_compare() {
        assertEquals(0,  Mass.grams(600).compareTo(Mass.kilograms(0.6)));
        assertEquals(1,  Mass.grams(601).compareTo(Mass.kilograms(0.6)));
        assertEquals(-1, Mass.grams(599).compareTo(Mass.kilograms(0.6)));

        assertEquals(0,  Count.of(6.0).compareTo(Count.of(6)));
        assertEquals(1,  Count.of(6.1).compareTo(Count.of(6)));
        assertEquals(-1, Count.of(5.9).compareTo(Count.of(6)));

        assertEquals(0,  Volume.millilitres(600).compareTo(Volume.litres(0.6)));
        assertEquals(1,  Volume.millilitres(601).compareTo(Volume.litres(0.6)));
        assertEquals(-1, Volume.millilitres(599).compareTo(Volume.litres(0.6)));

        assertThrows(IncompatibleIngredientsException.class, () -> Count.of(1).compareTo(Mass.grams(1)));
        assertThrows(IncompatibleIngredientsException.class, () -> Mass.grams(1).compareTo(Volume.litres(1)));
        assertThrows(IncompatibleIngredientsException.class, () -> Volume.cups(1).compareTo(Count.of(1)));
    }

    @Test
    void test_toString() {
        var cases = Map.of(
            Count.of(1),                "1",
            Mass.grams(35.1),           "35.1g",
            Mass.milligrams(300),       "300mg",
            Mass.kilograms(25),         "25kg",

            Volume.litres(35.1),        "35.1L",
            Volume.millilitres(300),    "300mL",
            Volume.cups(1),             "1cup",
            Volume.cups(3),             "3cups",
            Volume.tablespoons(3.5),    "3.5tbsp",
            Volume.teaspoons(1.1),      "1.1tsp"
        );

        cases.forEach((k, v) -> {
            assertEquals(v, k.toString());
        });

        cases = Map.of(
            Mass.grams(3.14),           "3.14g",
            Mass.grams(3.1415),         "3.142g"
        );

        cases.forEach((k, v) -> {
            assertEquals(v, k.toString());
        });
    }
}
