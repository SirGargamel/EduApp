package eduapp.level.quest;

import eduapp.FlowManager;

/**
 *
 * @author Petr Ječmen
 */
public class FinalQuest extends EquationQuest {  
    
    private int itemCount;

    public FinalQuest() {
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

    @Override
    public String getTask() {
        return toNiftyString();
    }    

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }
}
