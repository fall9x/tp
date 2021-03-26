// CommandResult.java

package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import chopchop.commons.util.Pair;
import chopchop.model.recipe.Recipe;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final List<Part> parts;

    private final boolean isError;
    private final boolean shouldExit;
    private final boolean isStatsOutput;
    private final List<Pair<String, String>> statsMessage;

    private final Panel switchedPanel;
    private final Optional<Recipe> recipeToShow;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    private CommandResult(String message, boolean isError, boolean shouldExit, boolean isStatsOutput,
                          List<Pair<String, String>> statsMessage, Panel panel, Optional<Recipe> rec) {
        this(List.of(Part.text(message)), isError, shouldExit, isStatsOutput, statsMessage, panel, rec);
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    private CommandResult(List<Part> parts, boolean isError, boolean shouldExit, boolean isStatsOutput,
                          List<Pair<String, String>> statsMessage, Panel panel, Optional<Recipe> rec) {
        requireNonNull(parts);

        this.parts = parts;
        this.isError = isError;
        this.shouldExit = shouldExit;
        this.isStatsOutput = isStatsOutput;
        this.statsMessage = new ArrayList<>(statsMessage);
        this.switchedPanel = panel;
        this.recipeToShow = rec;
    }

    /**
     * Returns true if the application should exit after this command
     */
    public boolean shouldExit() {
        return this.shouldExit;
    }

    /**
     * Returns true if the message should be styled as an error
     */
    public boolean isError() {
        return this.isError;
    }

    /**
     * Returns true if !isError()
     */
    public boolean didSucceed() {
        return !this.isError;
    }

    /**
     * Returns the parts of the message
     */
    public List<Part> getParts() {
        return new ArrayList<>(this.parts);
    }

    /**
     * Appends a new textual part to the list.
     */
    public CommandResult appending(String text, boolean prependNewline) {
        var list = new ArrayList<>(this.parts);
        if (list.size() > 0) {
            list.get(list.size() - 1).setAppendNewline(prependNewline);
        }

        list.add(Part.text(text));

        return new CommandResult(list, this.isError, this.shouldExit, this.isStatsOutput, this.statsMessage,
            this.switchedPanel, this.recipeToShow);
    }

    /**
     * Appends a new link part to the list.
     */
    public CommandResult appendingLink(String text, String url, boolean prependNewline) {
        var list = new ArrayList<>(this.parts);
        if (list.size() > 0) {
            list.get(list.size() - 1).setAppendNewline(prependNewline);
        }

        list.add(Part.link(text, url));

        return new CommandResult(list, this.isError, this.shouldExit, this.isStatsOutput, this.statsMessage,
            this.switchedPanel, this.recipeToShow);
    }

    /**
     * Sets the UI to show the given recipe after the command completes successfully.
     */
    public CommandResult showingRecipe(Recipe recipe) {
        return new CommandResult(this.parts, this.isError, this.shouldExit, this.isStatsOutput,
            this.statsMessage, Panel.RECIPE_DETAIL, Optional.of(recipe));
    }

    /**
     * Sets the UI to show the recipe list after the command completes successfully.
     */
    public CommandResult showingRecipeList() {
        return new CommandResult(this.parts, this.isError, this.shouldExit, this.isStatsOutput,
            this.statsMessage, Panel.RECIPE_LIST, Optional.empty());
    }

    /**
     * Sets the UI to show the ingredient list after the command completes successfully.
     */
    public CommandResult showingIngredientList() {
        return new CommandResult(this.parts, this.isError, this.shouldExit, this.isStatsOutput,
            this.statsMessage, Panel.INGREDIENT_LIST, Optional.empty());
    }

    /**
     * Sets the UI to show the recommendations list after the command completes successfully.
     */
    public CommandResult showingRecommendationList() {
        return new CommandResult(this.parts, this.isError, this.shouldExit, this.isStatsOutput,
            this.statsMessage, Panel.RECOMMENDATION_LIST, Optional.empty());
    }

    /**
     * Returns true if the command result comes from stats command.
     */
    public boolean isStatsOutput() {
        return this.isStatsOutput;
    }

    /**
     * Returns the list to be displayed in list view of stats box.
     */
    public ArrayList<Pair<String, String>> getStatsMessage() {
        return new ArrayList<>(this.statsMessage);
    }

    /**
     * Returns the panel to switch to
     */
    public Panel getSwitchedPanel() {
        return this.switchedPanel;
    }

    /**
     * Returns the recipe to show, if any
     */
    public Optional<Recipe> getDisplayedRecipe() {
        return this.recipeToShow;
    }


    @Override
    public String toString() {
        return (this.isError ? "Error: " : "")
            + this.parts.stream().map(Part::getText).collect(Collectors.joining(" "));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof CommandResult)) {
            return false;
        }

        var cr = (CommandResult) obj;

        return this.parts.equals(cr.parts)
            && this.isError == cr.isError
            && this.shouldExit == cr.shouldExit
            && this.isStatsOutput == cr.isStatsOutput
            && this.statsMessage.equals(cr.statsMessage)
            && this.switchedPanel.equals(cr.switchedPanel)
            && this.recipeToShow.equals(cr.recipeToShow);
    }


    /**
     * Constructs a new command result that only shows a message.
     *
     * @param message the message to show
     */
    public static CommandResult message(String message, Object... args) {
        return new CommandResult(String.format(message, args),
            /* isError: */ false, /* shouldExit: */ false, /* statsMsg: */ false,
            /* statsMsgs: */ new ArrayList<>(), /* panel: */ Panel.NONE,
            /* recipe: */ Optional.empty());
    }

    /**
     * Constructs a new command result that shows an error.
     *
     * @param error the error to show
     */
    public static CommandResult error(String error, Object... args) {
        return new CommandResult(String.format(error, args),
            /* isError: */ true, /* shouldExit: */ false, /* statsMsg: */ false,
            /* statsMsgs: */ new ArrayList<>(), /* panel: */ Panel.NONE,
            /* recipe: */ Optional.empty());
    }

    /**
     * Constructs a new command result that quits.
     */
    public static CommandResult exit() {
        return new CommandResult(List.of(), /* isError: */ false, /* shouldExit: */ true,
            /* statsMsg: */ false, /* statsMsgs: */ new ArrayList<>(),
            /* panel: */ Panel.NONE, /* recipe: */ Optional.empty());
    }

    /**
     * Constructs a new command result from stats that contains the list of values to be output in stats box and the
     * message that indicates the status of the command.
     */
    public static CommandResult statsMessage(List<Pair<String, String>> statsMsgs, String message, Object... args) {
        return new CommandResult(String.format(message, args),
            /* isError: */ false, /* shouldExit: */ false,
            /* statsMsg: */ true, statsMsgs, /* panel: */ Panel.NONE,
            /* recipe: */ Optional.empty());
    }


    public static class Part {
        private final String url;
        private final String text;
        private final boolean isLink;
        private boolean appendNewline;

        /**
         * Makes a new part.
         */
        Part(String text, String url, boolean isLink) {
            this.url = url;
            this.text = text;
            this.isLink = isLink;
            this.appendNewline = false;
        }

        public String getText() {
            return this.text;
        }

        public String getUrl() {
            return this.url;
        }

        public boolean isLink() {
            return this.isLink;
        }

        public boolean appendNewline() {
            return this.appendNewline;
        }

        public void setAppendNewline(boolean newline) {
            this.appendNewline = newline;
        }

        static Part text(String text) {
            return new Part(text, "", false);
        }

        static Part link(String text, String url) {
            return new Part(text, url, true);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof Part)) {
                return false;
            }

            var other = (Part) obj;
            return Objects.equals(this.url, other.url)
                && Objects.equals(this.text, other.text)
                && Objects.equals(this.isLink, other.isLink)
                && Objects.equals(this.appendNewline, other.appendNewline);
        }
    }


    public static enum Panel {
        NONE,
        RECIPE_LIST,
        INGREDIENT_LIST,
        RECOMMENDATION_LIST,
        RECIPE_DETAIL
    }
}
