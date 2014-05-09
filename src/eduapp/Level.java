package eduapp;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Jecmen
 */
public abstract class Level {

    protected static final float LEVEL_WIDTH = 50.0f;
    protected static final float LEVEL_HEIGHT = 3.5f;
    protected static final float FLOOR_DEPTH = 1.0f;
    protected static final float FLOOR_THICKNESS = 0.25f;
    protected final AssetManager assetManager;
    protected Node rootNode;
    protected final Vector3f translate;

    public Level(final AssetManager assetManager, final Vector3f translate) {
        this.assetManager = assetManager;
        this.translate = translate.clone();
        rootNode = new Node(this.getClass().getSimpleName());
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Vector3f getTranslate() {
        return translate;
    }
    
    public void attachChild(final Spatial item) {
        rootNode.attachChild(item);
    }

}
