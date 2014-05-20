package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;

/**
 *
 * @author Petr Ječmen
 */
public abstract class Trigger<T> {

    protected final String action;
    protected T target;
    private final boolean once;
    private BoundingVolume volume;

    public Trigger(BoundingVolume volume, T target, String action, boolean once) {
        this.volume = volume;
        this.target = target;
        this.action = action;
        this.once = once;
    }

    public boolean isOnce() {
        return once;
    }

    public BoundingVolume getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "[" + volume + " | " + target + " | " + action + "]";
    }
}
