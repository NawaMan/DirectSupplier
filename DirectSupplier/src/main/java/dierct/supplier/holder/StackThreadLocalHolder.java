package dierct.supplier.holder;

import java.util.Objects;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

import dierct.supplier.SupplierSupplier;

/**
 * This supplier actually can create a holder for stack thread local.
 * 
 * With this supplier, a new instance of the resource can be created and pushed into the thread local stack.
 * And another supplier can be create to obtain that resource value.
 * When needed, another instance can be created and pushed on the stack or pop out of the stack. 
 * 
 * @author NawaMan
 */
public class StackThreadLocalHolder<R>
        implements SupplierSupplier<R> {
    
    private ThreadLocal<Stack<R>> resource = new ThreadLocal<Stack<R>>() {
        @Override
        protected Stack<R> initialValue() {
            return new Stack<>();
        }
    };
    
    private final Function<R, R> creator;
    
    /**
     * Constructor.
     * 
     * @param supplier  the supplier to create new instance for each element.
     * @return  the stack thread local supplier.
     **/
    public StackThreadLocalHolder(
            final Supplier<R> supplier) {
        Objects.requireNonNull(supplier);
        
        this.creator = previous->supplier.get();
    }
    /**
     * Constructor.
     * 
     * @param function  the function to create new instance for each element given the previous instance.
     * @return  the stack thread local supplier.
     **/
    public StackThreadLocalHolder(
            final Function<R, R> function) {
        this.creator = Objects.requireNonNull(function);
    }
    
    @Override
    public R get() {
        if (this.resource.get().isEmpty()) {
            return null;
        } else {
            return this.resource.get().peek();
        }
    }
    
    /**
     * Create a new instance from the supplier and push on to the stack.
     * 
     * @return the instance.
     **/
    public R pushNew() {
        R previous = !this.isEmpty() ? this.peek() : null;
        R resource = this.creator.apply(previous);
        this.resource.get().push(resource);
        return resource;
    }
    
    /**
     * Push the given resource on to the stack.
     * 
     * @param  resource  the resource.
     * @return  the resource.
     */
    public R push(
            final R resource) {
        this.resource.get().push(resource);
        return resource;
    }
    
    /**
     * Pop of the top element.
     * 
     * @return the top element.
     */
    public R pop() {
        return this.resource.get().pop();
    }
    
    /**
     * Get the top element without removing it.
     * 
     * @return the top element.
     */
    public R peek() {
        return this.resource.get().peek();
    }
    
    /**
     * Check if the stack is empty.
     * 
     * @return {@code true} of the stack is empty.
     */
    public boolean isEmpty() {
        return this.resource.get().isEmpty();
    }
    
    /**
     * Returns the size of the stack.
     * 
     * @return the size.
     */
    public int size() {
        return this.resource.get().size();
    }
    
    //== Static creation ===============================================================================================
    
    /**
     * Create and return a new {@code StackThreadLocalSupplier}.
     **/
    public static <T> StackThreadLocalHolder<T> stackThreadLocal(
            final Supplier<T> supplier) {
        return new StackThreadLocalHolder<T>(supplier);
    }
    
    /**
     * Create and return a new {@code StackThreadLocalSupplier}.
     **/
    public static <T> StackThreadLocalHolder<T> of(
            final Supplier<T> supplier) {
        return new StackThreadLocalHolder<T>(supplier);
    }
    
    /**
     * Create and return a new {@code StackThreadLocalSupplier}.
     **/
    public static <T> StackThreadLocalHolder<T> stackThreadLocal(
            final Function<T,T> creator) {
        return new StackThreadLocalHolder<T>(creator);
    }
    
    /**
     * Create and return a new {@code StackThreadLocalSupplier}.
     **/
    public static <T> StackThreadLocalHolder<T> of(
            final Function<T,T> creator) {
        return new StackThreadLocalHolder<T>(creator);
    }
}
