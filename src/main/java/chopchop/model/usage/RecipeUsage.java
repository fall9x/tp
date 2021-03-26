package chopchop.model.usage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecipeUsage extends Usage {

    public RecipeUsage(String name, LocalDateTime date) {
        super(name, date);
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", this.getName(),
            this.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
