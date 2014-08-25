package eduapp.level.quest;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class EquationQuest extends QuestItem {

    private final List<String> in, out;
    private final List<String> extra;
    private final Mode mode;

    public EquationQuest(Mode mode, String reward) {
        super(reward);

        in = new LinkedList<>();
        out = new LinkedList<>();
        extra = new LinkedList<>();
        finished = false;
        this.mode = mode;
    }

    public void addInput(final String item) {
        in.add(item);
    }

    public void addOutput(final String item) {
        out.add(item);
    }

    public void addExtra(final String item) {
        extra.add(item);
    }

    public List<String> getInputs() {
        return in;
    }

    public List<String> getOutputs() {
        return out;
    }

    public List<String> getExtra() {
        return extra;
    }

    public void setResult(boolean finished) {
        this.finished = finished;
        if (!finished) {
            failed = true;
        }
        finish();
    }

    @Override
    public String toNiftyString() {
        return "Seřaďte předměty tak, aby rovnice platila.";
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }

    public Mode getMode() {
        return mode;
    }

    public static enum Mode {

        text,
        ikony;
    }
}
