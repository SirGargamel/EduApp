package eduapp.level.xml;

import eduapp.loaders.LevelLoader;
import eduapp.level.trigger.TriggerStub;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlTrigger extends XmlEntity<TriggerStub> {

    private static final String NODE_ACTION = "Action";
    private static final String NODE_OFF = "Off";
    private static final String NODE_ONCE = "Once";
    private static final String NODE_TARGET = "Target";
    private static final String NODE_VOLUME = "Volume";

    public XmlTrigger(Element node) {
        super(node);
    }

    @Override
    public TriggerStub generateGameEntity() {
        final NodeList nl = element.getElementsByTagName(NODE_TARGET);
        final String target;
        if (nl.getLength() == 0) {
            target = null;
        } else {
            target = nl.item(0).getTextContent();
        }

        return new TriggerStub(
                element.getNodeName(),
                element.getAttribute(LevelLoader.ATTR_ID),
                element.getElementsByTagName(NODE_VOLUME).item(0).getTextContent(),
                target,
                element.getElementsByTagName(NODE_ACTION).item(0).getTextContent(),
                element.getElementsByTagName(NODE_ONCE).getLength() > 0,
                element.getElementsByTagName(NODE_OFF).getLength() == 0);
    }
}
