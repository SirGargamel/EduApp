package eduapp.level.quest;

import eduapp.FlowManager;
import eduapp.level.item.Item;
import eduapp.level.Level;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class Quest extends Item {

    private static final String ACTION_DISPLAY = "Display";
    private static final String ACTION_DISPLAY_DRAG = "D";
    private static final String ACTION_DISPLAY_CONVERSION = "C";
    private static final String ACTION_DISPLAY_FINAL = "F";
    private static final String ACTION_DISPLAY_GROUP = "G";
    private static final String ACTION_DISPLAY_HELP = "H";
    private static final String ACTION_DISPLAY_JMOL = "J";
    private static final String ACTION_DISPLAY_PEXESO = "P";
    private static final String ACTION_DISPLAY_QUESTION = "Q";
    private static final String ACTION_DISPLAY_WEB = "W";
    private final List<QuestItem> data;
    private final HelpQuest help;
    private final FinalQuest finalQuest;
    private final String description, ending;

    public Quest(List<QuestItem> data, HelpQuest help, FinalQuest finalQuest, final String description, final String ending) {
        this.data = data;
        this.help = help;
        this.finalQuest = finalQuest;
        this.description = description;
        this.ending = ending;
    }

    public void makeActive() {
        FlowManager.getInstance().makeQuestActive(this);
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
                displayEquation(extractQuestItem(EquationQuest.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_PEXESO)) {
                final String number = rest.replace(ACTION_DISPLAY_PEXESO, "");
                displayPexeso(extractQuestItem(PexesoQuest.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_HELP)) {
                if (getFailedQuestItem() != null) {
                    displayQuestion(help.getNextQuestion());
                } else {
                    FlowManager.getInstance().displayMessage("Nemáte žádné špatně zodpovězené úkoly.", null);
                }
            } else if (rest.startsWith(ACTION_DISPLAY_FINAL)) {
                if (isFinished()) {
                    displayEquation(finalQuest);
                } else {
                    FlowManager.getInstance().displayMessage("Nejdříve musíte dokončit všechny základní úkoly.", null);
                }
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
        } else if (rest.startsWith(ACTION_DISPLAY_PEXESO)) {
            final String number = rest.replace(ACTION_DISPLAY_PEXESO, "");
            result = extractQuestItem(PexesoQuest.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_HELP)) {
            result = help;
        } else if (rest.startsWith(ACTION_DISPLAY_FINAL)) {
            result = finalQuest;
        }
        return result;
    }

    public QuestItem getFailedQuestItem() {
        QuestItem result = null;
        for (QuestItem qi : data) {
            if (qi.isFailed() && !qi.isFinished()) {
                result = qi;
                break;
            }
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

    private void displayQuestion(Question question) {
        FlowManager.getInstance().displayQuestion(question);
    }

    private void displayJmolQuestion(final JmolQuestion question) {
        FlowManager.getInstance().displayJmolQuestion(question);
    }

    private void displayWebQuestion(WebQuestion question) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(question.getWeb()));
            FlowManager.getInstance().displayQuestion(question);
        } catch (IOException | URISyntaxException ex) {
            System.err.println("Illegal web URL - " + ex);
        }
    }

    private void displayGroups(GroupingQuest group) {
        FlowManager.getInstance().gotoGroupScreen(group);
    }

    private void displayConversion(ConversionQuest conversion) {
        FlowManager.getInstance().displayConversionScreen(conversion);
    }

    private void displayEquation(EquationQuest quest) {
        FlowManager.getInstance().displayEquationScreen(quest);
    }

    private void displayPexeso(PexesoQuest quest) {
        FlowManager.getInstance().displayPexesoScreen(quest);
    }

    public List<QuestItem> getData() {
        return data;
    }

    public HelpQuest getHelp() {
        return help;
    }

    public FinalQuest getFinalQuest() {
        return finalQuest;
    }

    public void assignInterfaces(final Level level) {
        for (QuestItem qi : data) {
            qi.setLevel(level);
            qi.setItemRegistry(itemRegistry);
        }
    }

    public boolean isFinished() {
        boolean result = true;
        for (QuestItem qi : data) {
            if (!qi.isFinished()) {
                result = false;
                break;
            }
        }
        return result;
    }

    public String getDescription() {
        return description;
    }

    public String getEnding() {
        return ending;
    }
}
