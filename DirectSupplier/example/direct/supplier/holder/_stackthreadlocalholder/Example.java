package direct.supplier.holder._stackthreadlocalholder;

import direct.supplier.holder.StackThreadLocalHolder;
import direct.supplier.holder.TestStackThreadLocalHolder;
import direct.supplier.holder.ThreadLocalHolder;

/**
 * See the example in {@link TestStackThreadLocalHolder}.
 * 
 * StackThreadLocalSupplier can supplier separate resource for each thread similar to {@link ThreadLocalHolder}
 *     but also allows more resource to be used in FILO or stack fashion in the same thread.
 * 
 * In this example, a resource provide access to print out console.
 * A {@link StackThreadLocalHolder} is created to allows resources to be accessed thread safe as they are local t
 *   to each thread.
 * The {@link StackThreadLocalHolder} also stack each printout resources with indentation prefix.
 * This way the client code does not need to be away about the indentation or thread safe.
 * It can ask for the resource and call print.
 * 
 * Similar technique is used to provide nestable database transaction in something like JPA.
 * 
 * @author  NawaMan
 **/
public class Example extends TestStackThreadLocalHolder {
    
}
