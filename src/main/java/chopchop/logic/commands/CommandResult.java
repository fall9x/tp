// CommandResult.java

package chopchop.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final List<Part> parts;

    private final boolean isError;
    private final boolean showHelp;
    private final boolean shouldExit;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    private CommandResult(String message, boolean isError, boolean shouldExit, boolean showHelp) {
        requireNonNull(message);

        this.isError = isError;
        this.showHelp = showHelp;
        this.shouldExit = shouldExit;
        this.parts = List.of(Part.text(message));
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    private CommandResult(List<Part> parts, boolean isError, boolean shouldExit, boolean showHelp) {
        requireNonNull(parts);

        this.parts = parts;
        this.isError = isError;
        this.showHelp = showHelp;
        this.shouldExit = shouldExit;
    }

    /**
     * Returns true if the application should exit after this command
     */
    public boolean shouldExit() {
        return this.shouldExit;
    }

    /**
     * Returns true if the app should open the help window
     */
    public boolean shouldShowHelp() {
        return this.showHelp;
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
        return this.parts;
    }

    /**
     * Appends a new textual part to the list.
     */
    public CommandResult appending(String text, boolean prependNewline) {
        var list = new ArrayList<>(this.parts);
        if (list.size() > 0 && !prependNewline) {
            list.get(list.size() - 1).setAppendNewline(false);
        }

        list.add(Part.text(text));

        return new CommandResult(list, this.isError, this.shouldExit, this.showHelp);
    }

    /**
     * Appends a new link part to the list.
     */
    public CommandResult appendingLink(String text, String url, boolean prependNewline) {
        var list = new ArrayList<>(this.parts);
        if (list.size() > 0 && !prependNewline) {
            list.get(list.size() - 1).setAppendNewline(false);
        }

        list.add(Part.link(text, url));

        return new CommandResult(list, this.isError, this.shouldExit, this.showHelp);
    }

    @Override
    public String toString() {
        return (this.isError ? "Error: " : "")
            + String.join(" ", this.parts.stream().map(Part::getText).collect(Collectors.toList()));
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
            && this.showHelp == cr.showHelp
            && this.shouldExit == cr.shouldExit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parts, this.isError, this.showHelp, this.shouldExit);
    }

    /**
     * Constructs a new command result that only shows a message.
     *
     * @param message the message to show
     */
    public static CommandResult message(String message, Object... args) {
        return new CommandResult(String.format(message, args),
            /* isError: */ false, /* shouldExit: */ false, /* showHelp: */ false);
    }

    /**
     * Constructs a new command result that shows an error.
     *
     * @param error the error to show
     */
    public static CommandResult error(String error, Object... args) {
        return new CommandResult(String.format(error, args),
            /* isError: */ true, /* shouldExit: */ false, /* showHelp: */ false);
    }

    /**
     * Constructs a new command result that shows help
     */
    public static CommandResult help() {
        return new CommandResult("", /* isError: */ false, /* shouldExit: */ false,
            /* showHelp: */ true);
    }

    /**
     * Constructs a new command result that quits.
     */
    public static CommandResult exit() {
        return new CommandResult("", /* isError: */ false, /* shouldExit: */ true,
            /* showHelp: */ false);
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
            this.appendNewline = true;
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
}
