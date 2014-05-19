package eduapp.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Petr Jecmen
 */
public class ActionItemRegistry {

    private final Map<String, List<ActionItem>> data;

    public ActionItemRegistry() {
        data = new HashMap<>();
    }

    public void put(final String id, final ActionItem item) {
        List<ActionItem> l = data.get(id);
        if (l == null) {
            l = new ArrayList<>();
            data.put(id, l);
        }
        l.add(item);
    }
    
    public List<ActionItem> get(final String id) {
        return data.get(id);
    }
    
    public void clear() {
        data.clear();
    }

}
