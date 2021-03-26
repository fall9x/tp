// BulkEditableObservableList.java

package chopchop.commons.util;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ModifiableObservableListBase;

/**
 * A wrapper around an {@code ObservableList<T>} that supports bulk editing operations. Otherwise, this is
 * identical to an {@code ObservableList} from JavaFX.
 *
 * To start a bulk editing operation, call {@code startEditing()}, and {@code finishEditing()} to finish it.
 * Any edits (add/set/remove) to the list between those calls will be batched up, and the listener (and thus
 * all of the JavaFX machinery) is only called once, after the edit finishes.
 *
 * These calls can be nested arbitrarily, but they must be paired and closed properly.
 *
 * This container is backed by an {@code ArrayList}.
 */
public class BulkEditableObservableList<T> extends ModifiableObservableListBase<T> {

    private int editNesting = 0;
    private final List<T> list;

    /**
     * Creates an empty list.
     */
    public BulkEditableObservableList() {
        this.list = new ArrayList<>();
    }

    /**
     * Creates a list by copying items from the given list.
     *
     * @param items the input list
     */
    public BulkEditableObservableList(List<T> items) {
        this.list = new ArrayList<>(items);
    }

    /**
     * Starts a bulk edit operation on the list. Each call to {@code startEditing} must be paired
     * with a corresponding call to {@code finishEditing}. These pairs can be nested.
     */
    public void startEditing() {
        if (this.editNesting == 0) {
            super.beginChange();
        }

        this.editNesting++;
    }

    /**
     * Finishes a bulk edit operation on the entry list. Each call to {@code finishEditing} must be paired
     * with a corresponding call to {@code startEditing}. These pairs can be nested.
     */
    public void finishEditing() {
        this.editNesting--;

        if (this.editNesting == 0) {
            super.endChange();
        }
    }

    @Override
    public T get(int index) {
        return this.list.get(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    protected void doAdd(int index, T item) {
        this.list.add(index, item);
    }

    @Override
    public T doSet(int index, T item) {
        return this.list.set(index, item);
    }

    @Override
    public T doRemove(int index) {
        return this.list.remove(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof BulkEditableObservableList<?>)) {
            return false;
        }

        return this.list.equals(((BulkEditableObservableList<?>) obj).list);
    }
}
