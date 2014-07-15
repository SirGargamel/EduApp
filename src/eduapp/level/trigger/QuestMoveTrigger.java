package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.quest.Quest;

public class QuestMoveTrigger extends MoveTrigger<Quest> {

    @Override
    public void onEnter() {
        if (action.isEmpty()) {
            target.makeActive();
        } else {
            System.err.println("Unsupported action  - " + action);
        }
    }

    @Override
    public void onLeave() {
    }

    public QuestMoveTrigger(Spatial volume, Quest target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }
}
