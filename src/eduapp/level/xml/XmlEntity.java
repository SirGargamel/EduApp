package eduapp.level.xml;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Petr Jecmen
 * @param <T>
 */
public abstract class XmlEntity<T> {

    protected final Element element;

    public XmlEntity(Element element) {
        this.element = element;
    }

    public abstract T generateGameEntity() throws SAXException;
}
