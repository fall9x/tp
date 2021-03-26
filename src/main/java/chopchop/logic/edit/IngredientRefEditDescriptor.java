// IngredientEditDescriptor.java

package chopchop.logic.edit;

import static chopchop.commons.util.Enforce.enforce;
import static chopchop.commons.util.Enforce.enforceContains;
import static chopchop.commons.util.Enforce.enforceEmpty;
import static chopchop.commons.util.Enforce.enforcePresent;

import java.util.Optional;

import chopchop.model.attributes.Quantity;

public class IngredientRefEditDescriptor extends EditDescriptor {

    private final Optional<Quantity> ingredientQuantity;
    private final String ingredientName;

    /**
     * Creates a new descriptor representing ingredient editing.
     *
     * @param editType the type of edit
     * @param name     the name of the ingredient to edit
     * @param qty      the new quantity of the ingredient; should only be present iff type is EDIT or ADD
     */
    public IngredientRefEditDescriptor(EditOperationType editType, String name, Optional<Quantity> qty) {

        super(editType);

        enforce(!name.isEmpty());
        enforceContains(editType, EditOperationType.ADD, EditOperationType.EDIT, EditOperationType.DELETE);

        if (editType == EditOperationType.EDIT || editType == EditOperationType.ADD) {
            enforcePresent(qty);
        } else {
            enforceEmpty(qty);
        }

        this.ingredientName = name;
        this.ingredientQuantity = qty;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public Optional<Quantity> getIngredientQuantity() {
        return this.ingredientQuantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof IngredientRefEditDescriptor)) {
            return false;
        } else {
            var other = (IngredientRefEditDescriptor) obj;
            return this.getEditType() == other.getEditType()
                && this.ingredientQuantity.equals(other.ingredientQuantity)
                && this.ingredientName.equalsIgnoreCase(other.ingredientName);
        }
    }
}
