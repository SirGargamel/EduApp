package eduapp.level.xml;

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

    private static final String ITEM_QUESTION = "Question";
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
        return new Quest(lines);
    }

    private QuestItem generateQuestItem(final Node node) {
        final QuestItem result;
        switch (node.getNodeName()) {
            case ITEM_QUESTION:
                final String[] split = node.getTextContent().split(QUESTION_SEPARATOR);
                if (split.length == 2) {
                    result = new Question(split[0], split[1]);
                } else {
                    throw new IllegalArgumentException("Unsupported question text - " + node.getTextContent());
                }
                break;
            case ITEM_TASK:
                result = new Task(node.getTextContent());
                break;
            default:
                throw new IllegalArgumentException("Unsupported quest item type - " + node.getNodeName());
        }
        return result;
    }
}
