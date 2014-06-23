package eduapp.level.quest;

/**
 *
 * @author Petr Ječmen
 */
public class JmolQuestion extends Question {

    private final String modelName;

    public JmolQuestion(String question, String answer, String modelName, String reward) {
        super(question, answer, reward);

        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }
}
