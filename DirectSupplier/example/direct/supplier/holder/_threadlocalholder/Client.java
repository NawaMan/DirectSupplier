package direct.supplier.holder._threadlocalholder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

import dierct.supplier.holder.ThreadLocalHolder;

/**
 * This example shows that a resource supplier used in separate thread can be isolated.
 * 
 * @author NawaMan
 */
public class Client {
    
    @Test
    public void test() throws InterruptedException {
        
        CyclicBarrier gate = new CyclicBarrier(2 + 1);
        CountDownLatch latch = new CountDownLatch(2);
        
        Supplier<ContextController> contextController = ThreadLocalHolder.of(()->new ContextController());
        
        AtomicInteger count = new AtomicInteger();
        
        new Thread(()->{
            waitToStartAtTheSameTime(gate);
            
            String str = "Hello there!";
            
            // Same supplier (contextController) but return (from get()) with difference object. One for each thread.
            String buff = contextController.get().run(context->{
                context.out().println(str);
                count.incrementAndGet();
                
                sleep(100);
                
                context.out().println(str);
            });
            
            // Ensure that the first print got both thread are done.
            assertTrue(count.get() > 1);
            // Ensure that the buffer is only for this thread and not interfere with another thread.
            assertEquals(str + "\n" + str + "\n", buff);
            
            latch.countDown();
        }).start();
        
        new Thread(()->{
            waitToStartAtTheSameTime(gate);
            
            String str = "Hi there!";
            
            // Same supplier (contextController) but return (from get()) with difference object. One for each thread.
            String buff = contextController.get().run(context->{
                context.out().println(str);
                count.incrementAndGet();
                
                sleep(100);
                
                context.out().println(str);
            });
            
            // Ensure that the first print got both thread are done.
            assertTrue(count.get() > 1);
            // Ensure that the buffer is only for this thread and not interfere with another thread.
            assertEquals(str + "\n" + str + "\n", buff);
            
            latch.countDown();
        }).start();
        
        waitToStartAtTheSameTime(gate);
        latch.await();
    }
    
    protected void sleep(long pause) {
        try {
            Thread.sleep(pause);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void waitToStartAtTheSameTime(
            final CyclicBarrier gate) {
        try {
            gate.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
