package eduapp.level;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Petr Ječmen
 */
public class LevelLoader implements AssetLoader {

    private static final String ATTR_TEXTURE = "tag";
    private static final String NODE_BACKGROUND = "Background";
    private static final String NODE_COORDS = "Coords";
    private static final String NODE_EMPTY = "Empty";
    private static final String NODE_ITEM = "Item";
    private static final String NODE_ITEMS = "Items";
    private static final String NODE_LIGHTS = "Lights";
    private static final String NODE_NAME = "Name";
    private static final String NODE_PLAYER = "Player";
    private static final String NODE_SCALE = "Scale";
    private static final String NODE_TEXTURE = "Texture";
    private static final String NODE_VALUES = "Values";
    private static final String NODE_WALL = "Wall";
    private static final String NODE_WIDTH = "Width";

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        Level result = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(assetInfo.openStream());
            doc.getDocumentElement().normalize();

            final Background background = loadBackground(doc);
            final Player player = loadPlayer(doc);
            final Set<Item> items = loadItems(doc);
            final Set<Light> lights = loadLights(doc);
            result = new Level(background, player, items, lights);
        } catch (NullPointerException | ParserConfigurationException | SAXException ex) {
            ex.printStackTrace(System.err);
        }

        return result;
    }

    private static Background loadBackground(final Document doc) throws SAXException {
        final Node backNode = doc.getElementsByTagName(NODE_BACKGROUND).item(0);
        final Background result;
        Node node;
        Element el;
        if (backNode.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) backNode;
            result = Background.buildBackground(
                    element.getElementsByTagName(NODE_VALUES).item(0).getTextContent().replaceAll("[\n\t]", ""),
                    element.getElementsByTagName(NODE_COORDS).item(0).getTextContent(),
                    element.getElementsByTagName(NODE_WIDTH).item(0).getTextContent());
            // texture mappings
            NodeList nl = element.getElementsByTagName(NODE_TEXTURE);
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
        } else {
            throw new SAXException("Illegal background node - " + backNode);
        }
        return result;
    }

    private static Player loadPlayer(final Document doc) throws SAXException {
        final Node playerNode = doc.getElementsByTagName(NODE_PLAYER).item(0);
        final Player result;
        if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) playerNode;
            result = Player.buildPlayer(element.getElementsByTagName(NODE_COORDS).item(0).getTextContent());
        } else {
            throw new SAXException("Illegal background node - " + playerNode);
        }
        return result;
    }

    private static Set<Item> loadItems(final Document doc) throws SAXException {
        final Node itemsNode = doc.getElementsByTagName(NODE_ITEMS).item(0);
        final Set<Item> result = new HashSet<>();
        Element element;
        if (itemsNode.getNodeType() == Node.ELEMENT_NODE) {
            element = (Element) itemsNode;
            // texture mappings
            NodeList nl = element.getElementsByTagName(NODE_ITEM);
            for (int i = 0; i < nl.getLength(); i++) {
                element = (Element) nl.item(i);
                result.add(Item.generateItem(
                        element.getElementsByTagName(NODE_NAME).item(0).getTextContent(),
                        element.getElementsByTagName(NODE_COORDS).item(0).getTextContent(),
                        element.getElementsByTagName(NODE_SCALE).item(0).getTextContent()));
            }
        } else {
            throw new SAXException("Illegal items node - " + itemsNode);
        }
        return result;
    }

    private static Set<Light> loadLights(final Document doc) {
        final Set<Light> result = new LinkedHashSet<>();

        final Node lightsNode = doc.getElementsByTagName(NODE_LIGHTS).item(0);
        final NodeList lights = lightsNode.getChildNodes();
        Node node, subNode;
        NodeList nl;
        Map<String, String> params;
        for (int i = 0; i < lights.getLength(); i++) {
            params = new HashMap<>();

            node = lights.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                nl = node.getChildNodes();
                for (int j = 0; j < nl.getLength(); j++) {
                    subNode = nl.item(j);
                    params.put(subNode.getNodeName(), subNode.getTextContent());
                }
                result.add(new Light(node.getNodeName(), params));
            }
        }

        return result;
    }

}
