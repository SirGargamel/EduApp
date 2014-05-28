package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.quest.WebQuestion;

public class QuestActionTrigger extends ActionTrigger<Quest> {

    private static final String ACTION_DISPLAY = "Display";
    private static final String ACTION_DISPLAY_QUESTION = "Q";
    private static final String ACTION_DISPLAY_JMOL = "J";
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
            }
        }
    }

    public QuestActionTrigger(Spatial volume, Quest target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }
}
