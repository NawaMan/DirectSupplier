package exam.dssb.supplier.holder.contentholder;

import java.util.function.Supplier;

/**
 * This class needs the startTime value.
 **/
public class Client {
    
    private Supplier<Long> startTime = Switchboard.instance().getStartTimeSupplier();
    
    public long getStartTime() {
        return startTime.get();
    }
    
}
