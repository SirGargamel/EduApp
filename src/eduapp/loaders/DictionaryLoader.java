package eduapp.loaders;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import eduapp.level.ItemParameters;
import eduapp.level.Item;
import java.io.IOException;
import java.util.HashSet;
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
public class DictionaryLoader implements AssetLoader {

    public static final String EXTENSION = "dic";
    private static final String ATTR_ID = "id";
    private static final String NODE_DICT = "dict";
    private static final String NODE_ITEM = "item";

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        final Set<Item> result = new HashSet<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(assetInfo.openStream());
            doc.getDocumentElement().normalize();

            final Element dict = (Element) doc.getElementsByTagName(NODE_DICT).item(0);
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
        } catch (NullPointerException | ParserConfigurationException | SAXException ex) {
            ex.printStackTrace(System.err);
        }

        return result;
    }
}
