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
    
    public abstract void preformActionEnter(final String action);
    
    public abstract void preformActionLeave(final String action);
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
