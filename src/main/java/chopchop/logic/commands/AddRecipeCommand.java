package chopchop.logic.commands;

import static chopchop.util.Strings.ARG_INGREDIENT;
import static chopchop.util.Strings.ARG_QUANTITY;
import static chopchop.util.Strings.ARG_STEP;
import static java.util.Objects.requireNonNull;

import chopchop.logic.commands.exceptions.CommandException;
import chopchop.model.Model;
import chopchop.model.recipe.Recipe;

/**
 * Adds a person to the address book.
 */
public class AddRecipeCommand extends Command {

    public static final String COMMAND_WORD = "add recipe";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a recipe to the recipe book. "
            + "Parameters: "
            + "NAME "
            + "[" + ARG_INGREDIENT + "INGREDIENT [" + ARG_QUANTITY + " QUANTITY]]..."
            + "[" + ARG_STEP + "STEP]...\n"
            + "Example: " + COMMAND_WORD + " "
            + "Sugar Tomato"
            + ARG_INGREDIENT + "Sugar "
            + ARG_INGREDIENT + "Tomato " + ARG_QUANTITY + " 5 "
            + ARG_STEP + "Chop tomatoes. "
            + ARG_STEP + "Add sugar to it and mix well. ";

    public static final String MESSAGE_SUCCESS = "New recipe added: %1$s";
    public static final String MESSAGE_DUPLICATE_RECIPE = "This recipe already exists in the recipe book";

    private final Recipe recipe;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddRecipeCommand(Recipe recipe) {
        requireNonNull(recipe);
        this.recipe = recipe;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasRecipe(this.recipe)) {
            throw new CommandException(MESSAGE_DUPLICATE_RECIPE);
        }

        model.addRecipe(this.recipe);
        return new CommandResult(String.format(MESSAGE_SUCCESS, this.recipe));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddRecipeCommand // instanceof handles nulls
                && this.recipe.equals(((AddRecipeCommand) other).recipe));
    }


    @Override
    public String toString() {
        return String.format("AddRecipeCommand: %s", this.recipe);
    }
}


