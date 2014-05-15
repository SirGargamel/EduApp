package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Jecmen
 */
public class Item extends ActionItem {

    private final String modelName;
    private final Vector3f position;
    private final float scale;
    private Spatial model;

    public Item(String modelName, Vector3f position, float scale) {
        this.modelName = modelName;
        this.position = position;
        this.scale = scale;        
    }

    public Spatial generateItem(final AssetManager assetManager) {
        model = assetManager.loadModel("models/".concat(modelName));
        model.scale(scale);
        model.move(position);
        return model;
    }

    public Spatial getModel() {
        return model;
    }

    @Override
    public void preformAction(String action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
