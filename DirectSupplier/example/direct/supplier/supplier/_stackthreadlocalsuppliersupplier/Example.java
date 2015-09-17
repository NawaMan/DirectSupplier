package direct.supplier.supplier._stackthreadlocalsuppliersupplier;

import dierct.supplier.holder.ThreadLocalHolder;
import dierct.supplier.supplier.StackThreadLocalSupplierSupplier;
import direct.supplier.TestStackThreadLocalSupplierSupplier;

/**
 * See the example in {@link TestStackThreadLocalSupplierSupplier}.
 * 
 * StackThreadLocalSupplier can supplier separate resource for each thread similar to {@link ThreadLocalHolder}
 *     but also allows more resource to be used in FILO or stack fashion in the same thread.
 * 
 * In this example, a resource provide access to print out console.
 * A {@link StackThreadLocalSupplierSupplier} is created to allows resources to be accessed thread safe as they are local t
 *   to each thread.
 * The {@link StackThreadLocalSupplierSupplier} also stack each printout resources with indentation prefix.
 * This way the client code does not need to be away about the indentation or thread safe.
 * It can ask for the resource and call print.
 * 
 * Similar technique is used to provide nestable database transaction in something like JPA.
 * 
 * @author  NawaMan
 **/
public class Example extends TestStackThreadLocalSupplierSupplier {
    
}
