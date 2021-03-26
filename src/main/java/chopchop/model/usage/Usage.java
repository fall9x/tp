package chopchop.model.usage;

import static chopchop.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import chopchop.commons.util.Pair;

public abstract class Usage {
    private final String name;
    private final LocalDateTime date;

    protected Usage(String name, LocalDateTime date) {
        requireAllNonNull(name, date);
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public String getPrintableDate() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.date.format(formatter);
    }

    /**
     * Returns true if u2 is 'more later'.
     */
    public boolean isAfter(LocalDateTime u2) {
        if (u2 == null) {
            return true;
        }
        return this.date.isAfter(u2);
    }

    /**
     * Returns true if u2 is earlier.
     */
    public boolean isBefore(LocalDateTime u2) {
        if (u2 == null) {
            return true;
        }
        return this.date.isBefore(u2);
    }

    /**
     * Returns a pair of strings that is suitable for constructing list view panel.
     */
    public Pair<String, String> getListViewPair() {
        return new Pair<>(this.getName(), this.getPrintableDate());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Usage)) {
            return false;
        }

        return Objects.equals(this.name, ((Usage) obj).name)
            && Objects.equals(this.date, ((Usage) obj).date);
    }
}
