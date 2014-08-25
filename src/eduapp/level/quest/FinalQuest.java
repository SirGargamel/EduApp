package eduapp.level.quest;

/**
 *
 * @author Petr Ječmen
 */
public class FinalQuest extends EquationQuest {          

    public FinalQuest() {
        super(Mode.ikony, null);           
        finished = false;        
    }

    @Override
    public String toNiftyString() {
        return "Seřaďte nasbírané předměty tak, aby rovnice platila.";
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }    
}
