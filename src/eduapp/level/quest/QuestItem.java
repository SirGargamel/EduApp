package eduapp.level.quest;

import eduapp.level.Item;
import eduapp.level.Level;

/**
 *
 * @author Petr Ječmen
 */
public abstract class QuestItem extends Item {

    protected Level level;

    public abstract boolean isFinished();

    public abstract String toNiftyString();

    public abstract String getTask();

    public void setLevel(final Level level) {
        this.level = level;
    }

    public void finish() {
        setChanged();
        notifyObservers();
    }
}
