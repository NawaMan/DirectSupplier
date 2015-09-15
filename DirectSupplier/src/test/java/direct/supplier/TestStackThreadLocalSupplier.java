package direct.supplier;

import static org.junit.Assert.assertEquals;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

import dierct.supplier.StackThreadLocalSupplier;

public class TestStackThreadLocalSupplier {
    
    static class OutPrint {
        
        final StringBuffer buff = new StringBuffer();
        
        final String prefix;
        
        final OutPrint parent;
        
        OutPrint(
                String   prefix,
                OutPrint parent) {
            this.prefix = prefix;
            this.parent = parent;
        }
        
        public void println(String text) {
            if (this.parent != null) {
                this.parent.println(this.prefix + text);
            } else {
                buff.append(this.prefix + text + "\n");
            }
        }
        
    }
    
    @Test
    public void test() throws InterruptedException {
        StackThreadLocalSupplier<OutPrint> supplier = StackThreadLocalSupplier.of(parent->new OutPrint((parent == null) ? "" : "--> ", parent));
        
        int testSize = 10;
        
        Random random = new Random();
        
        CyclicBarrier gate = new CyclicBarrier(testSize + 1);
        CountDownLatch latch = new CountDownLatch(testSize);
        for (int i = 0; i < testSize; i++) {
            new Thread(()->{
                waitToStartAtTheSameTime(gate);
                sleep(random.nextInt(10));
                
                supplier.pushNew();
                OutPrint rootPrint = supplier.peek();
                supplier.get().get().println("one");
                sleep(random.nextInt(10));
                
                supplier.pushNew();
                supplier.get().get().println("two");
                supplier.get().get().println("three");
                sleep(random.nextInt(10));
                
                supplier.pushNew();
                supplier.get().get().println("four");
                supplier.get().get().println("five");
                sleep(random.nextInt(10));
                
                supplier.pop();
                supplier.get().get().println("six");
                supplier.get().get().println("seven");
                
                // Observe that
                //   1. There are multiple thread but the value never mixed.
                //   2. The indentation is proper.
                assertEquals(
                        "one\n" +
                        "--> two\n" +
                        "--> three\n" +
                        "--> --> four\n" +
                        "--> --> five\n" +
                        "--> six\n" +
                        "--> seven\n",
                        rootPrint.buff.toString());
                
                latch.countDown();
            }).start();
        }
        waitToStartAtTheSameTime(gate);
        latch.await();
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
