package eduapp.level.xml;

import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.quest.Task;
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
    private static final String ITEM_QUESTION = "Question";
    private static final String ITEM_QUESTION_JMOL = "Jmol";
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
        switch (node.getNodeName()) {
            case ITEM_QUESTION:
                split = node.getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 2) {
                    result = new Question(split[0], split[1]);
                } else {
                    throw new IllegalArgumentException("Unsupported question text, separator must be :: - " + node.getTextContent());
                }
                break;
            case ITEM_QUESTION_JMOL:
                split = node.getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 3) {
                    result = new JmolQuestion(split[0], split[1], split[2]);
                } else {
                    throw new IllegalArgumentException("Unsupported Jmol question text, separator must be :: - " + node.getTextContent());
                }
                break;
            case ITEM_TASK:
                result = new Task(node.getTextContent());
                break;
            default:
                throw new IllegalArgumentException("Unsupported quest item type - " + node.getNodeName());
        }
        result.setId(((Element)node).getAttribute(LevelLoader.ATTR_ID));
        return result;
    }
}
