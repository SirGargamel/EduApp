package eduapp.level.quest;

import static eduapp.level.quest.QuestItem.MAX_ERROR_COUNT;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestPicking extends QuestItem {

    private static final String DELIMITER = ",";
    private final Map<String, List<String>> data;
    private final String question;

    public QuestPicking(String question, String[] data, String reward) {
        super(reward);

        this.question = question;

        this.data = new LinkedHashMap<>(data.length);
        String[] split;
        List<String> list;
        for (String s : data) {
            split = s.split(DELIMITER);
            list = new ArrayList<>(split.length - 1);
            for (int i = 1; i < split.length; i++) {
                list.add(split[i]);
            }
            this.data.put(split[0], list);
        }
    }

    public Map<String, List<String>> getData() {
        return data;
    }

    public String getQuestion() {
        return question;
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

    @Override
    public String getTask() {
        return toNiftyString();
    }
}
