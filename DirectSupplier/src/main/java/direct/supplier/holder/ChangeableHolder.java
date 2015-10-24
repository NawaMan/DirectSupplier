package direct.supplier.holder;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import direct.supplier.SupplierSupplier;

/**
 * This resource holder holds a value that can be changed.
 * 
 * If a key is assigned, the key must be used to perform the changes.
 * The check is done by asking the assigned key to check equals.
 * If no key was given, the method without key must be used.
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
        this.doSet(value);
    }
    
    @Override
    public final V get() {
        return this.value.get();
    }
    
    private boolean checkKeyNotAssigned() {
        return this.key != null;
    }
    
    private boolean checkMatchedKey(
            final Object key) {
        if (this.key == null) {
            return false;
        }
        if (!Objects.equals(this.key.orElse(null), key)) {
            return false;
        }
        return true;
    }
    
    private boolean doSet(
            final V value) {
        synchronized (this) {
            this.value.set(value);
        }
        return true;
    }
    
    private boolean doCompareAndSupply(
            final V           expectedValue,
            final Supplier<V> valueSupplier) {
        if (Objects.equals(expectedValue, this.value.get())) {
            synchronized (this) {
                V currentValue = this.value.get();
                if (Objects.equals(expectedValue, currentValue)) {
                    this.value.set(valueSupplier.get());
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean doCheckAndUpdate(
            final Predicate<V>  expectedChecker,
            final Function<V,V> valueUpdater) {
        if (expectedChecker.test(this.value.get())) {
            synchronized (this) {
                V currentValue = this.value.get();
                if (expectedChecker.test(currentValue)) {
                    this.value.set(valueUpdater.apply(currentValue));
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Given no key was assigned. Change the value.
     * 
     * This is be successful only if the key was not set.
     * 
     * @param  value  the value.
     * @return {@code true} if successful.
     */
    public final boolean set(
            final V value) {
        boolean isAllowed = !checkKeyNotAssigned();
        boolean isSuccess = isAllowed && doSet(value);
        return isSuccess;
    }
    
    /**
     * Given a key was assigned. Change the value.
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
        boolean isAllowed = checkMatchedKey(key);
        boolean isSuccess = isAllowed && doSet(value);
        return isSuccess;
    }
    
    /**
     * Given no key was assigned. Check if the expected value is equal the current value. If so, change the value to the
     *   the one given by the supplier.
     * 
     * This is be successful only if the key was not set.
     * 
     * @param  expectedValue  the expected current value.
     * @param  valueSupplier  the value supplier.
     * @return {@code true} if successful.
     */
    public final boolean compareAndSupply(
            final V           expectedValue,
            final Supplier<V> valueSupplier) {
        boolean isAllowed = !checkKeyNotAssigned();
        boolean isSuccess = isAllowed && doCompareAndSupply(expectedValue, valueSupplier);
        return isSuccess;
    }
    
    /**
     * Given a key was assigned. Check if the expected value is equal the current value. If so, change the value to the
     *   the one given by the supplier.
     * 
     * This will only be successful only if the key was set and equals to the given key.
     * 
     * @param  key            the key.
     * @param  expectedValue  the expected current value.
     * @param  valueSupplier  the value supplier.
     * @return {@code} true if success.
     */
    public final boolean compareAndSupply(
            final Object      key,
            final V           expectedValue,
            final Supplier<V> valueSupplier) {
        boolean isAllowed = checkMatchedKey(key);
        boolean isSuccess = isAllowed && doCompareAndSupply(expectedValue, valueSupplier);
        return isSuccess;
    }
    
    /**
     * Given no key was assigned. Check if the expected value is equal the current value. If so, change the value to the
     *   the one given by the updater.
     * 
     * @param  expectedChecker the check if the expected value.
     * @param  valueUpdater   the supplier of the new value.
     * @return {@code true} if successful.
     */
    public final boolean checkAndUpdate(
            final Predicate<V>  expectedChecker,
            final Function<V,V> valueUpdater) {
        boolean isAllowed = !checkKeyNotAssigned();
        boolean isSuccess = isAllowed && doCheckAndUpdate(expectedChecker, valueUpdater);
        return isSuccess;
    }
    
    /**
     * Given a key was assigned. Check if the expected value is equal the current value. If so, change the value to the
     *   the one given by the updater.
     * 
     * @param  key             the key.
     * @param  expectedChecker the check if the expected value.
     * @param  valueUpdater    the value updater.
     * @return {@code} true if success.
     */
    public final boolean checkAndUpdate(
            final Object        key,
            final Predicate<V>  expectedChecker,
            final Function<V,V> valueUpdater) {
        boolean isAllowed = checkMatchedKey(key);
        boolean isSuccess = isAllowed && doCheckAndUpdate(expectedChecker, valueUpdater);
        return isSuccess;
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
    /**
     * Create and return a new {@code ChangeableHolder} without a key.
     **/
    public static <T> ChangeableHolder<T> changeable(
            final T value) {
        return of(NOKEY, value);
    }
    
    /**
     * Create and return a new {@code ChangeableHolder} without a key.
     **/
    public static <T> ChangeableHolder<T> of(
            final T value) {
        return new ChangeableHolder<T>(NOKEY, value);
    }
    
}
