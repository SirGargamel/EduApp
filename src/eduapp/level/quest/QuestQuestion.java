package eduapp.level.quest;

public class QuestQuestion extends QuestItem {

    private final String question, answer;
    private final boolean mustBeCorrect, mustMatchCase;
    private String userInput;

    public QuestQuestion(String question, String answer, String reward, boolean mustBeCorrect, boolean mustMatchCase) {
        super(reward);

        this.question = question;
        this.answer = answer;
        this.mustBeCorrect = mustBeCorrect;
        this.mustMatchCase = mustMatchCase;
        userInput = null;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserInput() {
        return userInput;
    }

    public boolean isMustBeCorrect() {
        return mustBeCorrect;
    }

    public boolean isMustMatchCase() {
        return mustMatchCase;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput.trim();
        boolean result;
        if (isMustMatchCase()) {
            result = answer.equals(userInput);
        } else {
            result = answer.equalsIgnoreCase(userInput);
        }
        
        finished = result;
        failed = !result;

        finish();
    }

    @Override
    public String toNiftyString() {
        return question;
    }
}
