package eduapp.level.item;


public class VirtualItem extends Item {
    
    public VirtualItem(final String id) {
        super();
        setId(id);
    }
    
    @Override
    public String getParam(String id) {
        return this.id;
    }
    
}
