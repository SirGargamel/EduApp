package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Jecmen
 */
public class Item {
    
    private final String modelName;
    private final String id;
    private final Vector3f position;
    private final float scale;
    
    public static Item generateItem(final String modelName, final String position, final String scale, final String id) {
        final String[] split = position.split(";");
        final Vector3f pos;
        if (split.length == 2) {
            pos = new Vector3f(Float.valueOf(split[0]), 0, Float.valueOf(split[1]));
        } else if (split.length == 3) {
            pos = new Vector3f(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
        } else {
            throw new IllegalArgumentException("Item position must be 2D or 3D - " + position);
        }
        
        final float scaleF = Float.valueOf(scale);
        
        return new Item(modelName, pos, scaleF, id);
    }

    private Item(String modelName, Vector3f position, float scale, String id) {
        this.modelName = modelName;
        this.position = position;
        this.scale = scale;
        this.id = id;
    }
    
    public Spatial generateItem(final AssetManager assetManager) {
        final Spatial result = assetManager.loadModel("models/".concat(modelName));
        result.scale(scale);
        result.move(position);
        result.setName(id);
        return result;
    }
}
