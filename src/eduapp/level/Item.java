package eduapp.level;

/**
 *
 * @author Petr Ječmen
 */
public class Item {

    protected ItemRegistry itemRegistry;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }
}
