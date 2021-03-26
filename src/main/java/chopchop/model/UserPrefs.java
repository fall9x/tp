package chopchop.model;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import chopchop.commons.core.GuiSettings;
import chopchop.commons.util.FileUtil;
import chopchop.commons.util.Result;
import chopchop.ui.UiManager;
import javafx.scene.control.Alert.AlertType;


/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {
    private GuiSettings guiSettings = new GuiSettings();

    private PathWithFallback recipeBookFilePath = new PathWithFallback(
        Paths.get("data" , "recipebook.json"));

    private PathWithFallback recipeUsageFilePath = new PathWithFallback(
        Paths.get("data" , "recipeusage.json"));

    private PathWithFallback ingredientBookFilePath = new PathWithFallback(
        Paths.get("data" , "ingredientbook.json"));

    private PathWithFallback ingredientUsageFilePath = new PathWithFallback(
        Paths.get("data" , "ingredientusage.json"));


    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        this.resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        this.setGuiSettings(newUserPrefs.getGuiSettings());
        this.setRecipeBookFilePath(newUserPrefs.getRecipeBookFilePath());
        this.setRecipeUsageFilePath(newUserPrefs.getRecipeUsageFilePath());
        this.setIngredientBookFilePath(newUserPrefs.getIngredientBookFilePath());
        this.setIngredientUsageFilePath(newUserPrefs.getIngredientUsageFilePath());
    }

    @Override
    public GuiSettings getGuiSettings() {
        return this.guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    @Override
    public Path getIngredientBookFilePath() {
        return this.ingredientBookFilePath.get();
    }

    @Override
    public Path getRecipeBookFilePath() {
        return this.recipeBookFilePath.get();
    }

    @Override
    public Path getRecipeUsageFilePath() {
        return this.recipeUsageFilePath.get();
    }

    @Override
    public Path getIngredientUsageFilePath() {
        return this.ingredientUsageFilePath.get();
    }

    public void setIngredientBookFilePath(Path path) {
        requireNonNull(path);
        this.ingredientBookFilePath = validateOrRectifyPath(path);
    }

    public void setRecipeBookFilePath(Path path) {
        requireNonNull(path);
        this.recipeBookFilePath = validateOrRectifyPath(path);
    }

    public void setIngredientUsageFilePath(Path path) {
        requireNonNull(path);
        this.ingredientUsageFilePath = validateOrRectifyPath(path);
    }

    public void setRecipeUsageFilePath(Path path) {
        requireNonNull(path);
        this.recipeUsageFilePath = validateOrRectifyPath(path);
    }

    /**
     * In some instances, the "current working directory" of Java will be the path to
     * java.exe, which, on windows, can be in C:/windows/system32, which we obviously
     * can't write to. This method attempts to detect such instances, and returns a "fixed"
     * path.
     */
    private PathWithFallback validateOrRectifyPath(Path path) {

        var r = isAccessible(path);
        if (r.hasValue()) {
            return new PathWithFallback(r.getValue());
        } else {
            System.err.printf("warning: '%s' is not accessible: %s\n", path, r.getError());
        }

        Path newPath = null;
        try {
            // this gets the path of the jar (which contains the Main.class), and goes up
            // one level to get the folder of the jar. under normal circumstances, this
            // should be accessible.
            newPath = Paths.get(new File(chopchop.Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).getPath()).getParent();

            // path is always a relative path, so we resolve it against the new base path.
            newPath = newPath.resolve(path);

            System.err.printf("trying fallback '%s'\n", newPath);
            r = isAccessible(newPath);

            if (r.hasValue()) {
                return new PathWithFallback(r.getValue());
            }

        } catch (URISyntaxException e) {
            System.err.printf("URISyntaxException: %s\n", e.getMessage());
        }

        System.err.printf("warning: fallback '%s' is not accessible: %s\n", newPath, r.getError());
        return new PathWithFallback(path).setFallback(getTempFolderFor(path)).useFallback();
    }

    private Path getTempFolderFor(Path path) {

        var ret = Paths.get(System.getProperty("java.io.tmpdir")).resolve(path);
        System.err.printf("warning: temp folder: '%s'\n", ret);

        UiManager.enqueueStartupAlert(AlertType.ERROR, "File Permissions Error",
            String.format("Failed to access file '%s'", path),
            String.format("ChopChop will now use a temporary folder, which may not save your changes: '%s'", ret)
        );

        return ret;
    }

    private Result<Path> isAccessible(Path path) {
        try {
            var didCreate = FileUtil.createIfMissing(path);

            if (Files.isReadable(path) && Files.isWritable(path)) {
                if (didCreate) {
                    Files.delete(path);
                }
                return Result.of(path);
            }

            return Result.error("Not readable or writable");

        } catch (IOException e) {
            return Result.error("Exception: %s", e.toString());
        }
    }





    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return this.guiSettings.equals(o.guiSettings)
            && this.ingredientBookFilePath.equals(o.ingredientBookFilePath)
            && this.recipeBookFilePath.equals(o.recipeBookFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.guiSettings, this.ingredientBookFilePath, this.recipeBookFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + this.guiSettings);
        sb.append("\nLocal ingredient data file location : " + this.ingredientBookFilePath);
        sb.append("\nLocal recipe data file location : " + this.recipeBookFilePath);
        return sb.toString();
    }




    /**
     * This is a simple wrapper class that holds two Paths; one is the "normal" one
     * that we should always try to use, and the other is the "fallback" one that we
     * might be using due to personal reasons.
     *
     * This is required so that, in the event we end up using a temp folder, we won't
     * save the path temp folder to the user preferences.
     *
     * We are using a custom json serialiser and deserialiser so that the 'usingFallback'
     * option is saved -- since we always want to assume that the normal path is good to
     * begin with.
     */
    @JsonDeserialize(using = PathWithFallbackDeserialiser.class)
    private static class PathWithFallback extends JsonSerializable.Base {
        private Path normal;
        private Path fallback;
        private boolean usingFallback;

        PathWithFallback(Path normalPath) {
            this.normal = normalPath;
            this.fallback = normalPath;
            this.usingFallback = false;
        }

        Path get() {
            if (this.usingFallback) {
                return this.fallback;
            } else {
                return this.normal;
            }
        }

        PathWithFallback setNormal(Path path) {
            this.normal = path;
            return this;
        }

        PathWithFallback setFallback(Path path) {
            this.fallback = path;
            return this;
        }

        PathWithFallback useFallback() {
            this.usingFallback = true;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof PathWithFallback)) {
                return false;
            }

            var other = (PathWithFallback) obj;
            return this.normal.equals(other.normal)
                && this.fallback.equals(other.fallback)
                && this.usingFallback == other.usingFallback;
        }

        @Override
        public void serializeWithType(JsonGenerator gen, SerializerProvider serialisers, TypeSerializer typeSer)
            throws IOException {
            serialize(gen, serialisers);
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serialisers) throws IOException {
            gen.writeStartObject();

            gen.writeFieldName("normal");
            gen.writeString(this.normal.toString());

            gen.writeFieldName("fallback");
            gen.writeString(this.fallback.toString());

            gen.writeEndObject();
        }
    }

    private static class PathWithFallbackDeserialiser extends StdDeserializer<PathWithFallback> {
        public PathWithFallbackDeserialiser() {
            this(null);
        }

        public PathWithFallbackDeserialiser(Class<?> vc) {
            super(vc);
        }

        @Override
        public PathWithFallback deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

            JsonNode node = jp.getCodec().readTree(jp);
            var normal = node.get("normal").asText();
            var fallback = node.get("fallback").asText();

            return new PathWithFallback(Paths.get(normal)).setFallback(Paths.get(fallback));
        }
    }
}
