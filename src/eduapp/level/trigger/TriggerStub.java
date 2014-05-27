package eduapp.level.trigger;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import eduapp.level.Item;
import eduapp.level.ItemRegistry;
import eduapp.level.Light;
import eduapp.level.Model;
import eduapp.level.Player;
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
    private static Material MAT_MOVE, MAT_ACTION;
    private final String nodeName, id;
    private final String volumeDescription;
    private final String targetDescription;
    private final String action;
    private final boolean once, active;

    public TriggerStub(String nodeName, String id, String volumeDescription, String targetDescription, String action, boolean once, boolean active) {
        this.volumeDescription = volumeDescription;
        this.targetDescription = targetDescription;
        this.action = action;
        this.once = once;
        this.nodeName = nodeName;
        this.active = active;
        this.id = id;
    }

    public Trigger generateTrigger(final ItemRegistry registry, final AssetManager assetManager) {
        if (MAT_ACTION == null) {
            generateMaterials(assetManager);
        }
        final Trigger result;
        final Spatial volume = generateVolume(registry);
        final Object target = registry.get(targetDescription);
        switch (nodeName) {
            case NODE_ACTION:
                volume.setMaterial(MAT_ACTION);
                if (target instanceof Light) {
                    result = new LightActionTrigger(volume, (Light) target, action, once, active);
                } else if (target instanceof Quest) {
                    result = new QuestActionTrigger(volume, (Quest) target, action, once, active);
                } else {
                    throw new IllegalArgumentException("Unsupported trigger target - " + target);
                }
                break;
            case NODE_MOVE:
                volume.setMaterial(MAT_MOVE);
                if (target instanceof Light) {
                    result = new LightMoveTrigger(volume, (Light) target, action, once, active);
                } else if (target instanceof Quest) {
                    result = new QuestMoveTrigger(volume, (Quest) target, action, once, active);
                } else {
                    throw new IllegalArgumentException("Unsupported trigger target - " + target);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported trigger type - " + nodeName);
        }

        result.setId(id);
        return result;
    }

    private static void generateMaterials(final AssetManager assetManager) {
        MAT_MOVE = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        MAT_MOVE.getAdditionalRenderState().setWireframe(true);
        MAT_MOVE.setColor("Color", ColorRGBA.Cyan);

        MAT_ACTION = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        MAT_ACTION.getAdditionalRenderState().setWireframe(true);
        MAT_ACTION.setColor("Color", ColorRGBA.Red);
    }

    private Spatial generateVolume(final ItemRegistry items) {
        final Spatial result;
        if (volumeDescription.contains(";")) {
            // numbers describing volume
            final String[] split = volumeDescription.split(";");
            if (split.length == 3) {
                result = new Geometry("wireframe sphere", new WireSphere(Float.valueOf(split[2])));
                result.setLocalTranslation(new Vector3f(Float.valueOf(split[0]), DEFAULT_HEIGHT / 2.0f, Float.valueOf(split[1])));
            } else if (split.length == 4) {
                result = new Geometry(
                        "wireframeCube",
                        new WireBox(Float.valueOf(split[2]), DEFAULT_HEIGHT, Float.valueOf(split[3])));
                result.setLocalTranslation(new Vector3f(Float.valueOf(split[0]), DEFAULT_HEIGHT, Float.valueOf(split[1])));
            } else {
                throw new IllegalArgumentException("Unsupported bounding volume definition - " + Arrays.toString(split));
            }
        } else {
            // item id
            final Item item = items.get(volumeDescription);
            if (item != null) {
                if (item instanceof Model) {
                    Model i = (Model) item;
                    result = i.getModel().clone();
                } else if (item instanceof Player) {
                    Player p = (Player) item;
                    result = new Geometry("wireframe sphere", new WireSphere(0.5f));
                    result.setLocalTranslation(p.getInitialPosition());
                } else {
                    System.err.println("Illegal target for volumeBound " + item);
                result = new Geometry("wireframe sphere", new WireSphere(0));
                }
            } else {
                System.err.println("No target for volumeBound " + volumeDescription);
                result = new Geometry("wireframe sphere", new WireSphere(0));
            }
        }
        return result;
    }
}
