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
    private static final String ACTION_DISPLAY_ADDING = "A";
    private static final String ACTION_DISPLAY_CONVERSION = "C";
    private static final String ACTION_DISPLAY_DRAG = "D";
    private static final String ACTION_DISPLAY_FINAL = "F";
    private static final String ACTION_DISPLAY_GROUP = "G";
    private static final String ACTION_DISPLAY_HELP = "H";
    private static final String ACTION_DISPLAY_JMOL = "J";
    private static final String ACTION_DISPLAY_PEXESO = "P";
    private static final String ACTION_DISPLAY_PICKING = "PC";
    private static final String ACTION_DISPLAY_MATCHING = "M";
    private static final String ACTION_DISPLAY_ORDERING = "O";
    private static final String ACTION_DISPLAY_QUESTION = "Q";
    private static final String ACTION_DISPLAY_QUESTION_MULTI = "QM";
    private static final String ACTION_DISPLAY_WEB = "W";
    private final List<QuestItem> data;
    private final QuestHelp help;
    private final QuestFinal finalQuest;
    private final String description, ending;

    public Quest(List<QuestItem> data, QuestHelp help, QuestFinal finalQuest, final String description, final String ending) {
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

            if (rest.startsWith(ACTION_DISPLAY_JMOL)) {
                final String number = rest.replace(ACTION_DISPLAY_JMOL, "");
                displayJmolQuestion(extractQuestItem(QuestQuestionJmol.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_ADDING)) {
                final String number = rest.replace(ACTION_DISPLAY_ADDING, "");
                displayAdding(extractQuestItem(QuestAdding.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_QUESTION_MULTI)) {
                final String number = rest.replace(ACTION_DISPLAY_QUESTION_MULTI, "");
                displayQuestionMulti(extractQuestItem(QuestQuestionMultiAnswer.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_QUESTION)) {
                final String number = rest.replace(ACTION_DISPLAY_QUESTION, "");
                displayQuestion(extractQuestItem(QuestQuestion.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_WEB)) {
                final String number = rest.replace(ACTION_DISPLAY_WEB, "");
                displayWebQuestion(extractQuestItem(QuestQuestionWeb.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_GROUP)) {
                final String number = rest.replace(ACTION_DISPLAY_GROUP, "");
                displayGroups(extractQuestItem(QuestGrouping.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_CONVERSION)) {
                final String number = rest.replace(ACTION_DISPLAY_CONVERSION, "");
                displayConversion(extractQuestItem(QuestConversion.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_DRAG)) {
                final String number = rest.replace(ACTION_DISPLAY_DRAG, "");
                displayEquation(extractQuestItem(QuestEquation.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_MATCHING)) {
                final String number = rest.replace(ACTION_DISPLAY_MATCHING, "");
                displayMatching(extractQuestItem(QuestMatching.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_PICKING)) {
                final String number = rest.replace(ACTION_DISPLAY_PICKING, "");
                displayPicking(extractQuestItem(QuestPicking.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_PEXESO)) {
                final String number = rest.replace(ACTION_DISPLAY_PEXESO, "");
                displayPexeso(extractQuestItem(QuestPexeso.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_ORDERING)) {
                final String number = rest.replace(ACTION_DISPLAY_ORDERING, "");
                displayOrdering(extractQuestItem(QuestOrdering.class, Integer.valueOf(number)));
            } else if (rest.startsWith(ACTION_DISPLAY_HELP)) {
                if (getFailedQuestItem() != null) {
                    displayQuestion(help.getNextQuestion());
                } else {
                    FlowManager.getInstance().displayMessage("Nemáte žádné špatně zodpovězené úkoly.", null);
                }
            } else if (rest.startsWith(ACTION_DISPLAY_FINAL)) {
                if (eduapp.EduApp.DEBUG) {
                    executeAction(ACTION_DISPLAY.concat(finalQuest.getQuestId()));
                } else {
                    if (isReadyForFinalQuest()) {
                        executeAction(ACTION_DISPLAY.concat(finalQuest.getQuestId()));
                    } else {
                        FlowManager.getInstance().displayMessage("Nejdříve musíte dokončit všechny základní úkoly.", null);
                    }
                }
            }
        }
    }

    public QuestItem findQuestItem(final String description) {
        String rest = description.replace(ACTION_DISPLAY, "");
        QuestItem result = null;
        if (rest.startsWith(ACTION_DISPLAY_JMOL)) {
            final String number = rest.replace(ACTION_DISPLAY_JMOL, "");
            result = extractQuestItem(QuestQuestionJmol.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_ADDING)) {
            final String number = rest.replace(ACTION_DISPLAY_ADDING, "");
            result = extractQuestItem(QuestAdding.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_QUESTION_MULTI)) {
            final String number = rest.replace(ACTION_DISPLAY_QUESTION_MULTI, "");
            result = extractQuestItem(QuestQuestionMultiAnswer.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_QUESTION)) {
            final String number = rest.replace(ACTION_DISPLAY_QUESTION, "");
            result = extractQuestItem(QuestQuestion.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_WEB)) {
            final String number = rest.replace(ACTION_DISPLAY_WEB, "");
            result = extractQuestItem(QuestQuestionWeb.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_GROUP)) {
            final String number = rest.replace(ACTION_DISPLAY_GROUP, "");
            result = extractQuestItem(QuestGrouping.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_CONVERSION)) {
            final String number = rest.replace(ACTION_DISPLAY_CONVERSION, "");
            result = extractQuestItem(QuestConversion.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_DRAG)) {
            final String number = rest.replace(ACTION_DISPLAY_DRAG, "");
            result = extractQuestItem(QuestEquation.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_PICKING)) {
            final String number = rest.replace(ACTION_DISPLAY_PICKING, "");
            result = extractQuestItem(QuestPicking.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_PEXESO)) {
            final String number = rest.replace(ACTION_DISPLAY_PEXESO, "");
            result = extractQuestItem(QuestPexeso.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_ORDERING)) {
            final String number = rest.replace(ACTION_DISPLAY_ORDERING, "");
            result = extractQuestItem(QuestOrdering.class, Integer.valueOf(number));
        } else if (rest.startsWith(ACTION_DISPLAY_MATCHING)) {
            final String number = rest.replace(ACTION_DISPLAY_MATCHING, "");
            result = extractQuestItem(QuestMatching.class, Integer.valueOf(number));
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

    private void displayAdding(QuestAdding quest) {
        FlowManager.getInstance().displayAddingScreen(quest);
    }

    private void displayQuestion(QuestQuestion question) {
        FlowManager.getInstance().displayQuestion(question);
    }

    private void displayQuestionMulti(QuestQuestionMultiAnswer question) {
        FlowManager.getInstance().displayQuestionMulti(question);
    }

    private void displayJmolQuestion(final QuestQuestionJmol question) {
        FlowManager.getInstance().displayJmolQuestion(question);
    }

    private void displayWebQuestion(QuestQuestionWeb question) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(question.getWeb()));
            FlowManager.getInstance().displayQuestion(question);
        } catch (IOException | URISyntaxException ex) {
            System.err.println("Illegal web URL - " + ex);
        }
    }

    private void displayGroups(QuestGrouping group) {
        FlowManager.getInstance().displayGroupScreen(group);
    }

    private void displayConversion(QuestConversion conversion) {
        FlowManager.getInstance().displayConversionScreen(conversion);
    }

    private void displayEquation(QuestEquation quest) {
        FlowManager.getInstance().displayEquationScreen(quest);
    }

    private void displayPexeso(QuestPexeso quest) {
        FlowManager.getInstance().displayPexesoScreen(quest);
    }

    private void displayPicking(QuestPicking quest) {
        FlowManager.getInstance().displayPickingScreen(quest);
    }

    private void displayOrdering(QuestOrdering quest) {
        FlowManager.getInstance().displayOrderingScreen(quest);
    }

    private void displayMatching(QuestMatching quest) {
        FlowManager.getInstance().displayMatchingScreen(quest);
    }

    public List<QuestItem> getData() {
        return data;
    }

    public QuestHelp getHelp() {
        return help;
    }

    public QuestFinal getFinalQuest() {
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

    public boolean isReadyForFinalQuest() {
        int counter = 0;
        for (QuestItem qi : data) {
            if (qi.isFinished()) {
                counter++;
            }
        }
        return counter >= data.size() - 1;
    }

    public String getDescription() {
        return description;
    }

    public String getEnding() {
        return ending;
    }
}
