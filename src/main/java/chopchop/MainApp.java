package chopchop;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import chopchop.model.ingredient.IngredientBook;
import chopchop.model.ingredient.ReadOnlyIngredientBook;
import chopchop.model.recipe.ReadOnlyRecipeBook;
import chopchop.model.recipe.RecipeBook;
import chopchop.model.util.SampleDataUtil;
import chopchop.storage.IngredientBookStorage;
import chopchop.storage.JsonIngredientBookStorage;
import chopchop.storage.JsonRecipeBookStorage;
import chopchop.storage.RecipeBookStorage;
import chopchop.ui.UiManager;
import javafx.application.Application;
import javafx.stage.Stage;
import chopchop.commons.core.Config;
import chopchop.commons.core.LogsCenter;
import chopchop.commons.core.Version;
import chopchop.commons.exceptions.DataConversionException;
import chopchop.commons.util.ConfigUtil;
import chopchop.commons.util.StringUtil;
import chopchop.logic.Logic;
import chopchop.logic.CommandDispatcher;
import chopchop.model.Model;
import chopchop.model.ModelManager;
import chopchop.model.ReadOnlyUserPrefs;
import chopchop.model.UserPrefs;
import chopchop.storage.JsonUserPrefsStorage;
import chopchop.storage.Storage;
import chopchop.storage.StorageManager;
import chopchop.storage.UserPrefsStorage;
import chopchop.ui.Ui;


/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        IngredientBookStorage ingredientBookStorage =
            new JsonIngredientBookStorage(userPrefs.getIngredientBookFilePath());
        RecipeBookStorage recipeBookStorage = new JsonRecipeBookStorage(userPrefs.getRecipeBookFilePath());
        storage = new StorageManager(ingredientBookStorage, recipeBookStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new CommandDispatcher(model, storage);

        ui = new UiManager(logic);

    }
    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s ingredient and recipe book
     * and {@code userPrefs}. <br>
     * The data from the sample ingredient or recipe book will be used instead if {@code storage}'s
     * ingredient or recipe book is not found,
     * or an empty ingredient or recipe book will be used instead if errors occur when reading
     * {@code storage}'s ingredient or recipe book.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {

        Optional<ReadOnlyIngredientBook> ingredientBookOptional;
        Optional<ReadOnlyRecipeBook> recipeBookOptional;
        ReadOnlyIngredientBook initialIndData;
        ReadOnlyRecipeBook initialRecData;

        try {
            ingredientBookOptional = storage.readIngredientBook();
            if (!ingredientBookOptional.isPresent()) {
                logger.info("Data file for ingredient book not found. Will be starting with a sample IndBook");
            }
            recipeBookOptional = storage.readRecipeBook();
            initialIndData = ingredientBookOptional.orElseGet(SampleDataUtil::getSampleIngredientBook);
            if (!recipeBookOptional.isPresent()) {
                logger.info("Data file for recipe book not found. Will be starting with a sample RecBook");
            }
            initialRecData = recipeBookOptional.orElseGet(SampleDataUtil::getSampleRecipeBook);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AddressBook");
            initialIndData = new IngredientBook();
            initialRecData = new RecipeBook();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initialIndData = new IngredientBook();
            initialRecData = new RecipeBook();
        }

        return new ModelManager(initialRecData, initialIndData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;

        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        initializedPrefs = new UserPrefs();
        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
