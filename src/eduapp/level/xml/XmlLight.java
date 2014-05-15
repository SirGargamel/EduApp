package eduapp.level.xml;

import eduapp.level.Light;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlLight extends XmlEntity<Light> {

    public XmlLight(Element node) {
        super(node);
    }

    @Override
    public Light generateGameEntity() {
        final Map<String, String> params = new HashMap<>();
        final NodeList nl = element.getChildNodes();
        Node subNode;
        for (int i = 0; i < nl.getLength(); i++) {
            subNode = nl.item(i);
            params.put(subNode.getNodeName(), subNode.getTextContent());
        }
        return new Light(element.getNodeName(), params);
    }

}
