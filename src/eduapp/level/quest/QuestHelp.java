package eduapp.level.quest;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class QuestHelp extends QuestItem implements Observer {

    final List<QuestQuestion> questions;
    QuestQuestion lastQuestion;

    public QuestHelp(List<QuestQuestion> questions) {
        super(null);

        this.questions = questions;
        for (QuestQuestion q : questions) {
            q.addObserver(this);
        }

        finished = false;
        lastQuestion = null;
    }

    public QuestQuestion getNextQuestion() {
        for (QuestQuestion q : questions) {
            if (!q.isFinished()) {
                lastQuestion = q;
                break;
            }
        }
        return lastQuestion;
    }

    public QuestQuestion getLastQuestion() {
        return lastQuestion;
    }

    @Override
    public String toNiftyString() {
        return "Bonusové úkoly";
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
