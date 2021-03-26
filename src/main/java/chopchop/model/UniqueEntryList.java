package chopchop.model;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import chopchop.commons.util.BulkEditableObservableList;
import chopchop.model.exceptions.DuplicateEntryException;
import chopchop.model.exceptions.EntryNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of entries that enforces uniqueness between its elements and does not allow nulls.
 * An entry is considered unique by comparing using {@code Entry#isSame(Entry)}. As such, adding and updating of
 * entries uses Entry#isSame(Entry) for equality so as to ensure that the entry being added or updated is
 * unique in terms of identity in the UniqueEntryList. However, the removal of an entry uses Entry#equals(Object) so
 * as to ensure that the entry with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Entry#isSame(Entry)
 */
public class UniqueEntryList<T extends Entry> implements Iterable<T> {
    private final BulkEditableObservableList<T> internalList = new BulkEditableObservableList<>();
    private final ObservableList<T> immutList = FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent entry as the given argument.
     */
    public boolean contains(T toCheck) {
        requireNonNull(toCheck);
        return this.internalList.stream().anyMatch(toCheck::isSame);
    }

    /**
     * Adds an entry to the list.
     * The entry must not already exist in the list.
     */
    public void add(T toAdd) {
        requireNonNull(toAdd);
        if (this.contains(toAdd)) {
            throw new DuplicateEntryException();
        }
        this.internalList.add(toAdd);
    }

    /**
     * Replaces the entry {@code target} in the list with {@code replacement}.
     * {@code target} must exist in the list.
     * The entry identity of {@code replacement} must not be the same as another existing entry in the list.
     */
    public void set(T target, T replacement) {
        requireAllNonNull(target, replacement);

        int index = this.internalList.indexOf(target);
        if (index == -1) {
            throw new EntryNotFoundException();
        }

        if (!target.isSame(replacement) && this.contains(replacement)) {
            throw new DuplicateEntryException();
        }

        this.internalList.set(index, replacement);
    }

    /**
     * Removes the equivalent entry from the list.
     * The entry must exist in the list.
     */
    public void remove(T toRemove) {
        requireNonNull(toRemove);
        if (!this.internalList.remove(toRemove)) {
            throw new EntryNotFoundException();
        }
    }

    public void setAll(UniqueEntryList<T> replacement) {
        requireNonNull(replacement);
        this.internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code entries}.
     * {@code entries} must not contain duplicate entries.
     */
    public void setAll(List<T> entries) {
        requireAllNonNull(entries);
        if (!this.entriesAreUnique(entries)) {
            throw new DuplicateEntryException();
        }

        this.internalList.setAll(entries);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<T> asUnmodifiableObservableList() {
        return this.immutList;
    }

    /**
     * Starts a bulk edit operation on the list. Each call to {@code startEditing} must be paired
     * with a corresponding call to {@code finishEditing}. These pairs can be nested.
     */
    public void startEditing() {
        this.internalList.startEditing();
    }

    /**
     * Finishes a bulk edit operation on the list. Each call to {@code finishEditing} must be paired
     * with a corresponding call to {@code startEditing}. These pairs can be nested.
     */
    public void finishEditing() {
        this.internalList.finishEditing();
    }

    @Override
    public Iterator<T> iterator() {
        return this.internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEntryList // instanceof handles nulls
                && this.internalList.equals(((UniqueEntryList<?>) other).internalList));
    }

    @Override
    public int hashCode() {
        return this.internalList.hashCode();
    }

    /**
     * Returns true if {@code entries} contains only unique entries.
     */
    private boolean entriesAreUnique(List<T> entries) {
        for (int i = 0; i < entries.size() - 1; i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                if (entries.get(i).isSame(entries.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
