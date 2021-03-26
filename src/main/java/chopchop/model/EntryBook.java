package chopchop.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;

/**
 * Wraps all data at the entry book level
 * Duplicates are not allowed (by .isSame comparison)
 */
public class EntryBook<T extends Entry> implements ReadOnlyEntryBook<T> {
    protected final UniqueEntryList<T> entries;

    public EntryBook() {
        this.entries = new UniqueEntryList<>();
    }

    /**
     * Creates an EntryBook using the Entries in the {@code toBeCopied}
     */
    public EntryBook(ReadOnlyEntryBook<T> toBeCopied) {
        this();
        this.resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the entries list with {@code entries}.
     * {@code entries} must not contain duplicate entries.
     */
    public void setAll(List<T> entries) {
        this.entries.setAll(entries);
    }

    /**
     * Resets the existing data of this {@code EntryBook} with {@code newData}.
     */
    public void resetData(ReadOnlyEntryBook<T> newData) {
        requireNonNull(newData);
        this.setAll(newData.getEntryList());
    }

    /**
     * Returns true if an entry with the same identity as {@code entry} exists in the entry book.
     */
    public boolean has(T entry) {
        requireNonNull(entry);
        return this.entries.contains(entry);
    }

    /**
     * Adds an entry to the entry book.
     * The entry must not already exist in the entry book.
     */
    public void add(T entry) {
        this.entries.add(entry);
    }

    /**
     * Replaces the given entry {@code target} in the list with {@code replacement}.
     * {@code target} must exist in the entry book.
     * The entry identity of {@code replacement} must not be the same as another existing entry in the entry book.
     */
    public void set(T target, T replacement) {
        requireNonNull(replacement);
        this.entries.set(target, replacement);
    }

    /**
     * Removes {@code entry} from this {@code EntryBook}.
     * {@code entry} must exist in the entry book.
     */
    public void remove(T entry) {
        this.entries.remove(entry);
    }

    /**
     * Starts a bulk edit operation on the entry list. Each call to {@code startEditing} must be paired
     * with a corresponding call to {@code finishEditing}. These pairs can be nested.
     */
    public void startEditing() {
        this.entries.startEditing();
    }

    /**
     * Finishes a bulk edit operation on the entry list. Each call to {@code finishEditing} must be paired
     * with a corresponding call to {@code startEditing}. These pairs can be nested.
     */
    public void finishEditing() {
        this.entries.finishEditing();
    }

    @Override
    public String toString() {
        return this.entries.asUnmodifiableObservableList().size() + " entries";
        // TODO: refine later
    }

    @Override
    public ObservableList<T> getEntryList() {
        return this.entries.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EntryBook
                && this.entries.equals(((EntryBook<?>) other).entries));
    }

    @Override
    public int hashCode() {
        return this.entries.hashCode();
    }
}
