package eduapp.level.quest;

import eduapp.level.item.Item;
import eduapp.level.Level;

/**
 *
 * @author Petr Ječmen
 */
public abstract class QuestItem extends Item {

    protected Level level;
    private String reward;

    public QuestItem(String reward) {
        this.reward = reward;
    }

    public abstract boolean isFinished();

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
