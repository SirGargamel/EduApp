package eduapp.level;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Petr Jecmen
 */
public class ItemRegistry {

    private final Map<String, Item> data;

    public ItemRegistry() {
        data = new HashMap<>();
    }

    public void put(final String id, final Item item) {
        data.put(id, item);
    }
    
    public Item get(final String id) {
        return data.get(id);
    }
    
    public void clear() {
        data.clear();
    }

}
