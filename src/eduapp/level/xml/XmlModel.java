package eduapp.level.xml;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import eduapp.level.Model;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Petr Jecmen
 */
public class XmlModel extends XmlEntity<Model> {

    private static final String NODE_COORDS = "Pozice";
    private static final String NODE_NAME = "Model";
    private static final String NODE_SCALE = "Velikost";
    private static final String NODE_ROTATE = "Rotace";

    public XmlModel(Element el) {
        super(el);
    }

    @Override
    public Model generateGameEntity() {
        final String modelName = element.getElementsByTagName(NODE_NAME).item(0).getTextContent();

        final float scaleF;
        final Vector3f pos;
        final float[] rotate;

        NodeList nl = element.getElementsByTagName(NODE_COORDS);
        if (nl.getLength() > 0) {
            final String position = nl.item(0).getTextContent();
            final String[] split = position.split(";");
            if (split.length == 2) {
                pos = new Vector3f(Float.valueOf(split[0]), 0, Float.valueOf(split[1]));
            } else if (split.length == 3) {
                pos = new Vector3f(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
            } else {
                throw new IllegalArgumentException("Item position must be 2D or 3D - " + position);
            }
        } else {
            pos = Vector3f.ZERO;
        }

        nl = element.getElementsByTagName(NODE_SCALE);
        if (nl.getLength() > 0) {
            scaleF = Float.valueOf(nl.item(0).getTextContent());
        } else {
            scaleF = 1;
        }

        nl = element.getElementsByTagName(NODE_ROTATE);
        if (nl.getLength() > 0) {
            final String rotation = nl.item(0).getTextContent();
            final String[] split = rotation.split(";");
            rotate = new float[]{
                Float.valueOf(split[0]) * FastMath.DEG_TO_RAD,
                Float.valueOf(split[1]) * FastMath.DEG_TO_RAD,
                Float.valueOf(split[2]) * FastMath.DEG_TO_RAD};
        } else {
            rotate = new float[]{0, 0, 0};
        }

        return new Model(modelName, pos, scaleF, rotate);
    }
}
