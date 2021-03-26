package chopchop.logic.commands;

import static chopchop.commons.util.Enforce.enforceNonNull;

import java.util.Optional;
import java.util.Set;

import chopchop.logic.history.HistoryManager;
import chopchop.model.Model;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.Tag;
import chopchop.model.attributes.units.Count;
import chopchop.model.exceptions.IncompatibleIngredientsException;
import chopchop.model.ingredient.Ingredient;

/**
 * Adds an ingredient to the ingredient book.
 */
public class AddIngredientCommand extends Command implements Undoable {

    private final String name;
    private final Set<Tag> tags;
    private final Optional<Quantity> quantity;
    private final Optional<ExpiryDate> expiryDate;

    private Ingredient addedIngredient;
    private Ingredient existingIngredient;
    private Ingredient combinedIngredient;

    /**
     * Creates a command to add an ingredient with the given parts.
     */
    public AddIngredientCommand(String name, Optional<Quantity> qty, Optional<ExpiryDate> exp, Set<Tag> tags) {
        enforceNonNull(name, qty, exp, tags);

        this.name = name;
        this.tags = tags;
        this.quantity = qty;
        this.expiryDate = exp;
    }



    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        enforceNonNull(model);

        if (this.quantity.map(Quantity::isZero).orElse(false)) {
            return CommandResult.error("Quantity should not be zero");
        }

        // first create the ingredient.
        this.addedIngredient = new Ingredient(this.name, this.quantity, this.expiryDate, this.tags);

        var existingIngredientOptional = model.findIngredientWithName(this.addedIngredient.getName());

        if (existingIngredientOptional.isPresent()) {
            this.existingIngredient = existingIngredientOptional.get();

            try {
                this.combinedIngredient = this.existingIngredient.combine(this.addedIngredient);
                model.setIngredient(this.existingIngredient, this.combinedIngredient);

            } catch (IncompatibleIngredientsException e) {
                return CommandResult.error("Could not add %s of '%s': " + e.getMessage(),
                    this.addedIngredient.getQuantity(), this.addedIngredient.getName());
            }

            return CommandResult.message("Updated ingredient '%s'", this.combinedIngredient.getName())
                .showingIngredientList();

        } else {

            model.addIngredient(this.addedIngredient);
            return CommandResult.message("Added ingredient '%s'", this.addedIngredient.getName())
                .showingIngredientList();
        }
    }

    @Override
    public CommandResult undo(Model model) {
        enforceNonNull(model);

        String action = "";
        Ingredient ingr = null;

        if (this.existingIngredient == null && this.combinedIngredient == null) {

            model.deleteIngredient(this.addedIngredient);

            ingr = this.addedIngredient;
            action = "removed";
        } else {
            model.setIngredient(this.combinedIngredient, this.existingIngredient);

            ingr = this.existingIngredient;
            action = "updated";
        }

        return CommandResult.message("Undo: %s ingredient '%s'", action, ingr.getName())
            .showingIngredientList();
    }

    @Override
    public String toString() {
        return String.format("AddIngredientCommand: %s (%s)%s", this.name,
            this.quantity.orElse(Count.of(1)), this.expiryDate
                .map(e -> String.format(" <Expiry Date: %s>", e)).orElse("")
            );
    }

    public static String getCommandString() {
        return "add ingredient";
    }

    public static String getCommandHelp() {
        return "Adds a new ingredient, or increases the quantity of an existing ingredient";
    }
}
