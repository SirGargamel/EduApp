package eduapp.level.trigger;

import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Ječmen
 */
public abstract class ActionTrigger<T> extends Trigger<T> {        
    
    public ActionTrigger(Spatial volume, T target, String action, boolean once) {
        super(volume, target, action, once);
    }
    
    public abstract void onActivate();
}
