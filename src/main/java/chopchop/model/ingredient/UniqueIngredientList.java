package chopchop.model.ingredient;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;
import java.util.Iterator;
import java.util.List;

import chopchop.model.ingredient.exceptions.DuplicateIngredientException;
import chopchop.model.ingredient.exceptions.IngredientNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UniqueIngredientList implements Iterable<Ingredient> {

    private final ObservableList<Ingredient> internalList = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> internalUnmodifiableList =
        FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent Ingredient as the given argument.
     */
    public boolean contains(Ingredient toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Searches for an ingredient by name in this list, and returns its index. Returns -1
     * if an ingredient with the given name could not be found.
     *
     * Name comparison is case-insensitive.
     */
    public int indexByName(String name) {
        for (int i = 0; i < this.internalList.size(); i++) {
            if (this.internalList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds a Ingredient to the list.
     * The Ingredient must not already exist in the list.
     */
    public void add(Ingredient toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateIngredientException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the Ingredient {@code target} in the list with {@code editedIngredient}.
     * {@code target} must exist in the list.
     * The Ingredient identity of {@code editedIngredient} must not be the same as another existing
     * Ingredient in the list.
     */
    public void setIngredient(Ingredient target, Ingredient editedIngredient) {
        requireAllNonNull(target, editedIngredient);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new IngredientNotFoundException();
        }

        if (!target.equals(editedIngredient) && contains(editedIngredient)) {
            throw new DuplicateIngredientException();
        }

        internalList.set(index, editedIngredient);
    }

    /**
     * Removes the equivalent Ingredient from the list.
     * The Ingredient must exist in the list.
     */
    public void remove(Ingredient toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new IngredientNotFoundException();
        }
    }

    public void setIngredients(UniqueIngredientList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code Ingredients}.
     * {@code Ingredients} must not contain duplicate Ingredients.
     */
    public void setIngredientEntries(List<Ingredient> ingredients) {
        requireAllNonNull(ingredients);
        if (!ingredientsAreUnique(ingredients)) {
            throw new DuplicateIngredientException();
        }

        internalList.setAll(ingredients);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Ingredient> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof UniqueIngredientList // instanceof handles nulls
            && internalList.equals(((UniqueIngredientList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code Ingredients} contains only unique Ingredients.
     */
    private boolean ingredientsAreUnique(List<Ingredient> ingredients) {
        for (int i = 0; i < ingredients.size() - 1; i++) {
            for (int j = i + 1; j < ingredients.size(); j++) {
                if (ingredients.get(i).equals(ingredients.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
