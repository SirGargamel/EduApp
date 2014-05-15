package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class Level {

    public static final String LEVEL_FILE_EXTENSION = "xml";
    public static final float LEVEL_HEIGHT = 2.5f;
    public static final int TILE_SIZE = 1;
    private static final float RADIUS_INTERACTION = 0.25f;
    private final Node rootNode;
    private final Background background;
    private final Player player;
    private final Set<Item> items;
    private final Set<Light> lights;
    private final Set<Spatial> itemModels;
    private final Set<ActionTrigger> actionTriggers;
    private final Set<ActionTrigger> activeTriggers;
    private final ActionItemRegistry itemRegistry;

    public static Level loadLevel(final String levelName, final AssetManager assetManager) {
        final String path = "levels/".concat(levelName).concat(".").concat(LEVEL_FILE_EXTENSION);
        final Level result = (Level) assetManager.loadAsset(path);
        result.generateLevel(assetManager);
        return result;
    }

    public Level(final Background background, final Player player, final Set<Item> items, final Set<Light> lights, final Set<ActionTrigger> actionItems) {
        this.background = background;
        this.player = player;
        this.items = items;
        this.lights = lights;
        this.actionTriggers = actionItems;

        itemModels = new HashSet<>();
        rootNode = new Node(this.getClass().getSimpleName());
        itemRegistry = new ActionItemRegistry();
        activeTriggers = new HashSet<>();
    }

    private void generateLevel(final AssetManager assetManager) {
        background.generateBackground(assetManager);
        rootNode.attachChild(background.getRootNode());

        Spatial s;
        for (Item i : items) {
            s = i.generateItem(assetManager);
            itemModels.add(s);
            rootNode.attachChild(s);
            itemRegistry.put(i.getId(), i);
        }
        // create lights
        for (Light l : lights) {
            rootNode.addLight(l.getLight());
            itemRegistry.put(l.getId(), l);
        }
        // generate action items
        for (ActionTrigger ai : actionTriggers) {
            ai.generateAction(itemRegistry);
            itemRegistry.put(ai.getId(), ai);
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

    public void addActionItem(final ActionTrigger item) {
        actionTriggers.add(item);
    }

    public Set<Spatial> getItems() {
        return itemModels;
    }

    public void attachChild(final Spatial item) {
        rootNode.attachChild(item);
    }

    public void visit(final Vector3f pos) {
        Iterator<ActionTrigger> it = actionTriggers.iterator();
        ActionTrigger trigger;
        List<ActionItem> list;
        while (it.hasNext()) {
            trigger = it.next();
            if (trigger.getVolume().distanceToEdge(pos) <= RADIUS_INTERACTION) {
                activeTriggers.add(trigger);

                list = itemRegistry.get(trigger.getTarget());
                if (list != null) {
                    for (ActionItem item : list) {
                        if (item != null) {
                            item.preformActionEnter(trigger.getAction());
                        } else {
                            System.err.println("Illegal target for trigger " + trigger.toString());
                        }
                        System.out.println("Action triggered on enter " + trigger + " at " + pos);
                    }

                    it.remove();
                }
            }
        }

        it = activeTriggers.iterator();
        while (it.hasNext()) {
            trigger = it.next();
            if (trigger.getVolume().distanceToEdge(pos) > RADIUS_INTERACTION) {
                list = itemRegistry.get(trigger.getTarget());
                if (list != null) {
                    for (ActionItem item : list) {
                        if (item != null) {
                            item.preformActionLeave(trigger.getAction());
                        } else {
                            System.err.println("Illegal target for trigger " + trigger.toString());
                        }
                        System.out.println("Action triggered on exit " + trigger + " at " + pos);
                    }

                    it.remove();
                    if (!trigger.isOnce()) {
                        actionTriggers.add(trigger);
                    }
                    
                    for (Light l : lights) {
                        rootNode.removeLight(l.getLight());
                        rootNode.addLight(l.getLight());                        
                    }
                }
            }
        }
    }

}
