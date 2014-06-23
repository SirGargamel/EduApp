package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import eduapp.level.quest.Quest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Petr Jecmen
 */
public class Player extends Item {

    private static final float PLAYER_HEIGHT = 0.50f;
    private static final float PLAYER_HEIGHT_MAX = 0.50f;
    private static final float PLAYER_HEIGHT_MIN = 0.25f;
    private static final String ID = "player";
    private final Vector3f initialPosition;
    private final String modelName;
    private Spatial model;
    private boolean isRunning, scaleUp;
    private Quest currentQuest;
    private final List<String> inventory;

    public Player(Vector3f initialPosition, String modelName) {
        inventory = new ArrayList<>();
        scaleUp = true;

        this.initialPosition = initialPosition;
        this.modelName = modelName;
        setId(ID);
    }

    public void generatePlayer(final AssetManager assetManager, final InputManager inputManager) {
        final StringBuilder sb = new StringBuilder();
        sb.append("models/");
        sb.append(modelName);
        sb.append(".j3o");
        model = assetManager.loadModel(sb.toString());
        final BoundingBox plB = (BoundingBox) model.getWorldBound();
        final float playerScale = PLAYER_HEIGHT / (plB.getYExtent() * 2.0f);
        model.scale(playerScale);

        isRunning = true;
    }

    public Spatial getModel() {
        return model;
    }

    public void setEnabled(boolean enabled) {        
        this.isRunning = enabled;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
    }

    public void addItemToInventory(final String itemId) {
        inventory.add(itemId);

        setChanged();
        notifyObservers();
    }

    public List<String> getAllItems() {
        return inventory;
    }

    public Vector3f getInitialPosition() {
        return initialPosition;
    }

    public String getModelName() {
        return modelName;
    }
    
    public void update(float tpf) {
        if (isRunning) {
            final float scale = model.getWorldScale().x;
            float val = 0;
            if (scaleUp) {
                if (scale < PLAYER_HEIGHT_MAX) {
                    val += tpf;
                } else {
                    scaleUp = false;
                }
            } else {
                if (scale > PLAYER_HEIGHT_MIN) {
                    val -= tpf;
                } else {
                    scaleUp = true;
                }
            }
            model.scale(1.0f + val);
        }
    }
}
