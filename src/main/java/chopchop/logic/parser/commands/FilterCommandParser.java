// FilterCommandParser.java
//@@author hjl99

package chopchop.logic.parser.commands;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import chopchop.commons.util.Result;
import chopchop.logic.commands.Command;
import chopchop.logic.commands.FilterIngredientCommand;
import chopchop.logic.commands.FilterRecipeCommand;
import chopchop.logic.parser.CommandArguments;
import chopchop.model.attributes.ExpiryDate;
import chopchop.model.attributes.ExpiryDateOnOrBeforePredicate;
import chopchop.model.attributes.IngredientsContainsKeywordsPredicate;
import chopchop.model.attributes.TagContainsKeywordsPredicate;
import chopchop.model.attributes.NameContainsKeywordsFilterPredicate;

import static chopchop.commons.util.Strings.ARG_NAME;
import static chopchop.commons.util.Strings.ARG_EXPIRY;
import static chopchop.commons.util.Strings.ARG_INGREDIENT;
import static chopchop.commons.util.Strings.ARG_TAG;
import static chopchop.commons.util.Strings.COMMAND_FILTER;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;
import static chopchop.logic.parser.commands.CommonParser.checkArguments;

public class FilterCommandParser {

    /**
     * Parses a 'filter' command. Syntax(es):
     * {@code filter recipe (keywords)+}
     * {@code filter ingredient (keywords)+}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     a FilterCommand, if the input was valid.
     */
    public static Result<? extends Command> parseFilterCommand(CommandArguments args) {

        ensureCommandName(args, COMMAND_FILTER);

        return getCommandTarget(args, /* acceptsPlural: */ true)
            .then(target -> {
                if (args.getAllArguments().isEmpty()) {
                    return Result.error("Filtering criteria cannot be empty!");
                }

                switch (target.fst()) {
                case RECIPE:
                    return parseFilterRecipeCommand(args);

                case INGREDIENT:
                    return parseFilterIngredientCommand(args);

                default:
                    return Result.error("Can only filter recipes or ingredients ('%s' invalid)", target.fst());
                }
            });
    }


    private static Result<? extends Command> parseFilterIngredientCommand(CommandArguments args) {

        Optional<String> err;
        var supportedArgs = List.of(ARG_NAME, ARG_TAG, ARG_EXPIRY);
        if ((err = checkArguments(args, "filter ingredient", supportedArgs)).isPresent()) {
            return Result.error(err.get());
        }

        var names = args.getArgument(ARG_NAME);
        var exps = args.getArgument(ARG_EXPIRY);
        var tags = args.getArgument(ARG_TAG);

        if ((err = checkImproperFieldInput("Expiry date", exps)).isPresent()) {
            return Result.error(err.get());
        } else if ((err = checkImproperFieldInput("Tag", tags)).isPresent()) {
            return Result.error(err.get());
        } else if ((err = checkImproperFieldInput("Name", names)).isPresent()) {
            return Result.error(err.get());
        }

        return parseExpiryDates(exps)
            .map(optExpiry -> new FilterIngredientCommand(
                optExpiry.map(ExpiryDateOnOrBeforePredicate::new).orElse(null),
                tags.isEmpty() ? null : new TagContainsKeywordsPredicate(tags),
                names.isEmpty() ? null : new NameContainsKeywordsFilterPredicate(names)
            ));
    }


    private static Result<? extends Command> parseFilterRecipeCommand(CommandArguments args) {

        Optional<String> err;
        var supportedArgs = List.of(ARG_NAME, ARG_TAG, ARG_INGREDIENT);
        if ((err = checkArguments(args, "filter recipe", supportedArgs)).isPresent()) {
            return Result.error(err.get());
        }

        var names = args.getArgument(ARG_NAME);
        var ingredients = args.getArgument(ARG_INGREDIENT);
        var tags = args.getArgument(ARG_TAG);

        if ((err = checkImproperFieldInput("Tag", tags)).isPresent()) {
            return Result.error(err.get());
        } else if ((err = checkImproperFieldInput("Ingredient", ingredients)).isPresent()) {
            return Result.error(err.get());
        } else if ((err = checkImproperFieldInput("Name", names)).isPresent()) {
            return Result.error(err.get());
        }

        return Result.of(new FilterRecipeCommand(
            tags.isEmpty() ? null : new TagContainsKeywordsPredicate(tags),
            ingredients.isEmpty() ? null : new IngredientsContainsKeywordsPredicate(ingredients),
            names.isEmpty() ? null : new NameContainsKeywordsFilterPredicate(names)
        ));
    }


    /**
     * Returns the first expiry date (chronologically) from the given list, or an
     * error if any of them failed to parse.
     */
    private static Result<Optional<ExpiryDate>> parseExpiryDates(List<String> args) {

        return Result.sequence(args.stream()
            .map(ExpiryDate::of)
            .collect(Collectors.toList())
        ).map(exps -> exps.stream().sorted().findFirst());
    }

    /**
     * Returns an error message if there was an error.
     */
    private static Optional<String> checkImproperFieldInput(String kind, List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            var s = list.get(i);

            if (s.isEmpty()) {
                return Optional.of(String.format("%s cannot be empty", kind));
            } else if (kind.equals("Expiry date") && s.split("\\s+").length > 1) {
                return Optional.of(String.format("Expiry date cannot contain spaces"));
            }
        }

        return Optional.empty();
    }
}
