package eduapp.level.quest;

import static eduapp.level.quest.QuestItem.MAX_ERROR_COUNT;

public class QuestAdding extends QuestItem {

    private final String[] text, fill;

    public QuestAdding(String[] text, String[] fill, String reward) {
        super(reward);

        this.text = text;
        this.fill = fill;
    }

    public String[] getText() {
        return text;
    }

    public void setResult(int correct) {
        if (fill.length - correct <= MAX_ERROR_COUNT) {
            finished = true;
        } else {
            failed = true;
        }
        finish();
    }

    public String[] getFill() {
        return fill;
    }

    @Override
    public String toNiftyString() {
        return "Dopňte daná slova na správné pozice.";
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }
}
