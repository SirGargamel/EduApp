package eduapp.level.xml;

import eduapp.level.quest.ConversionQuest;
import eduapp.level.quest.GroupingQuest;
import eduapp.loaders.LevelLoader;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.quest.Task;
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

    private static final String ATTR_NAME = "name";
    private static final String ITEM_CHILD = "Child";
    private static final String ITEM_CONVERSION = "Conversion";
    private static final String ITEM_DATA = "Data";
    private static final String ITEM_GROUPS = "Group";
    private static final String ITEM_QUESTION = "Question";
    private static final String ITEM_QUESTION_JMOL = "Jmol";
    private static final String ITEM_QUESTION_WEB = "Web";
    private static final String ITEM_TASK = "Task";
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
        final String[] split;
        final Element e = (Element) node;
        switch (e.getNodeName()) {
            case ITEM_CONVERSION:
                split = e.getElementsByTagName(ITEM_DATA).item(0).getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 2) {
                    result = new ConversionQuest(split[0], split[1]);
                } else {
                    throw new IllegalArgumentException("Unsupported conversion quest text, separator is ::, required 2 params - " + node.getTextContent());
                }
                break;
            case ITEM_GROUPS:
                split = e.getElementsByTagName(ITEM_DATA).item(0).getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 2) {
                    result = new GroupingQuest(split[0], split[1]);
                } else {
                    throw new IllegalArgumentException("Unsupported group task, separator is ::, required 2 params - " + node.getTextContent());
                }
                break;
            case ITEM_QUESTION:
                split = e.getElementsByTagName(ITEM_DATA).item(0).getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 2) {
                    result = new Question(split[0], split[1]);
                } else {
                    throw new IllegalArgumentException("Unsupported question text, separator is ::, required 2 params - " + node.getTextContent());
                }
                break;
            case ITEM_QUESTION_JMOL:
                split = e.getElementsByTagName(ITEM_DATA).item(0).getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 3) {
                    result = new JmolQuestion(split[0], split[1], split[2]);
                } else {
                    throw new IllegalArgumentException("Unsupported Jmol question text, separator is ::, required 3 params - " + node.getTextContent());
                }
                break;
            case ITEM_QUESTION_WEB:
                split = e.getElementsByTagName(ITEM_DATA).item(0).getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 3) {
                    result = new WebQuestion(split[0], split[1], split[2]);
                } else {
                    throw new IllegalArgumentException("Unsupported Web question text, separator is ::, required 3 params - " + node.getTextContent());
                }
                break;
            case ITEM_TASK:
                result = new Task(e.getElementsByTagName(ITEM_DATA).item(0).getTextContent());
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
}
