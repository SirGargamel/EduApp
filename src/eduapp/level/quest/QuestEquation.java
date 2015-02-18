package eduapp.level.quest;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class QuestEquation extends QuestItem {

    private final List<Equation> equations;
    private final List<String> extra;
    private final Mode mode;

    public QuestEquation(Mode mode, String reward) {
        super(reward);

        equations = new LinkedList<>();
        extra = new LinkedList<>();
        finished = false;
        this.mode = mode;
    }

    public void addExtra(final String item) {
        extra.add(item);
    }

    public void addEquation(final Equation eq) {
        equations.add(eq);
    }

    public List<Equation> getEquations() {
        return equations;
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

    public Mode getMode() {
        return mode;
    }

    public static enum Mode {

        text,
        ikony;
    }

    public static class Equation {

        private final List<String> in, out, catalysts;

        public Equation() {
            in = new LinkedList<>();
            out = new LinkedList<>();
            catalysts = new LinkedList<>();
        }

        public void addInput(final String item) {
            in.add(item);
        }

        public void addOutput(final String item) {
            out.add(item);
        }

        public void addCatalyst(final String item) {
            catalysts.add(item);
        }

        public List<String> getInputs() {
            return in;
        }

        public List<String> getOutputs() {
            return out;
        }

        public List<String> getCatalysts() {
            return catalysts;
        }
    }
}
