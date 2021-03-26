package chopchop;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

import chopchop.commons.core.Config;
import chopchop.commons.core.Log;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.commons.util.ConfigUtil;
import chopchop.commons.util.StringUtil;
import chopchop.logic.Logic;
import chopchop.logic.LogicManager;
import chopchop.model.Entry;
import chopchop.model.EntryBook;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.ReadOnlyEntryBook;
import chopchop.model.UsageList;
import chopchop.model.UserPrefs;
import chopchop.model.usage.Usage;
import chopchop.model.util.SampleDataUtil;
import chopchop.storage.JsonIngredientBookStorage;
import chopchop.storage.JsonIngredientUsageStorage;
import chopchop.storage.JsonRecipeBookStorage;
import chopchop.storage.JsonRecipeUsageStorage;
import chopchop.storage.JsonUserPrefsStorage;
import chopchop.storage.Storage;
import chopchop.storage.StorageManager;
import chopchop.storage.UserPrefsStorage;
import chopchop.ui.DisplayNavigator;
import chopchop.ui.Ui;
import chopchop.ui.UiManager;
import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Runs the application.
 */
public class MainApp extends Application {
    private static final Log logger = new Log(MainApp.class);
    private static MainApp singletonInstance;

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        MainApp.singletonInstance = this;

        logger.log("ChopChop Initialisation");
        super.init();

        var appParameters = AppParameters.parse(getParameters());
        this.config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        var userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        var userPrefs = initPrefs(userPrefsStorage);

        var recipeBookStorage = new JsonRecipeBookStorage(userPrefs.getRecipeBookFilePath());
        var ingredientBookStorage = new JsonIngredientBookStorage(userPrefs.getIngredientBookFilePath());

        var recipeUsageStorage = new JsonRecipeUsageStorage(userPrefs.getRecipeUsageFilePath());
        var ingredientUsageStorage = new JsonIngredientUsageStorage(userPrefs.getIngredientUsageFilePath());

        this.storage = new StorageManager(
            recipeBookStorage, ingredientBookStorage,
            recipeUsageStorage, ingredientUsageStorage,
            userPrefsStorage);

        this.model = new ModelManager(new EntryBook<>(), new EntryBook<>(),
            new UsageList<>(), new UsageList<>(), userPrefs);

        this.logic = new LogicManager(this.model, this.storage);
        ui = new UiManager(logic, model);
    }

    private void loadEntries() {

        // now that the UI is up, we can load the actual data. this is so there is a way to display
        // loading errors to the user.
        this.model.setRecipeBook(this.loadEntryBook("recipe",
            this.storage::readRecipeBook,
            SampleDataUtil::getSampleRecipeBook,
            this.storage.getRecipeBookFilePath()
        ));

        this.model.setIngredientBook(this.loadEntryBook("ingredient",
            this.storage::readIngredientBook,
            SampleDataUtil::getSampleIngredientBook,
            this.storage.getIngredientBookFilePath()
        ));

        this.model.setRecipeUsageList(this.loadUsages("recipe",
            this.storage::readRecipeUsages,
            this.storage.getRecipeUsageFilePath()
        ));

        this.model.setIngredientUsageList(this.loadUsages("ingredient",
            this.storage::readIngredientUsages,
            this.storage.getIngredientUsageFilePath()
        ));

        DisplayNavigator.initialLoad(this.model.getRecipeBook().getEntryList().size() > 0);
    }

    /**
     * Populates the model with the recipe book loaded from disk. If the json was not found, then
     * sample data is loaded; if the json was invalid, then no data is loaded.
     */
    private <T extends Entry> ReadOnlyEntryBook<T> loadEntryBook(String kind,
        EntryBookSupplier<T> loader, Supplier<ReadOnlyEntryBook<T>> sampleData, Path path) {

        try {

            var opt = loader.get();
            if (opt.isEmpty()) {

                logger.log("Data file for %s book not found; starting with sample recipes", kind);

                this.ui.showCommandOutput(
                    String.format("Could not find existing %ss, loading sample data", kind),
                    /* isError: */ false
                );

                return sampleData.get();
            } else {
                return opt.get();
            }

        } catch (DataConversionException e) {
            logger.error("Data file for %s book was invalid; starting with an empty book", kind);

            this.ui.showCommandOutput(
                String.format("Existing %ss were corrupted; starting with empty data", kind),
                /* isError: */ true
            );

            this.ui.displayModalDialog(AlertType.ERROR, "Data Loading Error",
                String.format("Failed to load %ss (from '%s')", kind, path),
                String.format("Note that making any changes here will overwrite any existing %ss", kind));
            return new EntryBook<T>();
        }
    }


    private <T extends Usage> UsageList<T> loadUsages(String kind, UsageListSupplier<T> loader, Path path) {

        try {
            var opt = loader.get();
            if (opt.isEmpty()) {

                logger.log("Data file for %s usage list not found", kind);
                return new UsageList<>();
            } else {
                return opt.get();
            }

        } catch (DataConversionException e) {
            logger.error("Data file for %s usage list was invalid; starting with an empty list", kind);

            this.ui.showCommandOutput(
                String.format("Existing %s usages were corrupted; starting with empty data", kind),
                /* isError: */ true
            );

            this.ui.displayModalDialog(AlertType.ERROR, "Data Loading Error",
                String.format("Failed to load %s usages (from '%s')", kind, path),
                String.format("Note that making any changes here will overwrite any existing %ss", kind));

            return new UsageList<T>();
        }
    }

    private void initLogging(Config config) {
        Log.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initialisedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.log("Using custom config file '%s'", configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.log("Using config file '%s'", configFilePathUsed);

        try {
            var configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initialisedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warn("Config file at '%s' is not in the correct format, using default values", configFilePathUsed);
            initialisedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initialisedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warn("Failed to save config file: %s", StringUtil.getDetails(e));
        }

        return initialisedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.log("Using prefs file '%s'", prefsFilePath);

        UserPrefs initialisedPrefs;

        try {
            var prefsOptional = storage.readUserPrefs();
            initialisedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warn("UserPrefs file at '%s' is not in the correct format, using default values", prefsFilePath);
            initialisedPrefs = new UserPrefs();
        }

        // Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initialisedPrefs);
        } catch (IOException e) {
            logger.warn("Failed to save config file: %s", StringUtil.getDetails(e));
        }

        return initialisedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.log("Starting ChopChop");
        this.ui.start(primaryStage);

        // we can only load entries after the UI starts!!!!
        this.loadEntries();
    }


    @Override
    public void stop() {
        logger.log("ChopChop Shutdown");
        try {
            this.storage.saveUserPrefs(this.model.getUserPrefs());
        } catch (IOException e) {
            logger.error("Failed to save preferences: ", StringUtil.getDetails(e));
        }
    }


    /**
     * Returns the singleton instance of the MainApp
     */
    public static MainApp the() {
        return MainApp.singletonInstance;
    }



    // dumb stuff.
    @FunctionalInterface
    private static interface EntryBookSupplier<T extends Entry> {
        Optional<ReadOnlyEntryBook<T>> get() throws DataConversionException;
    }

    @FunctionalInterface
    private static interface UsageListSupplier<T extends Usage> {
        Optional<UsageList<T>> get() throws DataConversionException;
    }
}
