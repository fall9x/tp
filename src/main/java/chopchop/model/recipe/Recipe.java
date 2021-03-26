package chopchop.model.recipe;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chopchop.model.Entry;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Step;
import chopchop.model.attributes.Tag;
import chopchop.model.ingredient.IngredientReference;

public class Recipe extends Entry {
    private final List<IngredientReference> ingredients;
    private final List<Step> steps;
    private final Set<Tag> tags;

    /**
     * Every field must be present and not null.
     */
    public Recipe(String name, List<IngredientReference> ingredients, List<Step> steps, Set<Tag> tags) {
        super(name);
        requireAllNonNull(name, ingredients, steps, tags);
        this.ingredients = new ArrayList<>(ingredients);
        this.steps = new ArrayList<>(steps);
        this.tags = new HashSet<>(tags);
    }

    /**
     * Returns an immutable ingredient set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<IngredientReference> getIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }

    /**
     * Returns an immutable step list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Step> getSteps() {
        return Collections.unmodifiableList(this.steps);
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    @Override
    public Optional<ExpiryDate> getExpiryDate() {
        return Optional.empty();
    }

    @Override
    public boolean isSame(Entry other) {
        return other == this
            || (other instanceof Recipe
            && this.name.equals(((Recipe) other).name));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
            || (other instanceof Recipe
            && this.name.equals(((Recipe) other).name)
            && this.ingredients.equals(((Recipe) other).ingredients)
            && this.steps.equals(((Recipe) other).steps))
            && this.tags.equals(((Recipe) other).tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, steps, tags);
    }

    @Override
    public String toString() {
        var ingredientJoiner = new StringJoiner(", ", "<Ingredients: ", ">");
        var stepJoiner = new StringJoiner(", ", "<Steps: ", ">");
        var tagJoiner = new StringJoiner(", ", "<Tags: ", ">");
        var counter = new AtomicInteger(1);

        ingredientJoiner.setEmptyValue("");
        stepJoiner.setEmptyValue("");
        tagJoiner.setEmptyValue("");

        this.getIngredients().forEach(ingredient -> ingredientJoiner.add(ingredient.toString()));
        this.getSteps().forEach(step -> stepJoiner.add(counter.getAndIncrement() + ". " + step.toString()));
        this.getTags().forEach(tag -> tagJoiner.add(tag.toString()));

        return Stream.of(this.getName(), ingredientJoiner.toString(), stepJoiner.toString(), tagJoiner.toString())
            .filter(field -> !field.isEmpty())
            .collect(Collectors.joining(" "));
    }
}
