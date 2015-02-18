package eduapp.level.quest;

import eduapp.FlowManager;

/**
 *
 * @author Petr Ječmen
 */
public class QuestFinal extends QuestEquation {

    private int itemCount;

    public QuestFinal() {
        super(Mode.text, null);
        finished = false;
    }

    @Override
    public void setResult(boolean finished) {
        if (finished) {
            setFinished(true);
            FlowManager.getInstance().displayQuestFinish();
        }
    }

    @Override
    public String toNiftyString() {
        return "Seřaďte nasbírané předměty tak, aby rovnice platila.";
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }
}
