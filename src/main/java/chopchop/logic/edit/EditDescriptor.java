// EditDescriptor.java

package chopchop.logic.edit;

public abstract class EditDescriptor {

    private final EditOperationType editType;

    public EditDescriptor(EditOperationType editType) {
        this.editType = editType;
    }

    public EditOperationType getEditType() {
        return this.editType;
    }
}
