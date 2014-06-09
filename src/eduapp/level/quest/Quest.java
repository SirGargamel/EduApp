package eduapp.level.quest;

import eduapp.FlowManager;
import eduapp.JmolUtils;
import eduapp.level.Item;
import eduapp.level.Level;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

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
        FlowManager.displayQuest(this);
    }

    public void displayQuestion(Question question) {
        FlowManager.displayQuestion(question);
    }

    public void displayJmolQuestion(final JmolQuestion question) {
        FlowManager.displayQuestion(question);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        this.wait(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Quest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
                JmolUtils.displayModel(question.getModelName());
            }
        }).start();

    }

    public void displayWebQuestion(WebQuestion question) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(question.getWeb()));
            FlowManager.displayQuestion(question);
        } catch (IOException | URISyntaxException ex) {
            System.err.println("Illegal web URL - " + ex);
        }
    }

    public void displayGroups(GroupingQuest group) {
        FlowManager.gotoGroupScreen(group);
    }

    public void displayConversion(ConversionQuest conversion) {
        FlowManager.displayConversionScreen(conversion);
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
