package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class Level {

    public static final String LEVEL_FILE_EXTENSION = "xml";
    public static final int TILE_SIZE = 1;
    private static final float[] TILE_NORMALS = {
        0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, // back
        1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, // right
        0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, // front
        -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, // left
        0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, // top
        0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0 // bottom
    };    
    private final Node rootNode;
    private final Background background;
    private final Player player;
    private final Set<Item> items;
    private final Set<Spatial> itemModels;

    public static Level loadLevel(final String levelName, final AssetManager assetManager) {
        final String path = "levels/".concat(levelName).concat(".").concat(LEVEL_FILE_EXTENSION);
        final Level result = (Level) assetManager.loadAsset(path);
        result.generateLevel(assetManager);
        return result;
    }

    Level(final Background background, final Player player, final Set<Item> items) {        
        this.background = background;
        this.player = player;
        this.items = items;
        itemModels = new HashSet<>();
        
        rootNode = new Node(this.getClass().getSimpleName());
    }

    private void generateLevel(final AssetManager assetManager) {
        background.generateBackground(assetManager);
        rootNode.attachChild(background.getRootNode());
        
        Spatial s;
        for (Item i : items) {
            s = i.generateItem(assetManager);
            itemModels.add(s);
            rootNode.attachChild(s);
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getWorldNode() {
        return background.getRootNode();
    }

    public Vector3f getInitialPlayerPos() {
        return player.initialPosition;
    }

    public void addItem(final Item item) {
        items.add(item);
    }

    public Set<Spatial> getItems() {
        return itemModels;
    }

    public void attachChild(final Spatial item) {
        rootNode.attachChild(item);
    }

    public void visitNode(final float x, final float y) {
        // TODO
    }



}
