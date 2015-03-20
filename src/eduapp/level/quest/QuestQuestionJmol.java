package eduapp.level.quest;

import static eduapp.level.quest.QuestItem.MAX_ERROR_COUNT;
import java.util.*;

/**
 *
 * @author Petr Ječmen
 */
public class QuestQuestionJmol extends QuestItem {

    private final String question;
    private final List<String> modelNames, answers;

    public QuestQuestionJmol(String question, String[] models, String[] answers, String reward) {
        super(reward);

        this.question = question;
        this.modelNames = Arrays.asList(models);
        this.answers = Arrays.asList(answers);
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getModelNames() {
        return modelNames;
    }
    
    public List<String> getAnswers() {
        return answers;
    }

    public void setResult(int correct) {
        if (modelNames.size() - correct <= MAX_ERROR_COUNT) {
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
