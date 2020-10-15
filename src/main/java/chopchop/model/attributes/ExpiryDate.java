package chopchop.model.attributes;

import static chopchop.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpiryDate {

    public static final String MESSAGE_CONSTRAINTS =
        "Expiry date should be in the form, yyyy-MM-dd";
    //temporary using 1 fixed date format before parser is integrated.
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_DATE;
    public static final String VALIDATION_REGEX = "^\\d{4}-\\d{2}-\\d{2}";
    private final LocalDate date;

    /**
     * Constructs a {@code ExpiryDate}.
     *
     * @param date A valid string in the date format, yyyy-MM-dd
     */
    public ExpiryDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        this.date = LocalDate.parse(date);
    }

    /**
     * Checks if the input date is valid.
     *
     * @param inputDate A string of unknown date format
     * @return true if the date format is valid. Otherwise, false.
     */
    public static boolean isValidDate(String inputDate) {
        if (!inputDate.matches(VALIDATION_REGEX)) {
            return false;
        }
        try {
            LocalDate.parse(inputDate , FORMAT);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return this.date.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ExpiryDate // instanceof handles nulls
            && this.date.equals(((ExpiryDate) other).date));
    }
    /**
     * Creates an expiry date far in the future, to represent food that either doesn't expire,
     * or where the expiry date was not given.
     */
    public static ExpiryDate none() {
        return new ExpiryDate("9999-12-31");
    }
}
