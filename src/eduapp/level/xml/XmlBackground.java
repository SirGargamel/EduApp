package eduapp.level.xml;

import eduapp.level.Background;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Petr Jecmen
 */
public class XmlBackground extends XmlEntity<Background> {

    private static final String ATTR_TEXTURE = "tag";
    private static final String NODE_EMPTY = "Empty";
    private static final String NODE_TEXTURE = "Texture";
    private static final String NODE_VALUES = "Values";
    private static final String NODE_WALL = "Wall";
    private static final String NODE_WIDTH = "Width";

    public XmlBackground(Element node) {
        super(node);
    }

    @Override
    public Background generateGameEntity() throws SAXException {        
        final String values = element.getElementsByTagName(NODE_VALUES).item(0).getTextContent().replaceAll("[\t ]", "");
        final Background result = new Background(values);
        // texture mappings
        NodeList nl = element.getElementsByTagName(NODE_TEXTURE);
        Element el;
        Node node;
        for (int i = 0; i < nl.getLength(); i++) {
            el = (Element) nl.item(i);
            result.addTextureMapping(el.getAttribute(ATTR_TEXTURE).charAt(0), el.getTextContent());
        }
        // walls
        nl = element.getElementsByTagName(NODE_WALL);
        for (int i = 0; i < nl.getLength(); i++) {
            node = nl.item(i);
            result.addWall(node.getTextContent().charAt(0));
        }
        // empty space
        nl = element.getElementsByTagName(NODE_EMPTY);
        if (nl.getLength() > 0) {
            result.setEmptyKey(nl.item(0).getTextContent().charAt(0));
        }

        return result;
    }
}
