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
    
    public abstract void performActionEnter(final String action);
    
    public abstract void performActionLeave(final String action);        
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
