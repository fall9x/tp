package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.logic.history.HistoryManager;
import chopchop.logic.parser.ItemReference;
import chopchop.model.Model;
import chopchop.model.attributes.Quantity;
import chopchop.model.exceptions.IncompatibleIngredientsException;
import chopchop.model.ingredient.Ingredient;

/**
 * Removes a given quantity of an ingredient identified using it's displayed index or name from the ingredient book.
 * If no quantity is specified, the ingredient will be deleted.
 */
public class DeleteIngredientCommand extends Command implements Undoable {

    private final ItemReference item;
    private final Optional<Quantity> quantity;
    private Ingredient ingredient;
    private Ingredient updatedIngredient;

    public DeleteIngredientCommand(ItemReference item) {
        this(item, Optional.empty());
    }

    /**
     * Constructs a command that deletes the given ingredient item.
     */
    public DeleteIngredientCommand(ItemReference item, Optional<Quantity> quantity) {
        requireNonNull(item);
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);

        var res = resolveIngredientReference(this.item, model);
        if (res.isError()) {
            return CommandResult.error(res.getError());
        }

        this.ingredient = res.getValue();

        if (this.quantity.isPresent()) {
            if (this.quantity.get().isZero()) {
                return CommandResult.error("Quantity should not be zero");
            }

            try {
                var splitIngredient = this.ingredient.split(this.quantity.get());
                this.updatedIngredient = splitIngredient.snd();

                if (this.updatedIngredient.getIngredientSets().isEmpty()) {
                    model.deleteIngredient(this.ingredient);
                } else {
                    model.setIngredient(this.ingredient, this.updatedIngredient);
                }

                return CommandResult.message("Removed %s of ingredient '%s'",
                    this.quantity.get().toString(), this.updatedIngredient.getName()
                ).showingIngredientList();

            } catch (IncompatibleIngredientsException | IllegalValueException e) {
                return CommandResult.error(e.getMessage());
            }
        } else {
            model.deleteIngredient(this.ingredient);
            return CommandResult.message("Deleted ingredient '%s'", this.ingredient.getName())
                .showingIngredientList();
        }
    }

    @Override
    public CommandResult undo(Model model) {
        requireNonNull(model);

        String action = "";

        if (this.quantity.isEmpty() || this.updatedIngredient.getIngredientSets().isEmpty()) {
            model.addIngredient(this.ingredient);
            action = "re-added";
        } else {
            model.setIngredient(this.updatedIngredient, this.ingredient);
            action = "updated";
        }

        return CommandResult.message("Undo: %s ingredient '%s'", action, this.ingredient.getName())
            .showingIngredientList();
    }

    @Override
    public String toString() {
        return String.format("DeleteIngredientCommand(%s%s)", this.item,
            this.quantity.map(q -> String.format(" (%s)", q)).orElse(""));
    }

    public static String getCommandString() {
        return "delete ingredient";
    }

    public static String getCommandHelp() {
        return "Deletes an ingredient, or removes some quantity of an existing ingredient";
    }
}
