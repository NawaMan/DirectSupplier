package exam.dssb.supplier.holder.contentholder;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientTest {
    
    @Test
    public void test() {
        // Given...
        long time = 500L;
        Switchboard.resetInstance();
        Switchboard.instance(new Switchboard(()->time));
        // When getting the value,
        long obtainedTime = Switchboard.instance().getStartTimeSupplier().get();
        // The value should star
        assertEquals(time, obtainedTime);
    }
    
}
