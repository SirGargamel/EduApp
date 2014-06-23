package eduapp.level.xml;

import com.jme3.math.Vector3f;
import eduapp.level.Player;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Petr Jecmen
 */
public class XmlPlayer extends XmlEntity<Player> {

    private static final String NAME = "player";
    private static final String NODE_COORDS = "Coords";
    private static final String NODE_MODEL = "Model";

    public XmlPlayer(Element el) {
        super(el);
    }

    @Override
    public Player generateGameEntity() throws SAXException {
        final String initialPosition = element.getElementsByTagName(NODE_COORDS).item(0).getTextContent();
        final String[] coords = initialPosition.split(";");
        if (coords.length != 2) {
            throw new IllegalArgumentException("Initial position must be 2D - " + initialPosition);
        }
        final Player result = new Player(
                new Vector3f(Float.valueOf(coords[0]), 0, Float.valueOf(coords[1])),
                element.getElementsByTagName(NODE_MODEL).item(0).getTextContent());
        result.setId(NAME);
        return result;
    }
}
