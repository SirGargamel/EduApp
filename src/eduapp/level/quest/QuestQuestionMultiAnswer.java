package eduapp.level.quest;

public class QuestQuestionMultiAnswer extends QuestItem {

    private final String question;
    private final String[] answers, correctAnswers;    

    public QuestQuestionMultiAnswer(String question, String[] correctAnswers, String[] answers, String reward) {
        super(reward);

        this.question = question;
        this.correctAnswers = correctAnswers;
        this.answers = answers;
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

    public void setResult(boolean result) {
        finished = result;
        failed = !result;

        finish();
    }

    @Override
    public String toNiftyString() {
        return question;
    }
}
