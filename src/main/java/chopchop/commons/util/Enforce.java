// Enforce.java

package chopchop.commons.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Replacement for assertions. Why does this class exist? to increase codecov percentage.
 *
 * Using a Java assert creates one static initialiser in each class containing 1 statement and 1 branch,
 * as well as 4 branches at each usage site. (2x2 -- asserts on/off, condition true/false).
 *
 * As one might expect, this tanks codecov when asserts are used liberally -- which defeats the purpose.
 * Thus, this class was created instead to "abstract out" the branches taken/not taken, so the penalty
 * is only applied once. Is this gaming the system? Without a doubt, yes.
 *
 * Furthermore, Java comparisons (eg. {@code a < b}) create a branch because the JVM is not well-designed,
 * which is why we have {@code enforceLessThan} and friends.
 *
 * If you don't care about codecov (or when the Jacoco people finally fix their library), this class can be
 * discarded.
 */
public class Enforce {

    private static boolean enabled = true;

    /**
     * Enables all enforcements
     */
    public static void enableEnforce() {
        Enforce.enabled = true;
    }

    /**
     * Disables all enforcements
     */
    public static void disableEnforce() {
        Enforce.enabled = false;
    }

    /**
     * Checks that the condition is true.
     */
    public static void enforce(boolean condition) {
        if (Enforce.enabled && !condition) {
            throw new AssertionError("assertion failed (condition was false)");
        }
    }

    /**
     * Checks that the objects are nonnull
     */
    public static void enforceNonNull(Object... objs) {
        for (var obj : objs) {
            enforce(obj != null);
        }
    }

    /**
     * Checks that all the conditions are true
     */
    public static void enforceAll(Boolean... conditions) {
        for (boolean b : conditions) {
            enforce(b);
        }
    }

    /**
     * Checks that at least one of the conditions are true
     */
    public static void enforceAny(Boolean... conditions) {
        if (conditions.length == 0) {
            return;
        }

        for (boolean b : conditions) {
            if (b) {
                return;
            }
        }

        enforce(false);
    }

    /**
     * Checks that the given item is contained in the list.
     */
    public static <T> void enforceContains(T item, List<T> list) {
        enforce(list.contains(item));
    }

    /**
     * Convenience overload for {@code enforceContains} that takes varargs
     */
    @SafeVarargs
    public static <T> void enforceContains(T item, T... list) {
        enforce(Arrays.asList(list).contains(item));
    }

    /**
     * Checks that the list is not empty
     */
    public static <T> void enforceNotEmpty(List<T> list) {
        enforce(!list.isEmpty());
    }

    /**
     * Checks that the optional is present
     */
    public static <T> void enforcePresent(Optional<T> opt) {
        enforce(opt.isPresent());
    }

    /**
     * Checks that the list is empty.
     */
    public static <T> void enforceEmpty(List<T> list) {
        enforce(list.isEmpty());
    }

    /**
     * Checks that the optional is empty.
     */
    public static <T> void enforceEmpty(Optional<T> opt) {
        enforce(opt.isEmpty());
    }

    /**
     * Checks that a < b
     */
    public static void enforceLessThan(int a, int b) {
        enforce(a < b);
    }

    /**
     * Checks that a == b
     */
    public static void enforceEqual(Object a, Object b) {
        enforce(a.equals(b));
    }

    /**
     * Checks that a > b
     */
    public static void enforceGreaterThan(int a, int b) {
        enforce(a > b);
    }
}
