// AddIngredientCommand.java

package chopchop.logic.commands;

import chopchop.model.Model;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.exceptions.IncompatibleIngredientsException;

import chopchop.logic.commands.exceptions.CommandException;

import static java.util.Objects.requireNonNull;

import static chopchop.util.Strings.ARG_EXPIRY;
import static chopchop.util.Strings.ARG_QUANTITY;

public class AddIngredientCommand extends Command {

    public static final String COMMAND_WORD = "add ingredient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an ingredient to the manager. "
        + "Parameters: "
        + "NAME "
        + ARG_QUANTITY + "QUANTITY "
        + ARG_EXPIRY + "EXPIRY "
        + "\n"
        + "Example: " + COMMAND_WORD + " "
        + "Chili "
        + ARG_QUANTITY + "3"
        + ARG_EXPIRY + "2020-10-05";

    public static final String MESSAGE_SUCCESS = "New ingredient added: %1$s";
    public static final String MESSAGE_COMBINED = "Updated ingredient: %1$s";
    public static final String MESSAGE_DUPLICATE_INGREDIENT = "This ingredient already exists in the ingredient book";


    private final Ingredient ingredient;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddIngredientCommand(Ingredient ingredient) {
        requireNonNull(ingredient);
        this.ingredient = ingredient;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasIngredient(ingredient)) {
            throw new CommandException(MESSAGE_DUPLICATE_INGREDIENT);
        }

        var foo = model.findIngredientWithName(this.ingredient.getName());
        if (foo.isPresent()) {
            var existing = foo.get();

            try {
                var combined = existing.combine(this.ingredient);
                model.setIngredient(existing, combined);

                return new CommandResult(String.format(MESSAGE_COMBINED, combined));

            } catch (IncompatibleIngredientsException e) {
                throw new CommandException(e.toString());
            }

        } else {
            model.addIngredient(ingredient);
            return new CommandResult(String.format(MESSAGE_SUCCESS, ingredient));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddIngredientCommand // instanceof handles nulls
            && ingredient.equals(((AddIngredientCommand) other).ingredient));
    }

    @Override
    public String toString() {
        return String.format("AddIngredientCommand: %s", this.ingredient);
    }
}
