package direct.supplier.holder._threadlocalholder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ContextController {
    
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    
    private Context context = new Context() {
        
        private PrintStream out = new PrintStream(buffer);
        
        @Override
        public PrintStream out() {
            return out;
        }
        
    };
    
    public Context getContext() {
        return this.context;
    }
    
    public String getBufferedData() {
        return this.buffer.toString();
    }
    
}
