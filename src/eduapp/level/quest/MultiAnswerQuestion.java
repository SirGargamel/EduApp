package eduapp.level.quest;

import java.util.Arrays;

public class MultiAnswerQuestion extends QuestItem {

    private final String question;
    private final String[] answers, correctAnswers;    
    private String[] userInput;

    public MultiAnswerQuestion(String question, String[] correctAnswers, String[] answers, String reward) {
        super(reward);

        this.question = question;
        this.correctAnswers = correctAnswers;
        this.answers = answers;
        userInput = null;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String[] getCorrectAnswers() {
        return correctAnswers;
    }    

    public void setUserInput(String[] input, boolean result) {
        this.userInput = input;        

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
            sb.append(Arrays.toString(userInput));
        }
        return sb.toString();
    }

    @Override
    public String getTask() {
        return question;
    }
}
