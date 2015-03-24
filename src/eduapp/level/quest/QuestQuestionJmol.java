package eduapp.level.quest;

import static eduapp.level.quest.QuestItem.MAX_ERROR_COUNT;
import java.util.*;

/**
 *
 * @author Petr Ječmen
 */
public class QuestQuestionJmol extends QuestItem {

    private final String question;    
    private final Map<String, String> data;

    public QuestQuestionJmol(String question, String[] models, String[] answers, String reward) {
        super(reward);

        this.question = question;
        data = new HashMap<>(models.length);
        for (int i = 0; i < models.length; i++) {
            data.put(models[i], answers[i]);
        }
    }

    public String getQuestion() {
        return question;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setResult(int correct) {
        if (data.size() - correct <= MAX_ERROR_COUNT) {
            finished = true;
        } else {
            failed = true;
        }
        finish();
    }

    @Override
    public String toNiftyString() {
        return question;
    }
}
