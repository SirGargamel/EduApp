package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;
import com.jme3.scene.Spatial;
import eduapp.level.item.Item;

/**
 *
 * @author Petr Ječmen
 */
public abstract class Trigger<T> extends Item {

    protected final String action;
    protected final T target;
    private final boolean once;
    private final Spatial volume;
    private boolean active;

    public Trigger(Spatial volume, T target, String action, boolean once, boolean active) {
        this.volume = volume;
        this.target = target;
        this.action = action;
        this.once = once;
        this.active = active;
    }

    public boolean isOnce() {
        return once;
    }

    public BoundingVolume getVolume() {
        return volume.getWorldBound();
    }

    public Spatial getGeometry() {
        return volume;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "[" + volume + " | " + target + " | " + action + "]";
    }
}
