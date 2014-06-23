package eduapp.level.quest;

public class Question extends QuestItem {

    private final String question, answer;
    private String userInput;
    private boolean finished;

    public Question(String question, String answer, String reward) {
        super(reward);

        this.question = question;
        this.answer = answer;
        userInput = "";
    }

    public String getQuestion() {
        return question;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput.trim();
        if (answer.equalsIgnoreCase(userInput)) {
            finished = true;
        }
        finish();
    }

    @Override
    public boolean isFinished() {
        return finished;
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
