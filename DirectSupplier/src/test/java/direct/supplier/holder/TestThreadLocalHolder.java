package direct.supplier.holder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

import direct.supplier.holder.ThreadLocalHolder;

public class TestThreadLocalHolder {
    
    private static class Counter {
        
        private static List<Counter> counters = new ArrayList<>();
        
        private int count = 0;
        
        public Counter() {
            synchronized (Counter.class) {
                counters.add(this);
            }
        }
        
        public int count() {
            this.count++;
            return this.count;
        }
        
        public int peek() {
            return this.count;
        }
        
    }
    
    @Test
    public void testGet_ensureEachThreadHasOne__thisTestWillTakesUpToFiveSeconds()
                throws InterruptedException {
        ThreadLocalHolder<Counter> counter = new ThreadLocalHolder<>(Counter::new);
        
        // Set up threads to access to the holder.
        int testSize  = 1000;
        int testCount = 1000;
        
        CyclicBarrier gate = new CyclicBarrier(testSize + 1);
        CountDownLatch latch = new CountDownLatch(testSize);
        for (int i = 0; i < testSize; i++) {
            new Thread(()->{
                waitToStartAtTheSameTime(gate);
                
                for (int c = 0; c < testCount; c++) {
                    assertEquals("The count should goes up by one every time", c + 1, counter.get().count());
                    sleep(5);
                }
                
                latch.countDown();
            }).start();
        }
        waitToStartAtTheSameTime(gate);
        latch.await();
        
        assertEquals("There should be the same counters as the testSize.",  testSize, Counter.counters.size());
        
        Counter.counters.stream().forEach(c->
            assertEquals("The counted value must be testCount for all counters.", testCount, c.peek())
        );
    }
    
    private void waitToStartAtTheSameTime(
            final CyclicBarrier gate) {
        try {
            gate.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sleep(
            final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
