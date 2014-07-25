package eduapp.level.xml;

import eduapp.level.quest.ConversionQuest;
import eduapp.level.quest.EquationQuest;
import eduapp.level.quest.EquationQuest.Mode;
import eduapp.level.quest.GroupingQuest;
import eduapp.loaders.LevelLoader;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.quest.WebQuestion;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlQuest extends XmlEntity<Quest> {

    private static final String ATTR_NAME = "jmeno";
    private static final String EQUATION_EXTRA = "Extra";
    private static final String EQUATION_STATIC = "_";
    private static final String ITEM_ANSWER = "Odpoved";
    private static final String ITEM_CHILD = "Dite";
    private static final String ITEM_CONVERSION = "Prevod";
    private static final String ITEM_DATA = "Data";
    private static final String ITEM_EQUATION = "Rovnice";
    private static final String ITEM_GROUPS = "Skupiny";
    private static final String ITEM_MODE = "mod";
    private static final String ITEM_QUESTION = "Otazka";
    private static final String ITEM_QUESTION_JMOL = "Jmol";
    private static final String ITEM_QUESTION_WEB = "Web";
    private static final String ITEM_REWARD = "Odmena";
    private static final String QUESTION_SEPARATOR = "::";

    public XmlQuest(Element node) {
        super(node);
    }

    @Override
    public Quest generateGameEntity() {
        final NodeList nl = element.getChildNodes();
        final List<QuestItem> lines = new ArrayList<>(nl.getLength());
        Node node;
        for (int i = 0; i < nl.getLength(); i++) {
            node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                lines.add(generateQuestItem(nl.item(i)));
            }
        }
        final Quest result = new Quest(lines);
        final String name = element.getAttribute(ATTR_NAME);
        if (!name.isEmpty()) {
            result.setName(name);
        }
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
                        extractNodeText(e, ITEM_REWARD));
                break;
            case ITEM_QUESTION_JMOL:
                result = new JmolQuestion(
                        extractNodeText(e, ITEM_QUESTION),
                        extractNodeText(e, ITEM_ANSWER),
                        extractNodeText(e, ITEM_DATA),
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
                EquationQuest dq = new EquationQuest(Mode.valueOf(extractNodeText(e, ITEM_MODE).toLowerCase()), extractNodeText(e, ITEM_REWARD));
                split = extractNodeText(e, ITEM_DATA).split(QUESTION_SEPARATOR);
                for (String s : split) {
                    if (s.startsWith(EQUATION_STATIC)) {
                        dq.addItem(new EquationQuest.DragItem(EquationQuest.DragItemType.STATIC, s.substring(EQUATION_STATIC.length())));
                    } else {
                        dq.addItem(new EquationQuest.DragItem(EquationQuest.DragItemType.DRAG, s));
                    }
                }
                split = extractNodeText(e, EQUATION_EXTRA).split(QUESTION_SEPARATOR);
                for (String s : split) {
                    dq.addExtra(s);
                }
                result = dq;
                break;
            default:
                throw new IllegalArgumentException("Unsupported quest item type - " + node.getNodeName());
        }
        final NodeList nl = e.getElementsByTagName(ITEM_CHILD);
        for (int i = 0; i < nl.getLength(); i++) {
            result.addChild(nl.item(i).getTextContent());
        }
        result.setId(((Element) node).getAttribute(LevelLoader.ATTR_ID));
        return result;
    }

    private static String extractNodeText(Element e, String nodeName) {
        return e.getElementsByTagName(nodeName).item(0).getTextContent();
    }
}
