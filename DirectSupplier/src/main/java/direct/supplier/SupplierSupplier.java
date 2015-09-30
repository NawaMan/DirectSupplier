package direct.supplier;

import java.util.function.Supplier;

/**
 * Classes implementing this interface can supply a supplier.
 * 
 * Usually classes implement this interface aims to supply a resource.
 * But they also allows write access such as setting or changing the resource.
 * If write access is not allowed, these objects can returns a supplier to access to its resource.
 * Otherwise, the objects itself can be used (as a supplier of the resource).
 * 
 * @author NawaMan
 */
@FunctionalInterface
public interface SupplierSupplier<R> extends Supplier<R> {

    /**
     * Gets a supplier.
     *
     * @return the supplier
     */
    default public Supplier<R> getSupplier() {
        return ()->get();
    }
    
    /**
     * Gets a resource.
     * 
     * @return the resource.
     */
    public R get();
    
}
