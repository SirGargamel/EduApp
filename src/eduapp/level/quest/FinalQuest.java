package eduapp.level.quest;

import eduapp.FlowManager;

/**
 *
 * @author Petr Ječmen
 */
public class FinalQuest extends EquationQuest {  
    
    private final int itemCount;

    public FinalQuest(final int itemCount) {
        super(Mode.ikony, null);           
        finished = false; 
        this.itemCount = itemCount;
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

    public int getItemCount() {
        return itemCount;
    }
}
