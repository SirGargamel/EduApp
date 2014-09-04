package eduapp.loaders;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetNotFoundException;
import eduapp.level.Player;
import eduapp.level.Background;
import eduapp.level.Model;
import eduapp.level.Level;
import eduapp.level.Light;
import eduapp.level.item.Item;
import eduapp.level.item.ItemParameter;
import eduapp.level.quest.Quest;
import eduapp.level.trigger.TriggerStub;
import eduapp.level.xml.XmlBackground;
import eduapp.level.xml.XmlLight;
import eduapp.level.xml.XmlModel;
import eduapp.level.xml.XmlPlayer;
import eduapp.level.xml.XmlQuest;
import eduapp.level.xml.XmlTrigger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class StateLoader implements AssetLoader {

    public static final String EXTENSION = "lvl";

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        int result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(assetInfo.openStream()))) {
            result = in.read();
        }

        return result;
    }
}
