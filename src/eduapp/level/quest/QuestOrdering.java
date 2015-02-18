package eduapp.level.quest;

public class QuestOrdering extends QuestItem {

    private final String question;
    private final String[] data;

    public QuestOrdering(String question, String[] data, String reward) {
        super(reward);

        this.question = question;
        this.data = data;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getData() {
        return data;
    }

    public void setResult(int correct) {
        if (data.length - correct <= MAX_ERROR_COUNT) {
            finished = true;
        } else {
            failed = true;
        }
        finish();
    }

    @Override
    public String toNiftyString() {
        return "Seřaďte kroky tak, aby vytvořili daný technologický postup - " + question;
    }  
}
