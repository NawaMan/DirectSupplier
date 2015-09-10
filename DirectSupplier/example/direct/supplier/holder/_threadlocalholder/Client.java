package direct.supplier.holder._threadlocalholder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.Test;

import dierct.supplier.holder.ThreadLocalHolder;

public class Client {
    
    @Test
    public void test() throws InterruptedException {
        
        int testSize = 10;
        
        CyclicBarrier gate = new CyclicBarrier(testSize + 1);
        CountDownLatch latch = new CountDownLatch(testSize);
        
        AtomicInteger threadIndex = new AtomicInteger();
        
        for(int i = 0; i < testSize; i++) {
            String prefix = "#" + threadIndex.getAndIncrement() + ": ";
            
            newExecution(prefix, context-> {
                waitToStartAtTheSameTime(gate);
                
                // The execution does not need to care if the context is thread safe.
                
                context.get().out().println("Hello");
                context.get().out().println("World");
                context.get().out().println("!!!");
                
                latch.countDown();
            });
        }
        
        waitToStartAtTheSameTime(gate);
        latch.await();
    }
    
    protected static void waitToStartAtTheSameTime(
            final CyclicBarrier gate) {
        try {
            gate.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void newExecution(String prefix, Consumer<Supplier<Context>> runner) {
        new Thread(()-> {
            ContextController controller = new ContextController();
            
            StringBuffer outBuff   = new StringBuffer();
            PrintStream  outStream = new PrintStream(new ByteArrayOutputStream()) {
                @Override
                public void println(String x) {
                    outBuff.append(prefix + x + "\n");
                }
            };
            controller.out(outStream);
            
            Supplier<Context> context = ThreadLocalHolder.of(()->controller.getContext());
            runner.accept(context);
            
            System.out.println(outBuff.toString().replaceAll("#[0-9]: ", ""));
        }).start();
    }
    
}
