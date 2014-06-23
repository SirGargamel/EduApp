package eduapp.level;

import eduapp.ItemRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Petr Ječmen
 */
public class Item extends Observable implements Comparable<Item> {

    private static final String FIELD_COMPARE = ItemParameters.NAME;
    private static final List<String> NO_ESCAPE;
    protected ItemRegistry itemRegistry;
    private String id;
    private Map<String, String> params;
    private final Set<String> children;

    static {
        NO_ESCAPE = new ArrayList<>();
        NO_ESCAPE.add(ItemParameters.ICON);
        NO_ESCAPE.add(ItemParameters.LINKS);
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
        String fixedP = param;

        if (!NO_ESCAPE.contains(id)) {
            final String base = "208";
            final Matcher m = Pattern.compile("[0-9]").matcher(fixedP);
            String val;
            int code;
            while (m.find()) {
                val = m.group();
                code = Integer.valueOf(base.concat(val), 16);
                fixedP = fixedP.replaceAll(
                        val,
                        String.valueOf((char) code));
            }
        }

        params.put(id, fixedP);
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
