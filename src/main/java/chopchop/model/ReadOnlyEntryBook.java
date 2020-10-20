package chopchop.model;

import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an entry book
 */
public interface ReadOnlyEntryBook<T extends Entry> {
    /**
     * Returns an unmodifiable view of the entries list.
     * This list will not contain any duplicate entries.
     */
    ObservableList<T> getEntryList();
}
