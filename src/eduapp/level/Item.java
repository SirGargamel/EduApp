package eduapp.level;

import eduapp.ItemRegistry;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Petr Ječmen
 */
public class Item {

    protected ItemRegistry itemRegistry;
    private String id;
    private Map<String, String> params;
    
    public Item() {
        params= new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getParam(String id) {
        return params.get(id);
    }
    
    public void setParam(String id, String param) {
        params.put(id, param);
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }
}
