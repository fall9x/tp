// IncompatibleIngredientsException.java

package chopchop.model.ingredient.exceptions;

/**
 * An exception that is thrown when attempting to add ingredients with incompatible
 * quantities (eg. volume and mass) or names.
 */
public class IncompatibleIngredientsException extends Exception {

    public IncompatibleIngredientsException(String message) {
        super(message);
    }
}
