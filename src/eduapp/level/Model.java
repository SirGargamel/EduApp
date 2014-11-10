package eduapp.level;

import eduapp.level.item.Item;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
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

    private static final String EXTENSION = ".j3o";
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
        String name;
        if (!modelName.endsWith(EXTENSION)) {
            name = modelName.concat(EXTENSION);
        } else {
            name = modelName;
        }

        try {
            model = assetManager.loadModel("models/".concat(name));
        } catch (AssetNotFoundException ex) {
            name = modelName.concat("/").concat(name);
            model = assetManager.loadModel("models/".concat(name));
        }


        model = GeometryBatchFactory.optimize((Node) model);
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
