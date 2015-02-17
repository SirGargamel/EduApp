package eduapp.level.xml;

import eduapp.level.quest.QuestConversion;
import eduapp.level.quest.QuestEquation;
import eduapp.level.quest.QuestEquation.Equation;
import eduapp.level.quest.QuestEquation.Mode;
import eduapp.level.quest.QuestFinal;
import eduapp.level.quest.QuestGrouping;
import eduapp.level.quest.QuestHelp;
import eduapp.loaders.LevelLoader;
import eduapp.level.quest.QuestQuestionJmol;
import eduapp.level.quest.QuestQuestionMultiAnswer;
import eduapp.level.quest.QuestOrdering;
import eduapp.level.quest.QuestPexeso;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestAdding;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.QuestMatching;
import eduapp.level.quest.QuestPicking;
import eduapp.level.quest.QuestQuestion;
import eduapp.level.quest.QuestQuestionWeb;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlQuest extends XmlEntity<Quest> {

    private static final String EQUATION_EXTRA = "Extra";
    private static final String ITEM_ANSWER = "Odpoved";
    private static final String ITEM_CHILD = "Dite";
    private static final String ITEM_DATA = "Data";
    private static final String ITEM_DESCRIPTION = "Popis";
    private static final String ITEM_ENDING = "Konec";
    private static final String ITEM_MODE = "Mod";
    private static final String ITEM_QUEST_ADDING = "Add";
    private static final String ITEM_QUEST_CONVERSION = "Prevod";
    private static final String ITEM_QUEST_EQUATION = "Rovnice";
    private static final String ITEM_QUEST_FINAL = "Final";
    private static final String ITEM_QUEST_GROUPS = "Skupiny";
    private static final String ITEM_QUEST_ORDER = "Order";
    private static final String ITEM_QUEST_PEXESO = "Pexeso";
    private static final String ITEM_QUEST_PICKING = "Pick";
    private static final String ITEM_QUEST_QUESTION = "Otazka";
    private static final String ITEM_QUEST_QUESTION_JMOL = "Jmol";
    private static final String ITEM_QUEST_QUESTION_MATCH = "Match";
    private static final String ITEM_QUEST_QUESTION_MULTI = "Multi";
    private static final String ITEM_QUEST_QUESTION_WEB = "Web";
    private static final String ITEM_REWARD = "Odmena";
    private static final String ITEM_HELP = "Pomocne";
    private static final String SEPARATOR_BASIC = ";";
    private static final String SEPARATOR_QUESTION_OUT = "=";
    private static final String SEPARATOR_QUESTION_ITEM = "\\+";

    public XmlQuest(Element node) {
        super(node);
    }

    @Override
    public Quest generateGameEntity() {
        final NodeList nl = element.getChildNodes();
        final List<QuestItem> lines = new ArrayList<>(nl.getLength());
        Node node;
        QuestItem qi;
        for (int i = 0; i < nl.getLength(); i++) {
            node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                qi = generateQuestItem(nl.item(i));
                if (qi != null) {
                    lines.add(qi);
                }
            }
        }
        final QuestHelp hq = generateHelpQuest(element.getElementsByTagName(ITEM_HELP).item(0));
        final QuestFinal fq = generateFinalQuest(element.getElementsByTagName(ITEM_QUEST_FINAL).item(0));
        final Quest result = new Quest(
                lines, hq, fq,
                extractNodeText(element, ITEM_DESCRIPTION),
                extractNodeText(element, ITEM_ENDING));
        return result;
    }

    private QuestItem generateQuestItem(final Node node) {
        final QuestItem result;
        String[] split;
        final Element e = (Element) node;
        switch (e.getNodeName()) {
            case ITEM_QUEST_CONVERSION:
                result = new QuestConversion(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_DATA),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_GROUPS:
                result = new QuestGrouping(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_DATA),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_ADDING:
                result = new QuestAdding(
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_ANSWER).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_QUESTION:
                result = new QuestQuestion(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_ANSWER),
                        extractNodeText(e, ITEM_REWARD), false, false);
                break;
            case ITEM_QUEST_QUESTION_JMOL:
                result = new QuestQuestionJmol(
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_QUESTION_WEB:
                result = new QuestQuestionWeb(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_ANSWER),
                        extractNodeText(e, ITEM_DATA),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_QUESTION_MATCH:
                result = new QuestMatching(
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_QUESTION_MULTI:
                result = new QuestQuestionMultiAnswer(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_ANSWER).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_ORDER:
                result = new QuestOrdering(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_EQUATION:
                final QuestEquation dq = new QuestEquation(Mode.valueOf(extractNodeText(e, ITEM_MODE).toLowerCase()), extractNodeText(e, ITEM_REWARD));
                final NodeList nl = e.getElementsByTagName(ITEM_QUEST_EQUATION);
                Equation eq;
                String text;
                for (int i = 0; i < nl.getLength(); i++) {
                    eq = new Equation();
                    text = nl.item(i).getTextContent();
                    split = text.substring(0, text.indexOf(SEPARATOR_QUESTION_OUT)).split(SEPARATOR_QUESTION_ITEM);
                    for (String s : split) {
                        eq.addInput(s);
                    }
                    final int index = text.indexOf(SEPARATOR_BASIC);
                    if (index >= 0) {
                        split = text.substring(text.indexOf(SEPARATOR_QUESTION_OUT) + 1, index).split(SEPARATOR_QUESTION_ITEM);
                        for (String s : split) {
                            eq.addOutput(s);
                        }
                        split = text.substring(text.indexOf(SEPARATOR_BASIC) + 1).split(SEPARATOR_BASIC);
                        for (String s : split) {
                            eq.addCatalyst(s);
                        }
                    } else {
                        split = text.substring(text.indexOf(SEPARATOR_QUESTION_OUT) + 1).split(SEPARATOR_QUESTION_ITEM);
                        for (String s : split) {
                            eq.addOutput(s);
                        }
                    }
                    dq.addEquation(eq);
                }
                split = extractNodeText(e, EQUATION_EXTRA).split(SEPARATOR_BASIC);
                for (String s : split) {
                    if (!s.isEmpty()) {
                        dq.addExtra(s);
                    }
                }
                result = dq;
                break;
            case ITEM_QUEST_PEXESO:
                result = new QuestPexeso(
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUEST_PICKING:
                result = new QuestPicking(
                        extractNodeText(e, ITEM_QUEST_QUESTION),
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_HELP:
            case ITEM_QUEST_FINAL:
            case ITEM_DESCRIPTION:
            case ITEM_ENDING:
                result = null;
                break;
            default:
                throw new IllegalArgumentException("Unsupported quest item type - " + node.getNodeName());
        }
        if (result != null) {
            final NodeList nl = e.getElementsByTagName(ITEM_CHILD);
            for (int i = 0; i < nl.getLength(); i++) {
                result.addChild(nl.item(i).getTextContent());
            }
            result.setId(((Element) node).getAttribute(LevelLoader.ATTR_ID));
        }
        return result;
    }

    private QuestHelp generateHelpQuest(final Node node) {
        final List<QuestQuestion> questions = new LinkedList<>();
        String question = null;

        final NodeList nl = node.getChildNodes();
        Node n;
        String text;
        for (int i = 0; i < nl.getLength(); i++) {
            n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                text = nl.item(i).getTextContent();
                if (question == null) {
                    question = text;
                } else {
                    questions.add(new QuestQuestion(question, text, null, true, false));
                    question = null;
                }
            }
        }
        return new QuestHelp(questions);
    }

    private QuestFinal generateFinalQuest(final Node node) {
        final Element e = (Element) node;
        QuestFinal result = new QuestFinal();

        final NodeList nl = e.getElementsByTagName(ITEM_QUEST_EQUATION);
        int itemCount = 0;
        Equation eq;
        String text;
        String[] split;
        for (int i = 0; i < nl.getLength(); i++) {
            eq = new Equation();
            text = nl.item(i).getTextContent();
            split = text.substring(0, text.indexOf(SEPARATOR_QUESTION_OUT)).split(SEPARATOR_QUESTION_ITEM);
            for (String s : split) {
                itemCount++;
                eq.addInput(s);
            }
            final int index = text.indexOf(SEPARATOR_BASIC);
            if (index >= 0) {
                split = text.substring(text.indexOf(SEPARATOR_QUESTION_OUT) + 1, index).split(SEPARATOR_QUESTION_ITEM);
                for (String s : split) {
                    eq.addOutput(s);
                    itemCount++;
                }
                split = text.substring(text.indexOf(SEPARATOR_BASIC) + 1).split(SEPARATOR_BASIC);
                for (String s : split) {
                    eq.addCatalyst(s);
                    itemCount++;
                }
            } else {
                split = text.substring(text.indexOf(SEPARATOR_QUESTION_OUT) + 1).split(SEPARATOR_QUESTION_ITEM);
                for (String s : split) {
                    eq.addOutput(s);
                    itemCount++;
                }
            }
            result.addEquation(eq);
        }
        result.setItemCount(itemCount);
        return result;
    }

    private static String extractNodeText(Element e, String nodeName) {
        return e.getElementsByTagName(nodeName).item(0).getTextContent().replace("\\n", "\n");
    }
}
