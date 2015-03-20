package eduapp.level.quest;

public class QuestQuestionMultiAnswer extends QuestItem {

    private final String question;
    private final String[] wrongAnswers, correctAnswers;    

    public QuestQuestionMultiAnswer(String question, String[] correctAnswers, String[] wrongAnswers, String reward) {
        super(reward);

        this.question = question;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
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
