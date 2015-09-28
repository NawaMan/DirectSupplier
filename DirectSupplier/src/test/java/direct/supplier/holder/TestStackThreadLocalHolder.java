package direct.supplier.holder;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

import dierct.supplier.holder.StackThreadLocalHolder;

public class TestStackThreadLocalHolder {
    
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
    public void testStackableAndThreadSafe() throws InterruptedException {
        StackThreadLocalHolder<OutPrint> supplier = StackThreadLocalHolder.of(parent->new OutPrint((parent == null) ? "" : "--> ", parent));
        
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
                supplier.get().println("one");
                sleep(random.nextInt(10));
                
                supplier.pushNew();
                supplier.get().println("two");
                supplier.get().println("three");
                sleep(random.nextInt(10));
                
                supplier.pushNew();
                supplier.get().println("four");
                supplier.get().println("five");
                sleep(random.nextInt(10));
                
                supplier.pop();
                supplier.get().println("six");
                supplier.get().println("seven");
                
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
    
    /**
     * Assert that the returned supplier can't be used to gain access to the {@link StackThreadLocalHolder}.
     */
    @Test
    public void ensureUnaccessible() {
        StackThreadLocalHolder<OutPrint> supplier = StackThreadLocalHolder.of(parent->new OutPrint((parent == null) ? "" : "--> ", parent));
        
        assertThat(supplier.getSupplier(), not(instanceOf(StackThreadLocalHolder.class)));
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
