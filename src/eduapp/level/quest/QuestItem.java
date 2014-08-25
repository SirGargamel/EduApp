package eduapp.level.quest;

import eduapp.level.item.Item;
import eduapp.level.Level;

/**
 *
 * @author Petr Ječmen
 */
public abstract class QuestItem extends Item {

    protected Level level;
    private final String reward;
    protected boolean failed, finished;

    public QuestItem(String reward) {
        this.reward = reward;
        failed = false;
        finished = false;
    }

    public boolean isFinished() {
        return finished;
    }
    

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public abstract String toNiftyString();

    public abstract String getTask();

    public void setLevel(final Level level) {
        this.level = level;
    }

    public String getReward() {
        return reward;
    }

    public void finish() {
        setChanged();
        notifyObservers();
    }
}
