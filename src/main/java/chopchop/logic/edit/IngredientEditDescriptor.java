package chopchop.logic.edit;

import java.util.List;
import java.util.Objects;

public class IngredientEditDescriptor {

    private final List<TagEditDescriptor> tagEdits;

    /**
     * Creates a IngredientEditDescriptor to edit an ingredient.
     *
     * @param tagEdits          the list of edit descriptors for tags
     */
    public IngredientEditDescriptor(List<TagEditDescriptor> tagEdits) {
        this.tagEdits = tagEdits;
    }

    public List<TagEditDescriptor> getTagEdits() {
        return this.tagEdits;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof IngredientEditDescriptor)) {
            return false;
        } else {
            var other = (IngredientEditDescriptor) obj;

            return Objects.equals(this.tagEdits, other.tagEdits);
        }
    }
}
