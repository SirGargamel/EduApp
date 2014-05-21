package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;

public class QuestActionTrigger extends ActionTrigger<Quest> {

    private static final String ACTION_DISPLAY = "Display";
    private static final String ACTION_DISPLAY_QUESTION = "Q";
    private static final String ACTION_DISPLAY_JMOL = "J";

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
            }
        }
    }

    public QuestActionTrigger(BoundingVolume volume, Quest target, String action, boolean once) {
        super(volume, target, action, once);
    }
}
