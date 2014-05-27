package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Task;

public class QuestMoveTrigger extends MoveTrigger<Quest> {

    private static final String ACTION_FINISH = "Finish";
    private static final String ACTION_FINISH_TASK = "T";

    @Override
    public void onEnter() {
        if (action.isEmpty()) {
            target.makeActive();
        } else if (action.startsWith(ACTION_FINISH)) {
            final String rest = action.replace(ACTION_FINISH, "");
            if (rest.startsWith(ACTION_FINISH_TASK)) {
                final String number = rest.replace(ACTION_FINISH_TASK, "");
                final int num = Integer.valueOf(number);
                int counter = 0;
                for (QuestItem qi : target.getData()) {
                    if (qi instanceof Task) {
                        counter++;
                        if (counter == num) {
                            final Task t = (Task) qi;
                            t.setDone(true);
                        }
                    }
                }
            } else {
                System.err.println("Unsupported action for QustMoveTrigger - " + action);
            }
        }
    }

    @Override
    public void onLeave() {
    }

    public QuestMoveTrigger(Spatial volume, Quest target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }
}
