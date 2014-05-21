package eduapp.level.quest;

import eduapp.JmolUtils;
import eduapp.gui.GuiManager;
import eduapp.level.Item;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class Quest extends Item {

    private final List<QuestItem> data;

    public Quest(List<QuestItem> data) {
        this.data = data;
    }
    
    public void makeActive() {
        GuiManager.displayQuest(this);
    }
    
    public void displayQuestion(Question question) {
        GuiManager.displayQuestion(question);
    }
    
    public void displayJmolQuestion(JmolQuestion question) {
        // TODO
        JmolUtils.launchJmol(question.getModelName());
        GuiManager.displayQuestion(question);
    }

    public List<QuestItem> getData() {
        return data;
    }
    
}
