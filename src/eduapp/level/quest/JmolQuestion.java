package eduapp.level.quest;

import java.util.*;

/**
 *
 * @author Petr Ječmen
 */
public class JmolQuestion extends QuestItem {

    private final List<String> modelNames;

    public JmolQuestion(String[] models, String reward) {
        super(reward);

        this.modelNames = Arrays.asList(models);
        Collections.shuffle(modelNames);
    }

    public Collection<String> getModelNames() {
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
