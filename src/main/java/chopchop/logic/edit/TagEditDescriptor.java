// TagEditDescriptor.java

package chopchop.logic.edit;

import static chopchop.commons.util.Enforce.enforce;
import static chopchop.commons.util.Enforce.enforceContains;

import chopchop.model.attributes.Tag;

public class TagEditDescriptor extends EditDescriptor {

    private final String tagName;

    /**
     * Creates a new descriptor representing tag editing.
     *
     * @param editType the type of edit; must be either ADD or DELETE
     * @param tag      the tag name; it must be valid according to Tag::isValidTagName
     */
    public TagEditDescriptor(EditOperationType editType, String tag) {
        super(editType);

        enforce(Tag.isValidTag(tag));
        enforceContains(editType, EditOperationType.ADD, EditOperationType.DELETE);

        this.tagName = tag;
    }

    public String getTagName() {
        return this.tagName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof TagEditDescriptor)) {
            return false;
        } else {
            var other = (TagEditDescriptor) obj;
            return this.getEditType() == other.getEditType()
                && this.tagName.equalsIgnoreCase(other.tagName);
        }
    }
}
