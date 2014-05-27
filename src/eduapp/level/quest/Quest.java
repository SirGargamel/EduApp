package eduapp.level.quest;

import eduapp.JmolUtils;
import eduapp.gui.GuiManager;
import eduapp.level.Item;
import eduapp.level.Level;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class Quest extends Item {

    private final List<QuestItem> data;
    private String name;

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

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return getId();
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void assignInterfaces(final Level level) {
        for (QuestItem qi : data) {            
            qi.setLevel(level);
            qi.setItemRegistry(itemRegistry);
        }
    }
}
