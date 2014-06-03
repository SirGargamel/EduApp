package eduapp;

import eduapp.level.Item;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Petr Jecmen
 */
public class ItemRegistry {

    private static final ItemRegistry INSTANCE;
    private final Map<String, Item> data;
    
    static {
        INSTANCE = new ItemRegistry();
    }

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        data = new HashMap<>();
    }

    public void put(final Item item) {
        data.put(item.getId(), item);
        item.setItemRegistry(this);
    }

    public Item get(final String id) {
        return data.get(id);
    }

    public void clear() {
        data.clear();
    }
}
