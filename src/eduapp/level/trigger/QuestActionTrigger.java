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

    private static final String ACTION_DISPLAY = "Display";
    private static final String ACTION_DISPLAY_DRAG = "D";
    private static final String ACTION_DISPLAY_CONVERSION = "C";
    private static final String ACTION_DISPLAY_GROUP = "G";
    private static final String ACTION_DISPLAY_JMOL = "J";
    private static final String ACTION_DISPLAY_QUESTION = "Q";
    private static final String ACTION_DISPLAY_WEB = "W";

    @Override
    public void onActivate() {
        if (action.isEmpty()) {
            target.makeActive();
        } else if (action.startsWith(ACTION_DISPLAY)) {
            final String rest = action.replace(ACTION_DISPLAY, "");
            if (rest.startsWith(ACTION_DISPLAY_QUESTION)) {
                final String number = rest.replace(ACTION_DISPLAY_QUESTION, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof Question) {
                        counter++;
                        if (counter == num) {
                            target.displayQuestion((Question) qi);
                        }
                    }
                }
            } else if (rest.startsWith(ACTION_DISPLAY_JMOL)) {
                final String number = rest.replace(ACTION_DISPLAY_JMOL, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof JmolQuestion) {
                        counter++;
                        if (counter == num) {
                            target.displayJmolQuestion((JmolQuestion) qi);
                        }
                    }
                }
            } else if (rest.startsWith(ACTION_DISPLAY_WEB)) {
                final String number = rest.replace(ACTION_DISPLAY_WEB, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof WebQuestion) {
                        counter++;
                        if (counter == num) {
                            target.displayWebQuestion((WebQuestion) qi);
                        }
                    }
                }
            } else if (rest.startsWith(ACTION_DISPLAY_GROUP)) {
                final String number = rest.replace(ACTION_DISPLAY_GROUP, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof GroupingQuest) {
                        counter++;
                        if (counter == num) {
                            target.displayGroups((GroupingQuest) qi);
                        }
                    }
                }
            } else if (rest.startsWith(ACTION_DISPLAY_CONVERSION)) {
                final String number = rest.replace(ACTION_DISPLAY_CONVERSION, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof ConversionQuest) {
                        counter++;
                        if (counter == num) {
                            target.displayConversion((ConversionQuest) qi);
                        }
                    }
                }
            }  else if (rest.startsWith(ACTION_DISPLAY_DRAG)) {
                final String number = rest.replace(ACTION_DISPLAY_DRAG, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof DragQuest) {
                        counter++;
                        if (counter == num) {
                            target.displayDrag((DragQuest) qi);
                        }
                    }
                }
            }
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
