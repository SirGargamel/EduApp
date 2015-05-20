package eduapp.level.item;

import eduapp.Utils;


public class VirtualItem extends Item {
    
    private final String data;
    
    public VirtualItem(final String id) {
        super();
        setId(id);
        data = Utils.convertNumbersToLowerIndexes(id);
    }
    
    @Override
    public String getParam(final String id) {
        return data;
    }
    
}
