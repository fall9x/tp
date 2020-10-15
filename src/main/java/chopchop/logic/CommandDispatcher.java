package chopchop.logic;

// import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import chopchop.commons.core.GuiSettings;
import chopchop.commons.core.LogsCenter;

import chopchop.logic.parser.CommandParser;
import chopchop.logic.parser.exceptions.ParseException;

import chopchop.logic.commands.CommandResult;
import chopchop.logic.commands.exceptions.CommandException;

import chopchop.model.Model;
import chopchop.model.ingredient.Ingredient;
import chopchop.model.ingredient.ReadOnlyIngredientBook;
import chopchop.model.recipe.Recipe;
import chopchop.model.recipe.ReadOnlyRecipeBook;

import javafx.collections.ObservableList;

import chopchop.storage.Storage;

/**
 * The main CommandDispatcher governing the logic in the app.
 */
public class CommandDispatcher implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(CommandDispatcher.class);

    private final Model model;
    private final Storage storage;
    private final CommandParser parser;

    /**
     * Constructs a {@code CommandDispatcher} with the given {@code Model} and {@code Storage}.
     */
    public CommandDispatcher(Model model, Storage storage) {
        this.model   = model;
        this.storage = storage;
        this.parser  = new CommandParser();
    }

    /**
     * Executes the specified command based on the commandText.
     *
     * @param commandText The command as entered by the user.
     */
    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        var res = this.parser.parse(commandText);
        if (res.isError()) {
            throw new ParseException(res.getError());
        }

        var cmd = res.getValue();
        var result = cmd.execute(this.model);


        // try {
        //     storage.saveAddressBook(model.getAddressBook());
        // } catch (IOException ioe) {
        //     throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        // }

        return result;
    }

    @Override
    public ReadOnlyRecipeBook getRecipeBook() {
        return model.getRecipeBook();
    }

    @Override
    public ObservableList<Recipe> getFilteredRecipeList() {
        return model.getFilteredRecipeList();
    }

    @Override
    public Path getRecipeBookFilePath() {
        return model.getRecipeBookFilePath();
    }

    @Override
    public ReadOnlyIngredientBook getIngredientBook() {
        return model.getIngredientBook();
    }

    @Override
    public ObservableList<Ingredient> getFilteredIngredientList() {
        return model.getFilteredIngredientList();
    }

    @Override
    public Path getIngredientBookFilePath() {
        return model.getIngredientBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
