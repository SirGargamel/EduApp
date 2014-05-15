package eduapp.level;

/**
 *
 * @author Petr Jecmen
 */
public abstract class ActionItem {
    
    private String id;
    
    public ActionItem() {
        id = "";
    }
    
    public abstract void preformAction(final String action);
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
