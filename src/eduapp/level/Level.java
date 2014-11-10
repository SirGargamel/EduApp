package eduapp.level;

import eduapp.level.quest.Quest;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eduapp.AppContext;
import eduapp.EduApp;
import eduapp.FlowManager;
import eduapp.ItemRegistry;
import eduapp.level.item.Item;
import eduapp.level.quest.QuestItem;
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

    public static final float LEVEL_HEIGHT = 2.5f;
    public static final int TILE_SIZE = 1;
    private static final float RADIUS_INTERACTION = 0.75f;
    private final Node rootNode;
    private final Background background;
    private final Player player;
    private final Set<Model> items;
    private final Set<Light> lights;
    private final Quest quest;
    private final Set<TriggerStub> stubs;
    private final Set<Trigger> triggers, activeTriggers;
    private final Set<Item> dictionary;

    public static Level loadLevel(final String levelName, final AssetManager assetManager, final InputManager inputManager) {
        final AssetKey<Level> key = new AssetKey<>("level.xml");
        final Level result = assetManager.loadAsset(key);
        result.generateLevel(assetManager, inputManager);
        ((DesktopAssetManager) assetManager).clearCache();
        return result;
    }

    public Level(final Background background, final Player player, final Set<Model> items, final Set<Light> lights, final Set<TriggerStub> triggerStubs, final Quest quest, final Set<Item> dictionary) {
        this.background = background;
        this.player = player;
        this.items = items;
        this.lights = lights;
        this.stubs = triggerStubs;
        this.quest = quest;
        this.dictionary = dictionary;

        rootNode = new Node(String.valueOf(Math.random()));
        triggers = new HashSet<>();
        activeTriggers = new HashSet<>();
    }

    private void generateLevel(final AssetManager assetManager, final InputManager inputManager) {
        FlowManager flowManager = FlowManager.getInstance();

        background.generateBackground(assetManager);
        rootNode.attachChild(background.getRootNode());

        final ItemRegistry ir = AppContext.getItemRegistry();

        player.generatePlayer(assetManager, inputManager);
        player.addObserver(flowManager);
        ir.put(player);


        ir.put(quest);
        quest.assignInterfaces(this);
        quest.addObserver(flowManager);
        for (QuestItem qi : quest.getData()) {
            qi.addObserver(flowManager);
        }
        quest.getHelp().addObserver(flowManager);


        Spatial s;
        for (Model i : items) {
            s = i.generateItem(assetManager);
            rootNode.attachChild(s);
            ir.put(i);
            i.addObserver(flowManager);
        }
        // create lights
        for (Light l : lights) {
            ir.put(l);
            l.actualizePos();
            rootNode.addLight(l.getLight());
            l.addObserver(flowManager);
        }
        // generate action items
        Trigger t;
        ActionTrigger at;
        for (TriggerStub ts : stubs) {
            t = ts.generateTrigger(AppContext.getItemRegistry(), assetManager);
            triggers.add(t);
            if (EduApp.DEBUG) {
                rootNode.attachChild(t.getGeometry());
            }
            ir.put(t);
            if (t instanceof ActionTrigger) {
                at = (ActionTrigger) t;
                rootNode.addLight(at.getLight());
            }
            t.addObserver(flowManager);
        }
        // load dictionary
        for (Item it : dictionary) {
            it.addObserver(flowManager);
            ir.put(it);
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getBackground() {
        return background.getRootNode();
    }

    public Player getPlayer() {
        return player;
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
                        if (EduApp.DEBUG) {
                            System.out.println("Action triggered on exit " + mt + " at " + pos);
                        }
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
                    if (EduApp.DEBUG) {
                        System.out.println("Action triggered on enter " + mt + " at " + pos);
                    }
                }
            }
        }

        // check for action triggers
        String msg = null;
        if (FlowManager.getInstance().getCurrentQuest() != null) {
            for (Trigger t : activeTriggers) {
                if (t instanceof ActionTrigger) {
                    // show icon
                    msg = FlowManager.getInstance().getCurrentQuest().findQuestItem(((ActionTrigger) t).description()).toNiftyString();
                    break;
                }
            }
        }

        FlowManager.getInstance().displayDescription(msg);
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
                if (EduApp.DEBUG) {
                    System.out.println("Action triggered on activation " + at + " at " + pos);
                }
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
