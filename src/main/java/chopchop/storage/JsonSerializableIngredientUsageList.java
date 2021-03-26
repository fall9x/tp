package chopchop.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import chopchop.commons.exceptions.IllegalValueException;
import chopchop.model.UsageList;
import chopchop.model.usage.IngredientUsage;

public class JsonSerializableIngredientUsageList {
    private final List<JsonAdaptedIngredientUsage> usages;

    /**
     * Constructs a {@code JsonSerializableIngredientUsageList} with the given ingredients.
     */
    @JsonCreator
    public JsonSerializableIngredientUsageList(@JsonProperty("usages") List<JsonAdaptedIngredientUsage> u) {
        this.usages = new ArrayList<>(u);
    }

    /**
     * Converts a given {@code UsageList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableIngredientBook}.
     */
    public JsonSerializableIngredientUsageList(UsageList<IngredientUsage> source) {
        this.usages = source.getUsages().stream().map(JsonAdaptedIngredientUsage::new).collect(Collectors.toList());
    }

    /**
     * Converts this ingredient book into the model's {@code UsageList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public UsageList<IngredientUsage> toModelType() throws IllegalValueException {
        List<IngredientUsage> recordList = new ArrayList<>();
        for (JsonAdaptedIngredientUsage jsonAdaptedRecord : this.usages) {
            IngredientUsage record = jsonAdaptedRecord.toModelType();
            recordList.add(record);
        }
        return new UsageList<IngredientUsage>(recordList);
    }
}
