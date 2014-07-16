package eduapp.level;

import eduapp.level.item.Item;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eduapp.level.quest.Quest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Petr Jecmen
 */
public class Player extends Item {

    private static final float PLAYER_HEIGHT = 0.60f;
    private static final float PLAYER_HEIGHT_MAX = 0.30f;
    private static final float PLAYER_HEIGHT_MIN = 0.20f;
    private static final int LIMIT_COUNTER = 1000;
    private static final float COEFF_ROTATE = 0.001f;
    private static final float COEFF_SCALE = 0.5f;
    private static final String ID = "player";
    private final Vector3f initialPosition;
    private final String modelName;
    private Spatial model, inner;
    private boolean isRunning, scaleUp;
    private Quest currentQuest;
    private final List<String> inventory;
    private Quaternion rot, add;
    private int counter;

    public Player(Vector3f initialPosition, String modelName) {
        inventory = new ArrayList<>();
        scaleUp = true;

        this.initialPosition = initialPosition;
        this.modelName = modelName;
        setId(ID);

        rot = new Quaternion();
        add = new Quaternion();
        
        counter = LIMIT_COUNTER;
    }

    public void generatePlayer(final AssetManager assetManager, final InputManager inputManager) {
        final StringBuilder sb = new StringBuilder();
        sb.append("models/");
        sb.append(modelName);
        sb.append(".j3o");
        
        final Node n = new Node("player");        
        inner = assetManager.loadModel(sb.toString());
        n.attachChild(inner);        
        model = n;
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
                    val += tpf * COEFF_SCALE;
                } else {
                    scaleUp = false;
                }
            } else {
                if (scale > PLAYER_HEIGHT_MIN) {
                    val -= tpf * COEFF_SCALE;
                } else {
                    scaleUp = true;
                }
            }
            model.scale(1.0f + val);

            if (counter >= LIMIT_COUNTER) {
                final Random rnd = new Random();
                add = new Quaternion(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
                System.out.println("New add - " + add);
                counter = 0;
            }
            rot.slerp(add, tpf * COEFF_ROTATE);
            System.out.println(rot);
            inner.rotate(rot);            
            counter++;
        }
    }
}
