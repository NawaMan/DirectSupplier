package direct.supplier.holder._lazyinitializeholder;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

import dierct.supplier.holder.LazyInitializeHolder;

/**
 * This class needs the value.
 **/
public class Client {
    
    private static final int INT_VALUE = 123456;
    
    private static final AtomicInteger count = new AtomicInteger(INT_VALUE);
    
    private static final Supplier<String> VALUE_HOLDER = LazyInitializeHolder.of(()-> "V" + count.getAndIncrement());
    
    public String getValue() {
        return VALUE_HOLDER.get();
    }
    
    //== Main ==========================================================================================================
    
    /**
     * The idea here is that multiple instance of Client can ask for the same value.
     * All, with the help of {@code LazyInitializeHolder}, the value was initialized only once.
     **/
    public static void main(String ... args) {
        for (int i = 0; i < 1000; i++) {
            String value = new Client().getValue();
            System.out.println(i + ": " + value);
            assertEquals("V" + INT_VALUE, value);
        }
        
    }
    
    //== Test ==========================================================================================================
    
    /**
     * Just run the main as a test.
     **/
    @Test
    public void test() {
        for (int i = 0; i < 1000; i++) {
            String value = new Client().getValue();
            assertEquals("V" + INT_VALUE, value);
        }
    }
    
}
