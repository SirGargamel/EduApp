package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class Level {

    public static final String LEVEL_FILE_EXTENSION = "xml";
    public static final float LEVEL_HEIGHT = 2.5f;
    public static final int TILE_SIZE = 1;
    private static final float RADIUS_INTERACTION = 1.5f;
    private final Node rootNode;
    private final Background background;
    private final Player player;
    private final Set<Item> items;
    private final Set<Light> lights;
    private final Set<Spatial> itemModels;
    private final Set<ActionItem> actionItems;

    public static Level loadLevel(final String levelName, final AssetManager assetManager) {
        final String path = "levels/".concat(levelName).concat(".").concat(LEVEL_FILE_EXTENSION);
        final Level result = (Level) assetManager.loadAsset(path);
        result.generateLevel(assetManager);
        return result;
    }

    Level(final Background background, final Player player, final Set<Item> items, final Set<Light> lights, final Set<ActionItem> actionItems) {
        this.background = background;
        this.player = player;
        this.items = items;
        this.lights = lights;
        this.actionItems = actionItems;

        itemModels = new HashSet<>();
        rootNode = new Node(this.getClass().getSimpleName());
    }

    private void generateLevel(final AssetManager assetManager) {
        background.generateBackground(assetManager);
        rootNode.attachChild(background.getRootNode());

        Spatial s;
        for (Item i : items) {
            s = i.generateItem(assetManager);
            itemModels.add(s);
            rootNode.attachChild(s);
        }
        for (Light l : lights) {
            rootNode.addLight(l.generateLight());
        }
        // generate action items
        for (ActionItem ai : actionItems) {
            ai.generateAction(itemModels);
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getWorldNode() {
        return background.getRootNode();
    }

    public Vector3f getInitialPlayerPos() {
        return player.initialPosition;
    }

    public void addItem(final Item item) {
        items.add(item);
    }

    public void addActionItem(final ActionItem item) {
        actionItems.add(item);
    }

    public Set<Spatial> getItems() {
        return itemModels;
    }

    public void attachChild(final Spatial item) {
        rootNode.attachChild(item);
    }

    public void visit(final Vector3f pos) {
        final Iterator<ActionItem> it = actionItems.iterator();
        ActionItem ai;
        while (it.hasNext()) {
            ai = it.next();
            if (ai.getVolume().distanceTo(pos) < RADIUS_INTERACTION) {
                System.out.println("Close to " + ai.toString());
                if (ai.isOnce()) {
                    it.remove();
                }
            }
        }
    }

}
