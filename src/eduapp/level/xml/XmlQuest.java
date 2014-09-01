package eduapp.level.xml;

import eduapp.level.quest.ConversionQuest;
import eduapp.level.quest.EquationQuest;
import eduapp.level.quest.EquationQuest.Equation;
import eduapp.level.quest.EquationQuest.Mode;
import eduapp.level.quest.FinalQuest;
import eduapp.level.quest.GroupingQuest;
import eduapp.level.quest.HelpQuest;
import eduapp.loaders.LevelLoader;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.quest.WebQuestion;
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
    private static final String ITEM_CONVERSION = "Prevod";
    private static final String ITEM_COUNT = "Pocet";
    private static final String ITEM_DATA = "Data";
    private static final String ITEM_EQUATION = "Rovnice";
    private static final String ITEM_FINAL = "Final";
    private static final String ITEM_GROUPS = "Skupiny";    
    private static final String ITEM_MODE = "Mod";    
    private static final String ITEM_QUESTION = "Otazka";
    private static final String ITEM_QUESTION_JMOL = "Jmol";
    private static final String ITEM_QUESTION_WEB = "Web";
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
        final HelpQuest hq = generateHelpQuest(element.getElementsByTagName(ITEM_HELP).item(0));
        final FinalQuest fq = generateFinalQuest(element.getElementsByTagName(ITEM_FINAL).item(0));
        final Quest result = new Quest(lines, hq, fq);
        return result;
    }

    private QuestItem generateQuestItem(final Node node) {
        final QuestItem result;
        String[] split;
        final Element e = (Element) node;
        switch (e.getNodeName()) {
            case ITEM_CONVERSION:
                result = new ConversionQuest(
                        extractNodeText(e, ITEM_QUESTION),
                        extractNodeText(e, ITEM_DATA),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_GROUPS:
                result = new GroupingQuest(
                        extractNodeText(e, ITEM_QUESTION),
                        extractNodeText(e, ITEM_DATA),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUESTION:
                result = new Question(
                        extractNodeText(e, ITEM_QUESTION),
                        extractNodeText(e, ITEM_ANSWER),
                        extractNodeText(e, ITEM_REWARD), false);
                break;
            case ITEM_QUESTION_JMOL:
                result = new JmolQuestion(                        
                        extractNodeText(e, ITEM_DATA).split(SEPARATOR_BASIC),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUESTION_WEB:
                result = new WebQuestion(
                        extractNodeText(e, ITEM_QUESTION),
                        extractNodeText(e, ITEM_ANSWER),
                        extractNodeText(e, ITEM_DATA),
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_EQUATION:
                final EquationQuest dq = new EquationQuest(Mode.valueOf(extractNodeText(e, ITEM_MODE).toLowerCase()), extractNodeText(e, ITEM_REWARD));
                final NodeList nl = e.getElementsByTagName(ITEM_EQUATION);
                Equation eq;
                String text;
                for (int i = 0; i < nl.getLength(); i++) {
                    eq = new Equation();
                    text = nl.item(i).getTextContent();
                    split = text.substring(text.indexOf(SEPARATOR_QUESTION_OUT) + 1).split(SEPARATOR_QUESTION_ITEM);
                    for (String s : split) {
                        eq.addOutput(s);
                    }
                    split = text.substring(0, text.indexOf(SEPARATOR_QUESTION_OUT)).split(SEPARATOR_QUESTION_ITEM);
                    for (String s : split) {
                        eq.addInput(s);
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
            case ITEM_HELP:
            case ITEM_FINAL:
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

    private HelpQuest generateHelpQuest(final Node node) {
        final List<Question> questions = new LinkedList<>();
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
                    questions.add(new Question(question, text, null, true));
                    question = null;
                }
            }
        }
        return new HelpQuest(questions);
    }

    private FinalQuest generateFinalQuest(final Node node) {
        final Element e = (Element) node;
        FinalQuest result = new FinalQuest(Integer.parseInt(extractNodeText(e, ITEM_COUNT)));

        final NodeList nl = e.getElementsByTagName(ITEM_EQUATION);
        Equation eq;
        String text;
        String[] split;
        for (int i = 0; i < nl.getLength(); i++) {
            eq = new Equation();
            text = nl.item(i).getTextContent();
            split = text.substring(text.indexOf(SEPARATOR_QUESTION_OUT) + 1).split(SEPARATOR_QUESTION_ITEM);
            for (String s : split) {
                eq.addOutput(s);
            }
            split = text.substring(0, text.indexOf(SEPARATOR_QUESTION_OUT)).split(SEPARATOR_QUESTION_ITEM);
            for (String s : split) {
                eq.addInput(s);
            }
            result.addEquation(eq);
        }
        return result;
    }

    private static String extractNodeText(Element e, String nodeName) {
        return e.getElementsByTagName(nodeName).item(0).getTextContent();
    }
}
