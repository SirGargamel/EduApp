package eduapp.level;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.Arrays;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class ActionItem {

    private final String volumeDescription;
    private final String action;
    private final boolean once;
    private BoundingVolume volume;

    public ActionItem(String volumeDescription, String action, boolean onlyOnce) {
        this.volumeDescription = volumeDescription;
        this.action = action;        
        this.once = onlyOnce;
    }

    public void generateAction(final Set<Spatial> items) {
        if (volumeDescription.contains(";")) {
            // numbers describing volume
            final String[] split = volumeDescription.split(";");
            if (split.length == 3) {
                volume = new BoundingSphere(
                        Float.valueOf(split[2]), 
                        new Vector3f(Float.valueOf(split[0]), Level.LEVEL_HEIGHT / 2.0f, Float.valueOf(split[1])));
            } else if (split.length == 4) {
                volume = new BoundingBox(
                        new Vector3f(Float.valueOf(split[0]), 0, Float.valueOf(split[1])),
                        new Vector3f(Float.valueOf(split[2]), Level.LEVEL_HEIGHT, Float.valueOf(split[3])));
            } else {
                throw new IllegalArgumentException("Unsupported bounding volume definition - " + Arrays.toString(split));
            }
        } else {
            // item id
            for (Spatial item : items) {
                if (volumeDescription.equals(item.getName())) {
                    volume = item.getWorldBound();
                    break;
                }
            }
        }
        if (volume == null) {
            System.err.println("No bounding volume !!!");
        }
    }

    public BoundingVolume getVolume() {
        return volume;
    }

    public String getAction() {
        return action;
    }

    public boolean isOnce() {
        return once;
    }

    @Override
    public String toString() {
        return volume.getCenter().toString() + " -- " + action;
    }

}
