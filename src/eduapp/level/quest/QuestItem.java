package eduapp.level.quest;

import eduapp.FlowManager;
import eduapp.level.Item;
import eduapp.level.Level;
import eduapp.level.Light;
import eduapp.level.trigger.Trigger;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Petr Ječmen
 */
public abstract class QuestItem extends Item {

    protected Level level;
    private final Set<String> children;

    public QuestItem() {
        children = new HashSet<>();
    }

    public abstract boolean isFinished();

    public abstract String toNiftyString();

    public abstract String getTask();

    public void setLevel(final Level level) {
        this.level = level;
    }

    public void addChild(final String id) {
        children.add(id);
    }

    public void finish() {
        Item i;
        for (String s : children) {
            i = itemRegistry.get(s);
            if (i instanceof Light) {
                Light l = (Light) i;
                l.enableLight(false);
            } else if (i instanceof Trigger) {
                Trigger t = (Trigger) i;
                t.setActive(false);
            } else {
                System.err.println("Illegal child for deactivation - " + s);
            }
        }        
        FlowManager.finishQuestItem("Úkol ".concat(getTask()).concat(" byl splněn."));
    }
}
