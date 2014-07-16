package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.quest.ConversionQuest;
import eduapp.level.quest.DragQuest;
import eduapp.level.quest.GroupingQuest;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.quest.WebQuestion;

public class QuestActionTrigger extends ActionTrigger<Quest> {
    
    @Override
    public void onActivate() {
        if (action.isEmpty()) {
            target.makeActive();
        } else {
            target.executeAction(action);
        }
    }
    
    public QuestActionTrigger(Spatial volume, Quest target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }
    
    @Override
    public String description() {
        return action;
    }
}
