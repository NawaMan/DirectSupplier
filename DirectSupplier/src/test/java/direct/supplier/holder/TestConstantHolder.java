package direct.supplier.holder;

import static direct.supplier.holder.ConstantHolder.constant;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import direct.supplier.holder.ConstantHolder;

public class TestConstantHolder {
    
    @Test
    public void testGet_ensureSameValue() {
        // Given a constant holder with a value.
        Integer value = 123456;
        ConstantHolder<Integer> holder = constant(value);
        // When the value is retrieved.
        Integer obtainedValue = holder.get();
        // Then both value must be the same.
        assertEquals(value, obtainedValue);
    }
    
}
