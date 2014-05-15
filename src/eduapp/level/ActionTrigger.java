package eduapp.level;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Petr Jecmen
 */
public class ActionTrigger extends ActionItem {

    private static final float DEFAULT_PLAYER_HEIGHT = 0.75f;
    private final String volumeDescription;
    private final String target;
    private final String action;
    private final boolean once;
    private BoundingVolume volume;

    public ActionTrigger(String volumeDescription, String target, String action, boolean onlyOnce) {
        this.volumeDescription = volumeDescription;
        this.target = target;
        this.action = action;
        this.once = onlyOnce;
    }

    public void generateAction(final ActionItemRegistry items) {
        if (volumeDescription.contains(";")) {
            // numbers describing volume
            final String[] split = volumeDescription.split(";");
            if (split.length == 3) {
                volume = new BoundingSphere(
                        Float.valueOf(split[2]),
                        new Vector3f(Float.valueOf(split[0]), DEFAULT_PLAYER_HEIGHT, Float.valueOf(split[1])));
            } else if (split.length == 4) {
                volume = new BoundingBox(
                        new Vector3f(Float.valueOf(split[0]), 0, Float.valueOf(split[1])),
                        new Vector3f(Float.valueOf(split[2]), DEFAULT_PLAYER_HEIGHT * 2, Float.valueOf(split[3])));
            } else {
                throw new IllegalArgumentException("Unsupported bounding volume definition - " + Arrays.toString(split));
            }
        } else {
            // item id
            final List<ActionItem> itemList = items.get(volumeDescription);
            if (itemList != null && !itemList.isEmpty()) {
                for (ActionItem it : itemList) {
                    if (it != null && (it instanceof Item)) {
                        Item i = (Item) it;
                        if (volume == null) {
                            volume = i.getModel().getWorldBound();
                        } else {
                            volume.mergeLocal(i.getModel().getWorldBound());
                        }
                    }
                }
            } else {
                System.err.println("No targets for volumeBound " + volumeDescription);
            }
        }
        if (volume == null) {

        }
    }

    public String getTarget() {
        return target;
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
        return "[" + volume + " | " + target + " | " + action + "]";
    }

    @Override
    public void preformActionEnter(String action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preformActionLeave(String action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
