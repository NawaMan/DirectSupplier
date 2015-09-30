package direct.supplier.holder;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Lazy Initialize holder initializes the value on the first request.
 * 
 * This holder can be used to implement something equivalent to an lazy-loaded application scope bean.
 * 
 * The implementation ensures that the value is only initialized only once even if it was accessed from multiple
 *   threads.
 * 
 * NOTE: This holder has three states.
 *         1. Uninitialized -- isInitialized is null.
 *         2. Initializing  -- isInitialized is false.
 *         3. Initialized   -- isInitialized is true.
 * 
 * @author NawaMan
 **/
public class LazyInitializeHolder<V>
                implements Supplier<V> {
    
    private volatile AtomicBoolean isInitialized = null;
    
    private final AtomicReference<V> value = new AtomicReference<>();
    
    private final Supplier<? extends V> valueProducer;
    
    /**
     * Default constructor.
     * 
     * This should only be used when the {@link LazyInitializeHolder#initialize()} is overridden.
     **/
    public LazyInitializeHolder() {
        this.valueProducer = null;
    }
    
    /**
     * Construct the holder with the producer of the value.
     * 
     * @param valueProducer
     *          the value producer.
     **/
    public LazyInitializeHolder(
            final Supplier<? extends V> valueProducer) {
        this.valueProducer = Objects.requireNonNull(valueProducer);
    }
    
    /** {@inheritDoc} */
    @Override
    public V get() {
        ensureInitialized();
        final V value = this.value.get();
        return value;
    }
    
    private void ensureInitialized() {
        while ((this.isInitialized == null) || !this.isInitialized.get()) {
            if (this.isInitialized != null) {
                continue;
            }
            synchronized (this) {
                if (this.isInitialized != null) {
                    continue;
                }
                
                this.isInitialized = new AtomicBoolean(false);
            }
            
            final V value = initialize();
            this.value.set(value);
            
            this.isInitialized.set(true);
        }
    }
    
    /**
     * Initialize the value.
     * 
     * The default implementation is to ask the value producer to produce the value.
     * 
     * @return the initialized value.
     **/
    protected V initialize() {
        final V value = Objects.requireNonNull(this.valueProducer).get();
        return value;
    }
    
    //== Static creation ===============================================================================================
    
    /**
     * Create and return a new {@code LazyInitializeHolder}.
     **/
    public static <T> LazyInitializeHolder<T> lazyInitialize(
            final Supplier<? extends T> initializer) {
        return new LazyInitializeHolder<T>(initializer);
    }
    
    /**
     * Create and return a new {@code LazyInitializeHolder}.
     **/
    public static <T> LazyInitializeHolder<T> of(
            final Supplier<? extends T> initializer) {
        return new LazyInitializeHolder<T>(initializer);
    }
    
}
