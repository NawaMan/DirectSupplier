package direct.supplier.holder._constantholder;

import java.util.function.Supplier;

/**
 * The switchboard will provide the supplier for the value.
 * 
 * @author NawaMan
 **/
public class Switchboard {
    
    /** The switchboard instance. */
    private static Switchboard instance = new Switchboard();
    
    /** Singleton instance */
    public static Switchboard getInstance() {
        return replaceSwitchboard(null);
    }
    
    /** Singleton instance */
    public static Switchboard switchboard() {
        return replaceSwitchboard(null);
    }
    
    /**
     * Change and returns the instance.
     **/
    protected static Switchboard replaceSwitchboard(Switchboard instance) {
        if (instance != null) {
            Switchboard.instance = instance;
        }
        return Switchboard.instance;
    }
    
    public static <T> T using(Switchboard instance, Supplier<T> action) {
        Switchboard prevValue = Switchboard.instance;
        try {
            replaceSwitchboard(instance);
            T result = action.get();
            return result;
        } finally {
            replaceSwitchboard(prevValue);
        } 
    }
    
    public Switchboard() {
        this.value = null;
    }
    
    //== Value =========================================================================================================
    
    public static final String REAL_VALUE = "Real value";
    
    /**
     * The supplier for value.
     **/
    protected Supplier<String> value;
    
    public Supplier<String> value() {
        if (this.value == null) {
            this.value = ()-> REAL_VALUE;
        }
        return this.value;
    }
    
}
