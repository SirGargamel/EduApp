package eduapp.level.item;

import eduapp.ItemRegistry;
import eduapp.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 *
 * @author Petr Ječmen
 */
public class Item extends Observable implements Comparable<Item> {

    private static final String FIELD_COMPARE = ItemParameter.NAME;
    private static final List<String> NO_ESCAPE;
    protected ItemRegistry itemRegistry;
    private String id;
    private Map<String, String> params;
    private final Set<String> children;

    static {
        NO_ESCAPE = new ArrayList<>();        
        NO_ESCAPE.add(ItemParameter.LINKS);
    }

    public Item() {
        params = new HashMap<>();
        children = new HashSet<>();
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
        if (!NO_ESCAPE.contains(id)) {
            params.put(id, Utils.convertNumbersToLowerIndexes(param));
        } else {
            params.put(id, param);
        }
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public void addChild(final String id) {
        children.add(id);
    }

    public Set<String> getChildren() {
        return children;
    }

    @Override
    public int compareTo(Item o) {
        final String s1 = getParam(FIELD_COMPARE);
        final String s2 = o.getParam(FIELD_COMPARE);
        if (s1 != null) {
            return s1.compareTo(s2);
        } else if (s2 != null) {
            return s2.compareTo(s1);
        } else {
            return 0;
        }
    }
}
