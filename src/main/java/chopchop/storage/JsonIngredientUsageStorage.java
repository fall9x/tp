package chopchop.storage;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import chopchop.commons.core.Log;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.commons.exceptions.IllegalValueException;
import chopchop.commons.util.FileUtil;
import chopchop.commons.util.JsonUtil;
import chopchop.model.UsageList;
import chopchop.model.usage.IngredientUsage;

public class JsonIngredientUsageStorage implements UsageStorage<IngredientUsage> {
    private static final Log logger = new Log(JsonIngredientUsageStorage.class);

    private final Path filePath;

    public JsonIngredientUsageStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path of the data file.
     */
    @Override
    public Path getUsageFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<UsageList<IngredientUsage>> readUsages() throws DataConversionException {
        return this.readUsages(this.filePath);
    }

    @Override
    public Optional<UsageList<IngredientUsage>> readUsages(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableIngredientUsageList> jsonUsageList = JsonUtil.readJsonFile(
            filePath, JsonSerializableIngredientUsageList.class);
        if (jsonUsageList.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonUsageList.get().toModelType());
        } catch (IllegalValueException e) {
            logger.warn("json error ('%s'): %s", filePath, e.getMessage());
            throw new DataConversionException(e);
        }
    }

    @Override
    public void saveUsages(UsageList<IngredientUsage> usages) throws IOException {
        this.saveUsages(usages, this.filePath);
    }

    @Override
    public void saveUsages(UsageList<IngredientUsage> usages, Path filePath) throws IOException {
        requireAllNonNull(usages, filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableIngredientUsageList(usages), filePath);
    }
}
