package eduapp.level.quest;

/**
 *
 * @author Petr Ječmen
 */
public class JmolQuestion extends QuestItem {

    private final String[] modelNames;

    public JmolQuestion(String[] modelNames, String reward) {
        super(reward);

        this.modelNames = modelNames;
    }

    public String[] getModelNames() {
        return modelNames;
    }

    @Override
    public String toNiftyString() {
        return "Dle modelu napište vzorec molekuly.";
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }
}
