package chopchop.ui;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URL;

import chopchop.MainApp;
import javafx.fxml.FXMLLoader;

/**
 * Represents a distinct part of the UI. e.g. Windows, dialogs, panels, status bars, etc.
 * It contains a scene graph with a root node of type {@code T}.
 */
public abstract class UiPart<T> {
    /** Resource folder where FXML files are stored. */
    public static final String FXML_FILE_FOLDER = "/view/";

    private final FXMLLoader fxmlLoader;

    /**
     * Constructs a UiPart with the specified FXML file URL.
     * The FXML file must not specify the {@code fx:controller} attribute.
     */
    public UiPart(URL fxmlFileUrl) {
        this(fxmlFileUrl, null);
    }

    /**
     * Constructs a UiPart using the specified FXML file within {@link #FXML_FILE_FOLDER}.
     * @see #UiPart(URL)
     */
    public UiPart(String fxmlFileName) {
        this(getFxmlFileUrl(fxmlFileName));
    }

    /**
     * Constructs a UiPart with the specified FXML file URL and root object.
     * The FXML file must not specify the {@code fx:controller} attribute.
     */
    public UiPart(URL fxmlFileUrl, T root) {
        this.fxmlLoader = new FXMLLoader();
        this.loadFxmlFile(fxmlFileUrl, root);
    }

    /**
     * Constructs a UiPart with the specified FXML file within {@link #FXML_FILE_FOLDER} and root object.
     * @see #UiPart(URL, T)
     */
    public UiPart(String fxmlFileName, T root) {
        this(getFxmlFileUrl(fxmlFileName), root);
    }

    /**
     * Returns the root object of the scene graph of this UiPart.
     */
    public T getRoot() {
        return this.fxmlLoader.getRoot();
    }

    /**
     * Loads the object hierarchy from a FXML document.
     * @param location Location of the FXML document.
     * @param root Specifies the root of the object hierarchy.
     */
    private void loadFxmlFile(URL location, T root) {
        requireNonNull(location);
        this.fxmlLoader.setLocation(location);
        this.fxmlLoader.setController(this);
        this.fxmlLoader.setRoot(root);

        try {
            this.fxmlLoader.load();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns the FXML file URL for the specified FXML file name within {@link #FXML_FILE_FOLDER}.
     */
    private static URL getFxmlFileUrl(String fxmlFileName) {
        requireNonNull(fxmlFileName);
        String fxmlFileNameWithFolder = FXML_FILE_FOLDER + fxmlFileName;
        URL fxmlFileUrl = MainApp.class.getResource(fxmlFileNameWithFolder);
        return requireNonNull(fxmlFileUrl);
    }
}
