package dierct.supplier.holder;

import java.util.Objects;
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
 * @author NawaMan
 **/
public class LazyInitializeHolder<V>
                implements Supplier<V> {
    
    private final AtomicReference<Supplier<V>> value = new AtomicReference<>();
    
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
        final V value = this.value.get().get();
        return value;
    }
    
    private void ensureInitialized() {
        this.value.compareAndSet(null, ()->{
            final V value = initialize();
            this.value.set(()->value);
            return value;
        });
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
