package eduapp.level.trigger;

import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Ječmen
 */
public abstract class MoveTrigger<T> extends Trigger<T> {

    public MoveTrigger(Spatial volume, T target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }

    public abstract void onEnter();

    public abstract void onLeave();
}
