package direct.supplier.holder._lazyinitializeholder;

import static org.junit.Assert.assertEquals;

import static dierct.supplier.holder.LazyInitializeHolder.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;

import org.junit.Test;


public class Client {
    
    /**
     * The idea here is that ConfigReader which actually do the reading of the configuration does not have to be
     *   thread-safe.
     * And it does not have to be singleton.
     * Using {@link LazyInitializeHolder}, the configuration can be accessed in the way that its initialization is only
     *   made when needed and only made once no many how many thread are trying to access it.
     * 
     * This holder makes the configuration behave like a lazy application scope in JEE.
     **/
    private static final Supplier<Map<String, String>> config = lazyInitialize(()-> {
        return ConfigReader.getInstance().getConfig();
    });
    
    public Map<String, String> getConfigMap() {
        return config.get();
    }
    
    //== Main ==========================================================================================================
    
    /**
     * This main represent a production environment where the value is the actual one to be used.
     * If the value is a functionality-providing interface, it will be the default implementation
     *     such as actually going to the database or network.
     * @throws InterruptedException 
     **/
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        client.test();
        
        String config = client.getConfigMap().toString();
        System.out.println("Config: " + config);
        System.out.println("All Done!");
    }
    
    //== Test ==========================================================================================================
    
    /**
     * This test method demonstrates that the value can be replaced for testing purposes.
     * @throws InterruptedException 
     **/
    @Test
    public void test() throws InterruptedException {
        // Precondition - If this is not right, the test may be moo.
        assertEquals(0, ConfigReader.getInstance().getLoadCount());
        assertEquals(0, ConfigReader.getInstance().getReadCount());
        
        // Given 1000 thread are trying to access the configuration map.
        int count = 1000;
        CyclicBarrier gate = new CyclicBarrier(count + 1);
        CountDownLatch done = new CountDownLatch(count);
        for(int i = 0; i < count; i++) {
            new Thread(()-> {
                waitToStart(gate);
                
                String config = new Client().getConfigMap().toString();
                assertEquals("{one=1, three=3, two=2}", config);
                done.countDown();
            }).start();
        }
        
        waitToStart(gate);
        done.await();
        
        assertEquals(1, ConfigReader.getInstance().getLoadCount());
        assertEquals(1, ConfigReader.getInstance().getReadCount());
        
        String config = new Client().getConfigMap().toString();
        assertEquals("{one=1, three=3, two=2}", config);
    }
    
    protected void waitToStart(CyclicBarrier gate) {
        try {
            gate.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
