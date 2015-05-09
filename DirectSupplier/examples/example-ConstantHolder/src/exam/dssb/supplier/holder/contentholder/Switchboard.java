package exam.dssb.supplier.holder.contentholder;

import java.util.function.Supplier;

/**
 * The switchboard will provide the supplier for startTime.
 * 
 * @author  NawaMan
 **/
public class Switchboard {
    
    /** The switchboard instance. */
    private static Switchboard instance = null;
    
    /** Singleton instance */
    public static Switchboard instance() {
        return instance(null);
    }
    
    /**
     * Change and returns the instance.
     **/
    public static Switchboard instance(Switchboard instance) {
        if (Switchboard.instance == null) {
            if (instance != null) {
                Switchboard.instance = instance;
            } else {
                Switchboard.instance = new Switchboard(()-> System.currentTimeMillis());
            }
        }
        return Switchboard.instance;
    }
    
    /**
     * Reset the instance -- only allow for the local classes including the test.
     **/
    protected static void resetInstance() {
        Switchboard.instance = null;
    }
    
    /**
     * The supplier for startTime.
     **/
    private Supplier<Long> startTimeSupplier;
    
    /** The constructor. */
    public Switchboard(
            Supplier<Long> startTimeSupplier) {
        this.startTimeSupplier = startTimeSupplier;
    }
    
    /**
     * Returns the supplier for start time.
     **/
    public Supplier<Long> getStartTimeSupplier() {
        return this.startTimeSupplier;
    }
    
}
