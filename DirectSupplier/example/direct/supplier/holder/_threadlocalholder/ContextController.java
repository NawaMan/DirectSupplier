package direct.supplier.holder._threadlocalholder;

import java.io.PrintStream;
import java.util.Optional;

public class ContextController {
    
    private PrintStream out = System.out;
    
    private PrintStream err = System.err;
    
    private Context context = new Context() {
        
        @Override
        public PrintStream out() {
            return ContextController.this.out;
        }
        
        @Override
        public PrintStream err() {
            return ContextController.this.err;
        }
        
    };
    
    
    public PrintStream out() {
        return this.out;
    }
    
    public PrintStream err() {
        return this.err;
    }
    
    public void out(PrintStream out) {
        this.out = Optional.ofNullable(out).orElse(System.out);
    }
    
    public void err(PrintStream err) {
        this.err = Optional.ofNullable(err).orElse(System.err);
    }
    
    public Context getContext() {
        return this.context;
    }
    
}
