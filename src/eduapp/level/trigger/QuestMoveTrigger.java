package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.quest.Quest;

public class QuestMoveTrigger extends MoveTrigger<Quest> {

    @Override
    public void onEnter() {
        if (action.isEmpty()) {
            target.makeActive();
        } else {
            target.executeAction(action);
        }
    }

    @Override
    public void onLeave() {
        target.executeAction(action);
    }

    public QuestMoveTrigger(Spatial volume, Quest target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }
}
