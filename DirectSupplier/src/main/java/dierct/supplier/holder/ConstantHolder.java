package dierct.supplier.holder;

import java.util.function.Supplier;

/**
 * Constant holder can hold a value which will never change.
 * 
 * This holder can be used to implement something equivalent to an eager application scope bean.
 * 
 * @author NawaMan
 **/
public class ConstantHolder<V>
        implements Supplier<V> {
    
    private final V value;
    
    /**
     * Construct the holder with a value.
     * 
     * @param value
     *          the value.
     **/
    public ConstantHolder(
            final V value) {
        this.value = value;
    }
    
    /**
     * Returns the value held in this holder.
     * 
     * {@inheritDoc}
     **/
    @Override
    public V get() {
        return this.value;
    }
    
    //== Static creation ===============================================================================================
    
    /**
     * Create and return a new {@code ConstantHolder}.
     **/
    public static <T> ConstantHolder<T> constant(T value) {
        return new ConstantHolder<T>(value);
    }
    
    /**
     * Create and return a new {@code ConstantHolder}.
     **/
    public static <T> ConstantHolder<T> of(T value) {
        return new ConstantHolder<T>(value);
    }
    
}
