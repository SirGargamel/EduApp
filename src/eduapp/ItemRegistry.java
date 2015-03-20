package eduapp;

import eduapp.level.item.Item;
import eduapp.level.item.VirtualItem;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (data.containsKey(id)) {
            return data.get(id);
        } else {
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.FINE, "Using virtual item for {0}", id);
            return new VirtualItem(id);
        }
    }

    public Collection<Item> listItems() {
        return data.values();
    }

    public void clear() {
        data.clear();
    }
}
