package chopchop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.model.ReadOnlyUserPrefs;
import chopchop.model.UserPrefs;
import chopchop.model.ingredient.ReadOnlyIngredientBook;
import chopchop.model.recipe.ReadOnlyRecipeBook;

/**
 * API of the Storage component
 */
public interface Storage extends IngredientBookStorage, RecipeBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getIngredientBookFilePath();

    @Override
    Optional<ReadOnlyRecipeBook> readRecipeBook() throws DataConversionException, IOException;

    @Override
    Path getRecipeBookFilePath();

    @Override
    Optional<ReadOnlyIngredientBook> readIngredientBook() throws DataConversionException, IOException;

    void saveIngredientBook(ReadOnlyIngredientBook ingredientBook) throws IOException;

    void saveRecipeBook(ReadOnlyRecipeBook recipeBook) throws IOException;
}
