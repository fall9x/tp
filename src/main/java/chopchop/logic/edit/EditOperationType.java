// EditOperationType.java

package chopchop.logic.edit;

public enum EditOperationType {
    ADD,
    EDIT,
    DELETE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
