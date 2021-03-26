package chopchop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import chopchop.commons.exceptions.DataConversionException;
import chopchop.model.UsageList;
import chopchop.model.usage.Usage;

public interface UsageStorage <T extends Usage> {
    /**
     * Returns the file path of the data file.
     */
    Path getUsageFilePath();

    Optional<UsageList<T>> readUsages() throws DataConversionException;

    Optional<UsageList<T>> readUsages(Path filePath) throws DataConversionException;

    void saveUsages(UsageList<T> usages) throws IOException;

    void saveUsages(UsageList<T> usages, Path filePath) throws IOException;
}
