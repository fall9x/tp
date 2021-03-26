// StepEditDescriptor.java

package chopchop.logic.edit;

import static chopchop.commons.util.Enforce.enforce;
import static chopchop.commons.util.Enforce.enforceContains;
import static chopchop.commons.util.Enforce.enforcePresent;

import java.util.Optional;


public class StepEditDescriptor extends EditDescriptor {

    private final Optional<Integer> stepNumber;
    private final String stepText;

    /**
     * Creates a new descriptor representing step editing. If the {@code stepNumber} is empty,
     * then the step should be appended to the end of the recipe's existing steps.
     *
     * @param editType   the type of edit
     * @param stepNumber the 1-based index of the step to edit.
     * @param stepText   the new content of the step
     */
    public StepEditDescriptor(EditOperationType editType, Optional<Integer> stepNumber, String stepText) {

        super(editType);

        enforceContains(editType, EditOperationType.ADD, EditOperationType.EDIT, EditOperationType.DELETE);

        if (editType == EditOperationType.ADD) {
            enforce(!stepText.isEmpty());
        } else if (editType == EditOperationType.EDIT) {
            enforce(!stepText.isEmpty());
            enforcePresent(stepNumber);
        } else {
            enforcePresent(stepNumber);
            enforce(stepText.isEmpty());
        }

        this.stepNumber = stepNumber;
        this.stepText = stepText;
    }

    public String getStepText() {
        return this.stepText;
    }

    public Optional<Integer> getStepNumber() {
        return this.stepNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof StepEditDescriptor)) {
            return false;
        } else {
            var other = (StepEditDescriptor) obj;
            return this.getEditType() == other.getEditType()
                && this.stepNumber.equals(other.stepNumber)
                && this.stepText.equals(other.stepText);
        }
    }
}

