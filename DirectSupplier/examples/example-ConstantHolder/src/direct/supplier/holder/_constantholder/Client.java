package direct.supplier.holder._constantholder;

import java.util.function.Supplier;

import org.junit.Test;

import static dierct.supplier.holder.ConstantHolder.constant;
import static direct.supplier.holder._constantholder.Switchboard.*;
import static org.junit.Assert.assertEquals;

/**
 * This class needs the value.
 **/
public class Client {
    
    /**
     * The idea here is that Client does not need to know how the value is determined.
     * In this case, the value is just a string but it can be anything
     *     including an implementation of a functionality-providing interface.
     * 
     **/
    private Supplier<String> value = switchboard().value();
    
    public String getValue() {
        return value.get();
    }
    
    //== Main ==========================================================================================================
    
    /**
     * This main represent a production environment where the value is the actual one to be used.
     * If the value is a functionality-providing interface, it will be the default implementation
     *     such as actually going to the database or network.
     **/
    public static void main(String[] args) {
        String theValue      = Switchboard.REAL_VALUE;
        String obtainedValue = new Client().getValue();
        System.out.println("Value: " + obtainedValue);
        assertEquals(theValue, obtainedValue);
    }
    
    //== Test ==========================================================================================================
    
    /**
     * This test method demonstrates that the value can be replaced for testing purposes.
     **/
    @Test
    public void test() {
        // Given...
        String theValue = "Test value";
        Switchboard testSwitchboard = new Switchboard() {{
            value = constant(theValue);
        }};
        // When getting the value,
        String obtainedValue = using(testSwitchboard, ()-> new Client().getValue());
        // The value should star
        assertEquals(theValue, obtainedValue);
    }
    
}
