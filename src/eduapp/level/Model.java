package eduapp.level;

import eduapp.level.item.Item;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.LightNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;

/**
 *
 * @author Petr Jecmen
 */
public class Model extends Item {

    private final String modelName;
    private final Vector3f position;
    private final float scale;
    private final float[] rotate;
    private Spatial model;

    public Model(String modelName, Vector3f position, float scale, float[] rotate) {
        this.modelName = modelName;
        this.position = position;
        this.scale = scale;
        this.rotate = rotate;
    }

    public Spatial generateItem(final AssetManager assetManager) {
        model = assetManager.loadModel("models/".concat(modelName));
        model.scale(scale);
        model.move(position);
        model.rotate(rotate[0], rotate[1], rotate[2]);
        
        final Node n = (Node) model;
        for (Spatial s : n.getChildren()) {
            if (s instanceof CameraNode || s instanceof LightNode) {
                n.detachChild(s);
            }
        }       
        
        return model;
    }

    public Spatial getModel() {
        return model;
    }
}
