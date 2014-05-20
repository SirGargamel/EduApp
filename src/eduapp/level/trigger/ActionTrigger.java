package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;

/**
 *
 * @author Petr Ječmen
 */
public abstract class ActionTrigger<T> extends Trigger<T> {        
    
    public ActionTrigger(BoundingVolume volume, T target, String action, boolean once) {
        super(volume, target, action, once);
    }
    
    public abstract void onActivate();
}
