package eduapp.level.quest;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class HelpQuest extends QuestItem implements Observer {

    final List<Question> questions;
    Question lastQuestion;

    public HelpQuest(List<Question> questions) {
        super(null);

        this.questions = questions;
        for (Question q : questions) {
            q.addObserver(this);
        }

        finished = false;
        lastQuestion = null;
    }

    public Question getNextQuestion() {
        for (Question q : questions) {
            if (!q.isFinished()) {
                lastQuestion = q;
                break;
            }
        }
        return lastQuestion;
    }

    public Question getLastQuestion() {
        return lastQuestion;
    }

    @Override
    public String toNiftyString() {
        return "Bonusové úkoly";
    }

    @Override
    public String getTask() {
        return "Toto by nemelo byt videt...";
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
