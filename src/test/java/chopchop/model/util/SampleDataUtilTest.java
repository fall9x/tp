// SampleDataUtilTest.java

package chopchop.model.util;

import org.junit.jupiter.api.Test;

public class SampleDataUtilTest {
    @Test
    void test() {
        new SampleDataUtil();

        SampleDataUtil.getSampleIngredients();
        SampleDataUtil.getSampleIngredientBook();
        SampleDataUtil.getSampleRecipe();
        SampleDataUtil.getSampleRecipeBook();
    }
}
