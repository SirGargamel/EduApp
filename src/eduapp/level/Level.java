package eduapp.level;

import eduapp.level.quest.Quest;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eduapp.gui.GuiManager;
import eduapp.level.trigger.ActionTrigger;
import eduapp.level.trigger.MoveTrigger;
import eduapp.level.trigger.Trigger;
import eduapp.level.trigger.TriggerStub;
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
    private static final float RADIUS_INTERACTION = 0.25f;
    private final Node rootNode;
    private final Background background;
    private final Player player;
    private final Set<Model> items;
    private final Set<Light> lights;
    private final Set<Quest> quests;
    private final Set<Spatial> itemModels;
    private final Set<TriggerStub> stubs;
    private final Set<Trigger> triggers, activeTriggers;
    private final ItemRegistry itemRegistry;

    public static Level loadLevel(final String levelName, final AssetManager assetManager) {
        final String path = "levels/".concat(levelName).concat(".").concat(LEVEL_FILE_EXTENSION);
        final AssetKey<Level> key = new AssetKey<>(path);
        final Level result = assetManager.loadAsset(key);
        result.generateLevel(assetManager);
        ((DesktopAssetManager) assetManager).clearCache();
        return result;
    }

    public Level(final Background background, final Player player, final Set<Model> items, final Set<Light> lights, final Set<TriggerStub> triggerStubs, final Set<Quest> quests) {
        this.background = background;
        this.player = player;
        this.items = items;
        this.lights = lights;
        this.stubs = triggerStubs;
        this.quests = quests;

        itemModels = new HashSet<>();
        rootNode = new Node(String.valueOf(Math.random()));
        itemRegistry = new ItemRegistry();
        triggers = new HashSet<>();
        activeTriggers = new HashSet<>();
    }

    private void generateLevel(final AssetManager assetManager) {
        background.generateBackground(assetManager);
        rootNode.attachChild(background.getRootNode());

        itemRegistry.put(player);

        for (Quest q : quests) {
            itemRegistry.put(q);
            q.assignInterfaces(this);
        }

        Spatial s;
        for (Model i : items) {
            s = i.generateItem(assetManager);
            itemModels.add(s);
            rootNode.attachChild(s);
            itemRegistry.put(i);
        }
        // create lights
        for (Light l : lights) {
            l.actualizePos(itemRegistry);
            rootNode.addLight(l.getLight());
            itemRegistry.put(l);            
        }
        // generate action items
        Trigger t;
        ActionTrigger at;
        for (TriggerStub ts : stubs) {
            t = ts.generateTrigger(itemRegistry, assetManager);
            triggers.add(t);
            rootNode.attachChild(t.getGeometry());
            itemRegistry.put(t);
            if (t instanceof ActionTrigger) {
                at = (ActionTrigger) t;
                rootNode.addLight(at.getLight());
            }
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getWorldNode() {
        return background.getRootNode();
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Spatial> getItems() {
        return itemModels;
    }

    public void attachChild(final Spatial item) {
        rootNode.attachChild(item);
    }

    public void visit(final Vector3f pos) {
        Iterator<Trigger> it;
        Trigger trigger;
        MoveTrigger mt;

        it = activeTriggers.iterator();
        while (it.hasNext()) {
            trigger = it.next();
            if (!trigger.isActive()) {
                it.remove();
            } else if (trigger.getVolume().distanceToEdge(pos) > RADIUS_INTERACTION) {
                it.remove();
                if (!trigger.isOnce()) {
                    triggers.add(trigger);
                }

                if (trigger instanceof MoveTrigger) {
                    if (trigger instanceof MoveTrigger) {
                        mt = (MoveTrigger) trigger;
                        mt.onLeave();
                        System.out.println("Action triggered on exit " + mt + " at " + pos);
                    }
                }
            }
        }

        it = triggers.iterator();
        while (it.hasNext()) {
            trigger = it.next();
            if (trigger.isActive() && trigger.getVolume().distanceToEdge(pos) <= RADIUS_INTERACTION) {
                it.remove();
                activeTriggers.add(trigger);

                if (trigger instanceof MoveTrigger) {
                    mt = (MoveTrigger) trigger;
                    mt.onEnter();
                    System.out.println("Action triggered on enter " + mt + " at " + pos);
                }
            }
        }

        // check for action triggers
        boolean show = false;
        for (Trigger t : activeTriggers) {
            if (t instanceof ActionTrigger) {
                // show icon
                show = true;
                break;
            }
        }
        GuiManager.showTriggerMarker(show);
    }

    public void activate(final Vector3f pos) {
        final Iterator<Trigger> it = activeTriggers.iterator();
        Trigger trg;
        ActionTrigger at;
        while (it.hasNext()) {
            trg = it.next();
            if (trg instanceof ActionTrigger) {
                at = (ActionTrigger) trg;
                at.onActivate();
                System.out.println("Action triggered on activation " + at + " at " + pos);
            }
        }
    }

    public void destroy() {
        for (Light l : lights) {
            l.getLight().setColor(ColorRGBA.BlackNoAlpha);
        }

        rootNode.detachAllChildren();
        rootNode.removeFromParent();
    }

    public void deactivateTrigger(final String triggerId) {
        if (triggerId != null && !triggerId.isEmpty()) {
            for (Trigger t : triggers) {
                if (triggerId.equals(t.getId())) {
                    t.setActive(false);
                }
            }
            Iterator<Trigger> it = activeTriggers.iterator();
            Trigger t;
            while (it.hasNext()) {
                t = it.next();
                if (triggerId.equals(t.getId())) {
                    t.setActive(false);
                    it.remove();
                }
            }
        }
    }
}
