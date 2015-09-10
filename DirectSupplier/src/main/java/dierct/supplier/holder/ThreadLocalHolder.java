package dierct.supplier.holder;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This holder holds one instance of value for each thread. This enables two interesting features.
 * 
 * <ol>
 *  <li>
 *    Allows an object to be shared without passing them around as they are stored with the thread.
 *    This technique is widely used to implement execution context like FacesContext.
 *  </li>
 *  <li>
 *    Make it easier to make efficiently use of non-thread-safe object as it is only initialized once per thread and
 *      not being shared across thread.
 *    A good example is {@link SimpleDateFormat}.
 *    </li>
 * </ol>
 * 
 * @author NawaMan
 **/
public class ThreadLocalHolder<R> implements Supplier<R> {
    
    private ThreadLocal<R> resource = new ThreadLocal<R>() {
        @Override
        protected R initialValue() {
            return ThreadLocalHolder.this.supplier.get();
        }
    };
    
    private final Supplier<R> supplier;
    
    /**
     * Constructor.
     **/
    public ThreadLocalHolder(
            final Supplier<R> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }
    
    /**
     * Returns the value held in this holder.
     * 
     * {@inheritDoc}
     **/
    @Override
    public R get() {
        return this.resource.get();
    }
    
    //== Static creation ===============================================================================================
    
    /**
     * Create and return a new {@code ThreadLocalHolder}.
     **/
    public static <T> ThreadLocalHolder<T> threadLocal(
            final Supplier<T> valueSupplier) {
        return new ThreadLocalHolder<T>(valueSupplier);
    }
    
    /**
     * Create and return a new {@code ThreadLocalHolder}.
     **/
    public static <T> ThreadLocalHolder<T> of(
            final Supplier<T> valueSupplier) {
        return new ThreadLocalHolder<T>(valueSupplier);
    }
    
}
