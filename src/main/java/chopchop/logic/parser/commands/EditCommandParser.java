package chopchop.logic.parser.commands;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import chopchop.logic.commands.EditIngredientCommand;
import chopchop.logic.commands.EditRecipeCommand;
import chopchop.logic.edit.IngredientEditDescriptor;
import chopchop.model.attributes.Quantity;
import chopchop.model.attributes.Tag;
import chopchop.commons.util.Result;
import chopchop.logic.edit.EditOperationType;
import chopchop.logic.edit.IngredientRefEditDescriptor;
import chopchop.logic.edit.RecipeEditDescriptor;
import chopchop.logic.edit.StepEditDescriptor;
import chopchop.logic.edit.TagEditDescriptor;
import chopchop.logic.parser.ArgName;
import chopchop.logic.parser.ItemReference;
import chopchop.logic.parser.CommandArguments;
import chopchop.logic.commands.Command;

import static chopchop.commons.util.Strings.ARG_INGREDIENT;
import static chopchop.commons.util.Strings.ARG_NAME;
import static chopchop.commons.util.Strings.ARG_QUANTITY;
import static chopchop.commons.util.Strings.ARG_STEP;
import static chopchop.commons.util.Strings.ARG_TAG;
import static chopchop.commons.util.Strings.COMMAND_EDIT;
import static chopchop.logic.parser.commands.CommonParser.ensureCommandName;
import static chopchop.logic.parser.commands.CommonParser.getCommandTarget;

public class EditCommandParser {

    /**
     * Parses an 'edit' command. Syntax:
     * {@code edit recipe <#REF> (...)}
     *
     * @param args the parsed command arguments from the {@code CommandParser}.
     * @return     an AddCommand, if the input was valid.
     */
    public static Result<? extends Command> parseEditCommand(CommandArguments args) {
        ensureCommandName(args, COMMAND_EDIT);

        return getCommandTarget(args)
            .then(target -> {
                if (target.snd().isEmpty()) {
                    return Result.error("Recipe or ingredient name cannot be empty");
                }

                switch (target.fst()) {
                case RECIPE:
                    return parseEditRecipeCommand(target.snd().strip(), args);

                case INGREDIENT:
                    return parseEditIngredientCommand(target.snd().strip(), args);

                default:
                    return Result.error("Can only edit recipes or ingredients ('%s' invalid)", target.fst());
                }
            });
    }

    private static Result<EditRecipeCommand> parseEditRecipeCommand(String name, CommandArguments args) {
        return ItemReference.parse(name)
            .then(item -> {

                Optional<String> editedName = Optional.empty();
                var tagEdits = new ArrayList<Result<TagEditDescriptor>>();
                var stepEdits = new ArrayList<Result<StepEditDescriptor>>();
                var ingrEdits = new ArrayList<Result<IngredientRefEditDescriptor>>();

                for (int i = 0; i < args.getAllArguments().size(); i++) {

                    var arg = args.getAllArguments().get(i);
                    var argName = arg.fst();
                    var argValue = arg.snd();

                    if (argName.nameEquals(ARG_NAME)) {

                        if (!argName.getComponents().isEmpty()) {
                            return Result.error("Expected only '/name' without any edit-arguments");
                        } else if (argValue.isEmpty()) {
                            return Result.error("Expected a name after '/name'");
                        } else if (editedName.isPresent()) {
                            return Result.error("Only one '/name' should be provided");
                        }

                        editedName = Optional.of(argValue);

                    } else if (argName.nameEquals(ARG_TAG)) {

                        tagEdits.add(parseTagEdit(argName, argValue));

                    } else if (argName.nameEquals(ARG_STEP)) {

                        stepEdits.add(parseStepEdit(argName, argValue));

                    } else if (argName.nameEquals(ARG_INGREDIENT)) {

                        Optional<Quantity> qty = Optional.empty();
                        if (i + 1 < args.getAllArguments().size()) {
                            var nextArg = args.getAllArguments().get(i + 1);
                            if (nextArg.fst().equals(ARG_QUANTITY)) {

                                var q = CommonParser.parseQuantity(nextArg.snd());
                                if (q.isError()) {
                                    return Result.error(q.getError());
                                } else {
                                    qty = q.toOptional();
                                }

                                // skip it on the next turn.
                                i += 1;
                            }
                        }

                        ingrEdits.add(parseIngredientEdit(argName, argValue, qty));

                    } else if (argName.nameEquals(ARG_QUANTITY)) {

                        return Result.error("/qty can only appear after an /ingredient:add or /ingredient:delete");

                    } else {
                        return Result.error("'edit recipe' command doesn't support '%s'", argName);
                    }
                }

                // ugly AF, but i don't care at the moment.
                var tes = Result.sequence(tagEdits);
                var ses = Result.sequence(stepEdits);
                var ies = Result.sequence(ingrEdits);

                if (tes.isError()) {
                    return Result.error(tes.getError());
                } else if (ses.isError()) {
                    return Result.error(ses.getError());
                } else if (ies.isError()) {
                    return Result.error(ies.getError());
                }

                return Result.of(new EditRecipeCommand(item,
                    new RecipeEditDescriptor(editedName, ies.getValue(), ses.getValue(), tes.getValue())
                ));
            });
    }

    private static Result<EditIngredientCommand> parseEditIngredientCommand(String name, CommandArguments args) {
        return ItemReference.parse(name)
            .then(item -> {
                var tagEdits = new ArrayList<Result<TagEditDescriptor>>();

                for (var arg : args.getAllArguments()) {
                    var argName = arg.fst();
                    var argValue = arg.snd();

                    if (argName.nameEquals(ARG_TAG)) {
                        tagEdits.add(parseTagEdit(argName, argValue));
                    } else {
                        return Result.error("'edit ingredient' command doesn't support '%s'", argName);
                    }
                }

                var tes = Result.sequence(tagEdits);

                if (tes.isError()) {
                    return Result.error(tes.getError());
                }

                return Result.of(new EditIngredientCommand(item,
                    new IngredientEditDescriptor(tes.getValue())
                ));
            });
    }

    private static Result<IngredientRefEditDescriptor> parseIngredientEdit(ArgName argName, String ingredientName,
                                                                           Optional<Quantity> qty) {

        var components = argName.getComponents();
        if (components.isEmpty()) {
            return Result.error("Expected either /ingredient:add, /ingredient:edit, or /ingredient:delete");
        }

        var op = components.get(0);

        if (components.size() != 1 || !List.of("add", "edit", "delete").contains(op)) {
            return Result.error("Expected either (and only) 'add', 'edit', or 'delete' after '/ingredient:'");
        }

        if (ingredientName.isEmpty()) {
            return Result.error("Expected ingredient name after /ingredient:%s", op);
        }

        return ensureNoArgsForDeleteAndGetOperationType("quantity", "ingredient", op, qty.isEmpty())
            .map(kind -> new IngredientRefEditDescriptor(kind, ingredientName, qty));
    }









    private static Result<TagEditDescriptor> parseTagEdit(ArgName argName, String argValue) {

        var comps = argName.getComponents();
        if (comps.isEmpty()) {
            return Result.error("Expected either /tag:add or /tag:delete");
        }

        var op = comps.get(0);
        if (comps.size() != 1 || !List.of("add", "delete").contains(op)) {
            return Result.error("Expected either /tag:add or /tag:delete");
        }

        if (argValue.isEmpty()) {
            return Result.error("Expected tag name after /tag:add or /tag:delete");
        } else if (!Tag.isValidTag(argValue)) {
            return Result.error(Tag.MESSAGE_CONSTRAINTS);
        }

        return Result.of(new TagEditDescriptor(
            op.equals("add") ? EditOperationType.ADD : EditOperationType.DELETE,
            argValue
        ));
    }

    private static Result<StepEditDescriptor> parseStepEdit(ArgName argName, String argValue) {

        var components = argName.getComponents();
        if (components.isEmpty()) {
            return Result.error("expected either /step:add, /step:edit, or /step:delete");
        }

        var op = components.get(0);

        // first get the step number.
        Optional<Integer> optStep = Optional.empty();
        if (components.size() > 1) {

            int stepnum = 0;

            try {
                stepnum = Integer.parseInt(components.get(1));
            } catch (NumberFormatException e) {
                return Result.error("'%s' was not a valid step number (expected /step:%s:<number>)",
                    components.get(1), op);
            }

            if (stepnum <= 0) {
                return Result.error("Step number should be greater than 0");
            }

            optStep = Optional.of(stepnum);
        }


        final var stepNumber = optStep;
        if (op.equals("add")) {

            // for add, the index is optional.
            if (components.size() > 2) {
                return Result.error("Expected either /step:add or /step:add:<number>");
            }

            if (argValue.isEmpty()) {
                return Result.error("Expected non-empty step after /step:add");
            }

            return Result.of(new StepEditDescriptor(EditOperationType.ADD, stepNumber, argValue));

        } else if (op.equals("edit") || op.equals("delete")) {

            // either stepNumber will be valid, or component size is not 2, because:
            // (a) if #components was > 1, then we either got a valid step number, or returned
            // (b) if #components was <= 1, then it is != 2, so we will return here.
            if (components.size() != 2) {
                return Result.error("Expected number after /step:%s (eg. /step:%s:3)", op, op);
            }

            return ensureNoArgsForDeleteAndGetOperationType("step", op, argValue.isEmpty())
                .map(kind -> new StepEditDescriptor(kind, stepNumber, argValue));

        } else {
            return Result.error("Expected either /step:add, /step:edit, or /step:delete");
        }
    }




    private static Result<EditOperationType> ensureNoArgsForDeleteAndGetOperationType(
        String editor, String editor2, String op, boolean argIsEmpty) {

        if (op.equals("add")) {
            if (argIsEmpty) {
                return Result.error("Expected non-empty %s after /%s:%s", editor, editor2, op);
            }

            return Result.of(EditOperationType.ADD);

        } else if (op.equals("edit")) {
            if (argIsEmpty) {
                return Result.error("Expected non-empty %s after /%s:%s", editor, editor2, op);
            }

            return Result.of(EditOperationType.EDIT);

        } else if (op.equals("delete")) {
            if (!argIsEmpty) {
                return Result.error("Unexpected %s after /%s:%s", editor, editor2, op);
            }

            return Result.of(EditOperationType.DELETE);

        } else {
            return Result.error("Expected either /%s:add, /%s:edit, or /%s:delete",
                editor, editor, editor);
        }
    }

    private static Result<EditOperationType> ensureNoArgsForDeleteAndGetOperationType(
        String editor, String op, boolean argIsEmpty) {

        return ensureNoArgsForDeleteAndGetOperationType(editor, editor, op, argIsEmpty);
    }
}
