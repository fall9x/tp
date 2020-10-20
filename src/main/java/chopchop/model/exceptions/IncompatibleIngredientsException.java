package chopchop.model.exceptions;

/**
 * An exception that is thrown when attempting to add ingredients with incompatible
 * quantities (eg. volume and mass) or names.
 */
public class IncompatibleIngredientsException extends RuntimeException {
    public IncompatibleIngredientsException(String message) {
        super(message);
    }
}
