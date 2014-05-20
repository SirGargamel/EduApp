package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;
import eduapp.level.Quest;

public class QuestActionTrigger extends ActionTrigger<Quest> {

    @Override
    public void onActivate() {
        target.makeActive();
    }

    public QuestActionTrigger(BoundingVolume volume, Quest target, String action, boolean once) {
        super(volume, target, action, once);
    }
}
