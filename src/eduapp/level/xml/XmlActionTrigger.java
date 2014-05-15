package eduapp.level.xml;

import eduapp.level.ActionTrigger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlActionTrigger extends XmlEntity<ActionTrigger> {

    private static final String NODE_ACTION = "Action";
    private static final String NODE_ONCE = "Once";
    private static final String NODE_TARGET = "Target";
    private static final String NODE_VOLUME = "Volume";

    public XmlActionTrigger(Element node) {
        super(node);
    }

    @Override
    public ActionTrigger generateGameEntity() {
        final NodeList nl = element.getElementsByTagName(NODE_TARGET);
        final String target;
        if (nl.getLength() == 0) {
            target = null;
        } else {
            target = nl.item(0).getTextContent();
        }
        return new ActionTrigger(
                element.getElementsByTagName(NODE_VOLUME).item(0).getTextContent(),
                target,
                element.getElementsByTagName(NODE_ACTION).item(0).getTextContent(),
                element.getElementsByTagName(NODE_ONCE).getLength() > 0);
    }

}
