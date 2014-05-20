package eduapp.level.trigger;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import eduapp.level.Item;
import eduapp.level.ItemRegistry;
import eduapp.level.Light;
import eduapp.level.Model;
import eduapp.level.quest.Quest;
import java.util.Arrays;

/**
 *
 * @author Petr Ječmen
 */
public class TriggerStub {

    private static final String NODE_MOVE = "MoveTrigger";
    private static final String NODE_ACTION = "ActionTrigger";
    private static final float DEFAULT_HEIGHT = 0.75f;
    private final String nodeName;
    private final String volumeDescription;
    private final String targetDescription;
    private final String action;
    private final boolean once;

    public TriggerStub(String nodeName, String volumeDescription, String targetDescription, String action, boolean once) {
        this.volumeDescription = volumeDescription;
        this.targetDescription = targetDescription;
        this.action = action;
        this.once = once;
        this.nodeName = nodeName;
    }

    public Trigger generateTrigger(final ItemRegistry registry) {
        final Trigger result;
        final BoundingVolume volume = generateVolume(registry);
        final Object target = registry.get(targetDescription);
        switch (nodeName) {
            case NODE_ACTION:
                if (target instanceof Light) {
                    result = new LightActionTrigger(volume, (Light) target, action, once);
                } else if (target instanceof Quest) {
                    result = new QuestActionTrigger(volume, (Quest) target, action, once);
                } else {
                    throw new IllegalArgumentException("Unsupported trigger target - " + target);
                }
                break;
            case NODE_MOVE:
                if (target instanceof Light) {
                    result = new LightMoveTrigger(volume, (Light) target, action, once);
                } else if (target instanceof Quest) { 
                    result = new QuestMoveTrigger(volume, (Quest) target, action, once);
                } else {
                    throw new IllegalArgumentException("Unsupported trigger target - " + target);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported trigger type - " + nodeName);
        }

        return result;
    }

    private BoundingVolume generateVolume(final ItemRegistry items) {
        final BoundingVolume result;
        if (volumeDescription.contains(";")) {
            // numbers describing volume
            final String[] split = volumeDescription.split(";");
            if (split.length == 3) {
                result = new BoundingSphere(
                        Float.valueOf(split[2]),
                        new Vector3f(Float.valueOf(split[0]), DEFAULT_HEIGHT, Float.valueOf(split[1])));
            } else if (split.length == 4) {
                result = new BoundingBox(
                        new Vector3f(Float.valueOf(split[0]), 0, Float.valueOf(split[1])),
                        new Vector3f(Float.valueOf(split[2]), DEFAULT_HEIGHT * 2, Float.valueOf(split[3])));
            } else {
                throw new IllegalArgumentException("Unsupported bounding volume definition - " + Arrays.toString(split));
            }
        } else {
            // item id
            final Item item = items.get(volumeDescription);
            if (item != null && item instanceof Model) {
                Model i = (Model) item;
                result = i.getModel().getWorldBound();
            } else {
                System.err.println("No targets for volumeBound " + volumeDescription);
                result = new BoundingSphere(0, Vector3f.ZERO);
            }
        }
        return result;
    }
}
