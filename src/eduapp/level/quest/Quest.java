package eduapp.level.quest;

import eduapp.FlowManager;
import eduapp.JmolUtils;
import eduapp.level.item.Item;
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

    private static final String ACTION_DISPLAY = "Display";
    private static final String ACTION_DISPLAY_DRAG = "D";
    private static final String ACTION_DISPLAY_CONVERSION = "C";
    private static final String ACTION_DISPLAY_GROUP = "G";
    private static final String ACTION_DISPLAY_JMOL = "J";
    private static final String ACTION_DISPLAY_QUESTION = "Q";
    private static final String ACTION_DISPLAY_WEB = "W";
    private final List<QuestItem> data;
    private String name;

    public Quest(List<QuestItem> data) {
        this.data = data;
    }

    public void makeActive() {
        FlowManager.getInstance().displayQuest(this);
    }

    public void executeAction(final String action) {
        if (action.startsWith(ACTION_DISPLAY)) {
            final String rest = action.replace(ACTION_DISPLAY, "");

            if (rest.startsWith(ACTION_DISPLAY_QUESTION)) {
                final String number = rest.replace(ACTION_DISPLAY_QUESTION, "");
                displayQuestion(extractQuestItem(Question.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_JMOL)) {
                final String number = rest.replace(ACTION_DISPLAY_JMOL, "");
                displayJmolQuestion(extractQuestItem(JmolQuestion.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_WEB)) {
                final String number = rest.replace(ACTION_DISPLAY_WEB, "");
                displayWebQuestion(extractQuestItem(WebQuestion.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_GROUP)) {
                final String number = rest.replace(ACTION_DISPLAY_GROUP, "");
                displayGroups(extractQuestItem(GroupingQuest.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_CONVERSION)) {
                final String number = rest.replace(ACTION_DISPLAY_CONVERSION, "");
                displayConversion(extractQuestItem(ConversionQuest.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_DRAG)) {
                final String number = rest.replace(ACTION_DISPLAY_DRAG, "");
                displayDrag(extractQuestItem(EquationQuest.class, Integer.valueOf(number)));
            }
        }
    }

    public QuestItem findQuestItem(final String description) {
        String rest = description.replace(ACTION_DISPLAY, "");
        QuestItem result = null;
        if (rest.startsWith(ACTION_DISPLAY_QUESTION)) {
            final String number = rest.replace(ACTION_DISPLAY_QUESTION, "");
            result = extractQuestItem(Question.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_JMOL)) {
            final String number = rest.replace(ACTION_DISPLAY_JMOL, "");
            result = extractQuestItem(JmolQuestion.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_WEB)) {
            final String number = rest.replace(ACTION_DISPLAY_WEB, "");
            result = extractQuestItem(WebQuestion.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_GROUP)) {
            final String number = rest.replace(ACTION_DISPLAY_GROUP, "");
            result = extractQuestItem(GroupingQuest.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_CONVERSION)) {
            final String number = rest.replace(ACTION_DISPLAY_CONVERSION, "");
            result = extractQuestItem(ConversionQuest.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_DRAG)) {
            final String number = rest.replace(ACTION_DISPLAY_DRAG, "");
            result = extractQuestItem(EquationQuest.class, Integer.valueOf(number));
        }
        return result;
    }

    private <T extends QuestItem> T extractQuestItem(final Class<T> cls, final int pos) {
        Object result = null;
        int counter = 0;
        for (QuestItem qi : getData()) {
            if (cls.isAssignableFrom(qi.getClass())) {
                counter++;
                if (counter == pos) {
                    result = qi;
                    break;
                }
            }
        }
        return (T) result;
    }

    public void displayQuestion(Question question) {
        FlowManager.getInstance().displayQuestion(question);
    }

    public void displayJmolQuestion(final JmolQuestion question) {
        FlowManager.getInstance().displayQuestion(question);
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
            FlowManager.getInstance().displayQuestion(question);
        } catch (IOException | URISyntaxException ex) {
            System.err.println("Illegal web URL - " + ex);
        }
    }

    public void displayGroups(GroupingQuest group) {
        FlowManager.getInstance().gotoGroupScreen(group);
    }

    public void displayConversion(ConversionQuest conversion) {
        FlowManager.getInstance().displayConversionScreen(conversion);
    }

    public void displayDrag(EquationQuest quest) {
        FlowManager.getInstance().displayDragScreen(quest);
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
