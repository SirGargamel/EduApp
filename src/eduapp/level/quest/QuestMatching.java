package eduapp.level.quest;

import static eduapp.level.quest.QuestItem.MAX_ERROR_COUNT;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuestMatching extends QuestItem {

    private final String question;
    private final Map<String, String> data;

    public QuestMatching(final String question, String[] data, String reward) {
        super(reward);

        this.question = question;
        this.data = new LinkedHashMap<>(data.length / 2);
        for (int i = 0; i < data.length; i += 2) {
            this.data.put(data[i], data[i + 1]);
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setResult(int correct) {
        if (data.keySet().size() - correct <= MAX_ERROR_COUNT) {
            finished = true;
        } else {
            failed = true;
        }
        finish();
    }

    @Override
    public String toNiftyString() {
        return question;
    }
}
