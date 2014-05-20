package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;

/**
 *
 * @author Petr Ječmen
 */
public abstract class MoveTrigger<T> extends Trigger<T> {
    
    public MoveTrigger(BoundingVolume volume, T target, String action, boolean once) {
        super(volume, target, action, once);
    }
    
    public abstract void onEnter();
    
    public abstract void onLeave();
    
}
