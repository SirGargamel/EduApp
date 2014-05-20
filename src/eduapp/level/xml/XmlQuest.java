package eduapp.level.xml;

import eduapp.level.Quest;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlQuest extends XmlEntity<Quest> {

    private static final String NODE_LINE = "Line";

    public XmlQuest(Element node) {
        super(node);
    }

    @Override
    public Quest generateGameEntity() {
        final NodeList nl = element.getElementsByTagName(NODE_LINE);
        final List<String> lines = new ArrayList<>(nl.getLength());
        for (int i = 0; i < nl.getLength(); i++) {
            lines.add(nl.item(i).getTextContent());
        }
        return new Quest(lines);
    }
}
