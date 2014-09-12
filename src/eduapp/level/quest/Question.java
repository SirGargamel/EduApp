package eduapp.level.quest;

public class Question extends QuestItem {

    private final String question, answer;
    private final boolean mustBeCorrect, mustMatchCase;
    private String userInput;

    public Question(String question, String answer, String reward, boolean mustBeCorrect, boolean mustMatchCase) {
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
        final StringBuilder sb = new StringBuilder();
        sb.append(question);
        if (isFinished()) {
            sb.append(" - ");
            sb.append(userInput);
        }
        return sb.toString();
    }

    @Override
    public String getTask() {
        return question;
    }
}
