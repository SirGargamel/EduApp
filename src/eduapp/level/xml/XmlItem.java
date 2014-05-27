package eduapp.level.xml;

import com.jme3.math.Vector3f;
import eduapp.level.Model;
import org.w3c.dom.Element;

/**
 *
 * @author Petr Jecmen
 */
public class XmlItem extends XmlEntity<Model> {

    private static final String NODE_COORDS = "Coords";
    private static final String NODE_NAME = "Name";
    private static final String NODE_SCALE = "Scale";

    public XmlItem(Element el) {
        super(el);
    }

    @Override
    public Model generateGameEntity() {
        final String modelName = element.getElementsByTagName(NODE_NAME).item(0).getTextContent();
        final String position = element.getElementsByTagName(NODE_COORDS).item(0).getTextContent();

        final String[] split = position.split(";");
        final Vector3f pos;
        if (split.length == 2) {
            pos = new Vector3f(Float.valueOf(split[0]), 0, Float.valueOf(split[1]));
        } else if (split.length == 3) {
            pos = new Vector3f(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
        } else {
            throw new IllegalArgumentException("Item position must be 2D or 3D - " + position);
        }
        final float scaleF = Float.valueOf(element.getElementsByTagName(NODE_SCALE).item(0).getTextContent());

        return new Model(modelName, pos, scaleF);
    }
}
