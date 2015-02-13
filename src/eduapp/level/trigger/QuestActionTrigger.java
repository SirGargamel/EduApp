package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.quest.QuestConversion;
import eduapp.level.quest.QuestEquation;
import eduapp.level.quest.QuestGrouping;
import eduapp.level.quest.QuestQuestionJmol;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.QuestQuestion;
import eduapp.level.quest.QuestQuestionWeb;

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
