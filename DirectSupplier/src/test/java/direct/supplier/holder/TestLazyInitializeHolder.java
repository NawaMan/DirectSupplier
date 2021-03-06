package direct.supplier.holder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

import static direct.supplier.holder.LazyInitializeHolder.*;

public class TestLazyInitializeHolder {
    
    @Test
    public void ensureInitializeOnce__thisTestWillTakesUpToFiveSeconds()
                throws Exception {
        final AtomicInteger     count           = new AtomicInteger(5);
        final ArrayList<String> allInitialized  = new ArrayList<>();
        
        class StrSupplier implements Supplier<String> {
            @Override
            public String get() {
                sleep(5);
                String initializedValue = "Int-" + count.incrementAndGet();
                
                // Record all that are initialized.
                allInitialized.add(initializedValue);
                return initializedValue;
            }
        }
        
        // Set up threads to access to the holder.
        int testSize = 1000;
        LazyInitializeHolder<String> lazyInitHolder = lazyInitialize(new StrSupplier());
        CyclicBarrier gate = new CyclicBarrier(testSize + 1);
        CountDownLatch latch = new CountDownLatch(testSize);
        for (int i = 0; i < testSize; i++) {
            new Thread(()->{
                waitToStartAtTheSameTime(gate);
                
                // There should only be one initialization.
                String value = lazyInitHolder.get();
                assertEquals("There should be only one initialized.",            1,                     allInitialized.size());
                assertEquals("The value should be the one that is initialized.", allInitialized.get(0), value);
                latch.countDown();
            }).start();
        }
        
        waitToStartAtTheSameTime(gate);
        latch.await();
        
        // There should only be one initialization.
        String value = lazyInitHolder.get();
        assertEquals("There should be only one initialized.",            1,                     allInitialized.size());
        assertEquals("The value should be the one that is initialized.", allInitialized.get(0), value);
        
        // After all thread are done, there should still be one value initialized.
        assertEquals("There should be only one initialized.",            1,                     allInitialized.size());
        assertEquals("The value should be the one that is initialized.", allInitialized.get(0), value);
    }
    
    protected void waitToStartAtTheSameTime(
            final CyclicBarrier gate) {
        try {
            gate.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sleep(
            final int seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
