package eduapp.level.quest;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class HelpQuest extends QuestItem implements Observer {

    final Set<Question> questions;

    public HelpQuest(Set<Question> questions) {
        super(null);
        
        this.questions = questions;        
        for (Question q : questions) {
            q.addObserver(this);
        }
        
        finished = false;
    }

    public Question getNextQuestion() {
        Question result = null;
        for (Question q : questions) {
            if (!q.isFinished()) {
                result = q;
                break;
            }
        }        
        return result;
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
