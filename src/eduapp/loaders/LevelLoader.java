package eduapp.loaders;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import eduapp.AppContext;
import eduapp.ItemRegistry;
import eduapp.level.Background;
import eduapp.level.Item;
import eduapp.level.Model;
import eduapp.level.Level;
import eduapp.level.Light;
import eduapp.level.Player;
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
    private static final String NODE_ACTION_ITEMS = "ActionItems";
    private static final String NODE_BACKGROUND = "Background";
    private static final String NODE_ITEMS = "Items";
    private static final String NODE_ITEM = "Item";
    private static final String NODE_LIGHTS = "Lights";
    private static final String NODE_PLAYER = "Player";
    private static final String NODE_QUEST = "Quests";

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
            final Set<Quest> quests = loadQuests(doc);                       
            
            result = new Level(background, player, items, lights, actionItems, quests);            
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

    private static Set<Quest> loadQuests(final Document doc) throws SAXException {
        final Set<Quest> result = new HashSet<>();
        final Node itemsNode = doc.getElementsByTagName(NODE_QUEST).item(0);
        Node node;
        Element el;
        Quest q;
        if (itemsNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList nl = itemsNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    el = (Element) node;
                    q = new XmlQuest(el).generateGameEntity();
                    q.setId(el.getAttribute(ATTR_ID));
                    result.add(q);
                }
            }
        }
        return result;
    }   
}
