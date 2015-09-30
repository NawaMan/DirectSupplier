package direct.supplier.holder;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import direct.supplier.SupplierSupplier;

/**
 * This resource holder holds a value that can be changed.
 * 
 * @author NawaMan
 */
public class ChangeableHolder<V>
        implements SupplierSupplier<V> {
    
    /** The key to be used if no key is to be assigned. */
    public final static Object NOKEY = new Object();
    
    private final AtomicReference<V> value = new AtomicReference<>();
    
    private final Optional<Object> key;
    
    /**
     * Construct a holder with a key. If the key is {@code ChangeableHolder.NOKEY}, then no key is assigned.
     * 
     * @param  key  the key.
     */
    public ChangeableHolder(
            final Object key,
            final V      value) {
        this.key = (key == NOKEY) ? null : Optional.ofNullable(key);
        this.value.set(value);
    }
    
    @Override
    public final V get() {
        return this.value.get();
    }
    
    /**
     * Change the value without using key.
     * 
     * This is be successful only if the key was not set.
     * 
     * @param  value  the value.
     * @return {@code true} if successful.
     */
    public final boolean set(
            final V value) {
        if (this.key != null) {
            return false;
        }
        
        this.value.set(value);
        return true;
    }
    
    /**
     * Change the value using a key.
     * 
     * This will only be successful only if the key was set and equals to the given key.
     * 
     * @param  key    the key.
     * @param  value  the value.
     * @return {@code} true if success.
     */
    public final boolean set(
            final Object key,
            final V      value) {
        if (this.key == null) {
            return false;
        }
        if (!Objects.equals(this.key.orElse(null), key)) {
            return false;
        }
        
        this.value.set(value);
        return true;
    }
    
    //== Static creation ===============================================================================================
    
    /**
     * Create and return a new {@code ChangeableHolder}.
     **/
    public static <T> ChangeableHolder<T> changeable(
            final Object key,
            final T      value) {
        return of(key, value);
    }
    
    /**
     * Create and return a new {@code ChangeableHolder}.
     **/
    public static <T> ChangeableHolder<T> of(
            final Object key,
            final T      value) {
        return new ChangeableHolder<T>(key, value);
    }
    
}
