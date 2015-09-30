package direct.supplier.holder;

import static direct.supplier.holder.ChangeableHolder.NOKEY;
import static direct.supplier.holder.ChangeableHolder.changeable;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import direct.supplier.holder.ChangeableHolder;

public class ChangeableHolderTest {
    
    private final String INIT_VALUE = "";
    
    private ChangeableHolder<String> holder;
    
    private ChangeableHolder<String> createHolder(Object key) {
        holder = changeable(key, INIT_VALUE);
        return holder;
    }
    
    @Test
    public void whenNoKeyAssigned_anyOneCanChange() {
        holder = createHolder(NOKEY);
        assertEquals(INIT_VALUE, holder.get());
        
        holder.set("42");
        assertEquals("42", holder.get());
    }
    
    @Test
    public void whenNoKeyAssigned_givenKeyWillNotChange() {
        Object key = new Object();
        holder = createHolder(NOKEY);
        assertEquals(INIT_VALUE, holder.get());
        
        holder.set(key, "42");
        assertEquals(INIT_VALUE, holder.get());
    }
    
    @Test
    public void whenKeyAssigned_keyIsNeededWhenChange() {
        Object key = new Object();
        holder = createHolder(key);
        assertEquals(INIT_VALUE, holder.get());
        
        holder.set(key, "42");
        assertEquals("42", holder.get());
    }
    
    @Test
    public void whenKeyAssigned_givenNoKeyDoesNotChangeValue() {
        Object key = new Object();
        holder = createHolder(key);
        assertEquals(INIT_VALUE, holder.get());
        
        holder.set("42");
        assertEquals(INIT_VALUE, holder.get());
    }
    
    @Test
    public void whenKeyAssigned_theKeyEqualsIsUsed() {
        Object key = new Object() {
            @Override
            public boolean equals(Object obj) {
                return (this == obj);
            }
        };
        Object fakeKey = new Object() {
            @Override
            public boolean equals(Object obj) {
                return true;
            }
        };
        
        holder = createHolder(key);
        assertEquals(INIT_VALUE, holder.get());
        
        holder.set(key, "42");
        assertEquals("42", holder.get());
        
        holder.set(fakeKey, "24");
        assertEquals("42", holder.get());
    }
    
}
