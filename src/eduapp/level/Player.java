package eduapp.level;

import eduapp.level.item.Item;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eduapp.EduApp;
import eduapp.level.quest.Quest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Petr Jecmen
 */
public class Player extends Item {

    private static final float PLAYER_HEIGHT = 0.75f;
    private static final float PLAYER_SCALE_LIM = 0.75f;
    private static final int LIMIT_COUNTER = 1000;
    private static final float LIMIT_SCALE_TPF = 0.1f;
    private static final float COEFF_ROTATE = 0.001f;
    private static final float COEFF_SCALE = 0.35f;
    private static final String SPLITTER = ";";
    private static final String ID = "player";
    private final Vector3f initialPosition;
    private final String modelName;
    private Spatial model, inner;
    private boolean isRunning, scaleUp;
    private Quest currentQuest;
    private final List<String> inventory;
    private Quaternion rot, target;
    private int counter;
    private float playerScale, currentScale;

    public Player(Vector3f initialPosition, String modelName) {
        inventory = new ArrayList<>();
        scaleUp = true;

        this.initialPosition = initialPosition;
        this.modelName = modelName;
        setId(ID);

        rot = new Quaternion();
        target = new Quaternion();

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
        playerScale = PLAYER_HEIGHT / (plB.getYExtent() * 2.0f);
        currentScale = playerScale;
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

    public void addItemToInventory(final String itemIds) {
        final String[] split = itemIds.split(SPLITTER);
        for (String s : split) {
            inventory.add(s.trim());
        }

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
        if (isRunning && tpf <= LIMIT_SCALE_TPF) {
            float val = 1.0f;
            if (scaleUp) {
                if (currentScale < playerScale / PLAYER_SCALE_LIM) {
                    val += tpf * COEFF_SCALE;
                } else {
                    scaleUp = false;
                }
            } else {
                if (currentScale > playerScale * PLAYER_SCALE_LIM) {
                    val -= tpf * COEFF_SCALE;
                } else {
                    scaleUp = true;
                }
            }
            model.scale(val);
            currentScale *= val;

            if (counter >= LIMIT_COUNTER) {
                final Random rnd = new Random();
                target = new Quaternion(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
                counter = 0;

                if (EduApp.DEBUG) {
                    System.out.println("New target rotation - " + target);
                }

            }
            rot.slerp(target, tpf * COEFF_ROTATE);
            inner.rotate(rot);
            counter++;
        }
    }
}
