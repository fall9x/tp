package chopchop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import chopchop.commons.exceptions.DataConversionException;
import chopchop.model.UsageList;
import chopchop.model.usage.IngredientUsage;
import chopchop.model.usage.RecipeUsage;

/**
 * API of the Storage component
 */
public interface Storage extends IngredientBookStorage, RecipeBookStorage, UserPrefsStorage {

    public Path getRecipeUsageFilePath();

    public Optional<UsageList<RecipeUsage>> readRecipeUsages() throws DataConversionException;

    public Optional<UsageList<RecipeUsage>> readRecipeUsages(Path filePath) throws DataConversionException;

    public void saveRecipeUsages(UsageList<RecipeUsage> usages) throws IOException;

    public void saveRecipeUsages(UsageList<RecipeUsage> usages, Path filePath) throws IOException;

    public Path getIngredientUsageFilePath();

    public Optional<UsageList<IngredientUsage>> readIngredientUsages() throws DataConversionException;

    public Optional<UsageList<IngredientUsage>> readIngredientUsages(Path filePath) throws DataConversionException;

    public void saveIngredientUsages(UsageList<IngredientUsage> usages) throws IOException;

    public void saveIngredientUsages(UsageList<IngredientUsage> usages, Path filePath) throws IOException;
}
