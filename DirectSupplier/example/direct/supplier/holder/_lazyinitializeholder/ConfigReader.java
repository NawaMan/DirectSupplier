package direct.supplier.holder._lazyinitializeholder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigReader {
    
    private final AtomicInteger readCounter = new AtomicInteger(0);
    
    private final AtomicInteger loadCounter = new AtomicInteger(0);
    
    private Map<String, String> config = null;
    
    public ConfigReader() {
        
    }
    
    public Map<String, String> getConfig() {
        if (this.config == null) {
            this.loadConfig();
        }
        this.readCounter.incrementAndGet();
        return Collections.unmodifiableMap(this.config);
    }
    
    private void loadConfig() {
        this.loadCounter.incrementAndGet();
        
        Properties  properties = new Properties();
        InputStream inStream   = this.getClass().getResourceAsStream("/direct/supplier/holder/_lazyinitializeholder/testconfig.properties");
        try {
            properties.load(inStream);
            this.config = new TreeMap<>();
            properties.keySet().stream().forEach((key)->
                this.config.put(String.valueOf(key), String.valueOf(properties.get(key)))
            );
        } catch(IOException problem) {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public int getReadCount() {
        return this.readCounter.get();
    }
    
    public int getLoadCount() {
        return this.loadCounter.get();
    }
    
}
