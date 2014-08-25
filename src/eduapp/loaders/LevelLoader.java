package eduapp.loaders;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import eduapp.level.Player;
import eduapp.level.Background;
import eduapp.level.Model;
import eduapp.level.Level;
import eduapp.level.Light;
import eduapp.level.item.Item;
import eduapp.level.item.ItemParameters;
import eduapp.level.quest.Quest;
import eduapp.level.trigger.TriggerStub;
import eduapp.level.xml.XmlBackground;
import eduapp.level.xml.XmlLight;
import eduapp.level.xml.XmlModel;
import eduapp.level.xml.XmlPlayer;
import eduapp.level.xml.XmlQuest;
import eduapp.level.xml.XmlTrigger;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    public static final String EXTENSION = "xml";
    public static final String ATTR_ID = "id";
    private static final String NODE_ACTION_ITEMS = "Spoustece";
    private static final String NODE_BACKGROUND = "Pozadi";
    private static final String NODE_ELEMENTS = "Elementy";
    private static final String NODE_MISC = "Misc";
    private static final String NODE_ITEMS = "Veci";
    private static final String NODE_ITEM = "Vec";
    private static final String NODE_LIGHTS = "Svetla";
    private static final String NODE_PLAYER = "Hrac";
    private static final String NODE_QUEST = "Ukoly";

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
            final Set<Model> items = loadItems(doc);
            final Set<Light> lights = loadLights(doc);
            final Set<TriggerStub> actionItems = loadTriggers(doc);
            final Quest quest = loadQuest(doc);
            final Set<Item> dict = loadDictionary(doc, NODE_ELEMENTS);
            dict.addAll(loadDictionary(doc, NODE_MISC));
            
            result = new Level(background, player, items, lights, actionItems, quest, dict);
        } catch (NullPointerException | ParserConfigurationException | SAXException ex) {
            ex.printStackTrace(System.err);
        }

        return result;
    }

    private static Background loadBackground(final Document doc) throws SAXException {
        final Node backNode = doc.getElementsByTagName(NODE_BACKGROUND).item(0);
        final Background result;
        if (backNode.getNodeType() == Node.ELEMENT_NODE) {
            result = new XmlBackground((Element) backNode).generateGameEntity();
        } else {
            throw new SAXException("Illegal background node - " + backNode);
        }
        return result;
    }

    private static Player loadPlayer(final Document doc) throws SAXException {
        final Node playerNode = doc.getElementsByTagName(NODE_PLAYER).item(0);
        final Player result;
        if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
            result = new XmlPlayer((Element) playerNode).generateGameEntity();
        } else {
            throw new SAXException("Illegal player node - " + playerNode);
        }
        return result;
    }

    private static Set<Model> loadItems(final Document doc) throws SAXException {
        final Node itemsNode = doc.getElementsByTagName(NODE_ITEMS).item(0);
        final Set<Model> result = new HashSet<>();
        Element el;
        Model item;
        if (itemsNode != null && itemsNode.getNodeType() == Node.ELEMENT_NODE) {
            el = (Element) itemsNode;
            NodeList nl = el.getElementsByTagName(NODE_ITEM);
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    el = (Element) nl.item(i);
                    item = new XmlModel(el).generateGameEntity();
                    item.setId(el.getAttribute(ATTR_ID));
                    result.add(item);
                }
            }
        }
        return result;
    }

    private static Set<Light> loadLights(final Document doc) {
        final Set<Light> result = new LinkedHashSet<>();

        final Node lightsNode = doc.getElementsByTagName(NODE_LIGHTS).item(0);
        final NodeList lights = lightsNode.getChildNodes();
        Node node;
        Element el;
        Light l;
        for (int i = 0; i < lights.getLength(); i++) {
            node = lights.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                el = (Element) node;
                l = new XmlLight(el).generateGameEntity();
                l.setId(el.getAttribute(ATTR_ID));
                result.add(l);
            }
        }

        return result;
    }

    private static Set<TriggerStub> loadTriggers(final Document doc) throws SAXException {
        final Set<TriggerStub> result = new HashSet<>();
        final Node itemsNode = doc.getElementsByTagName(NODE_ACTION_ITEMS).item(0);
        Node node;
        Element el;
        if (itemsNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList nl = itemsNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    el = (Element) node;
                    result.add(new XmlTrigger(el).generateGameEntity());
                }
            }
        }
        return result;
    }

    private static Quest loadQuest(final Document doc) throws SAXException {
        Quest result = null;
        final Node itemsNode = doc.getElementsByTagName(NODE_QUEST).item(0);
        Node node;
        Element el;        
        if (itemsNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList nl = itemsNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    el = (Element) node;
                    result = new XmlQuest(el).generateGameEntity();
                    result.setId(el.getAttribute(ATTR_ID));                    
                }
            }
        }
        return result;
    }

    private static Set<Item> loadDictionary(final Document doc, final String nodeName) {
        final Set<Item> result = new HashSet<>();
        try {
            final Element dict = (Element) doc.getElementsByTagName(nodeName).item(0);
            final String id = dict.getAttribute(ATTR_ID);
            final NodeList nl = dict.getElementsByTagName(NODE_ITEM);
            Node n, n2;
            NodeList nlSub;
            Item it;
            for (int i = 0; i < nl.getLength(); i++) {
                n = nl.item(i);
                if (n.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    nlSub = n.getChildNodes();
                    it = new Item();
                    it.setId(((Element) n).getAttribute(ATTR_ID));
                    for (int j = 0; j < nlSub.getLength(); j++) {
                        n2 = nlSub.item(j);
                        if (n2.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            it.setParam(n2.getNodeName(), n2.getTextContent());
                        }
                    }
                    it.setParam(ItemParameters.SOURCE, id);
                    result.add(it);
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.err);
        }

        return result;
    }
}
