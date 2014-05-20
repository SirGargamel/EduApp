package eduapp.level.quest;

import eduapp.level.Item;

public class Question extends Item implements QuestItem {

    private final String question, answer;
    private String userInput;

    public Question(String question, String answer) {
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
    }

    @Override
    public boolean isFinished() {
        return answer.equalsIgnoreCase(userInput);
    }

    @Override
    public String toNiftyString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(question);
        sb.append(" - ");
        if (isFinished()) {
            sb.append(userInput);
        }
        return sb.toString();
    }
}
