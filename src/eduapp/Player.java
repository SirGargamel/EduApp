package eduapp;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Spatial;
import eduapp.level.quest.Quest;

/**
 *
 * @author Petr Jecmen
 */
public class Player implements AnimEventListener, ActionListener, AnalogListener {

    private static final float PLAYER_HEIGHT = 1.5f;
    private Spatial model;
    private String animationIdle, animationWalk;
    private final AnimChannel channel;
    private final AnimControl control;
    private boolean isRunning;
    private Quest currentQuest;

    public Player(final AssetManager assetManager, final InputManager inputManager, final String modelName) {
        final StringBuilder sb = new StringBuilder();
        sb.append("models/");
        sb.append(modelName);
        sb.append(".j3o");
        model = assetManager.loadModel(sb.toString());
        final BoundingBox plB = (BoundingBox) model.getWorldBound();
        final float playerScale = PLAYER_HEIGHT / (plB.getYExtent() * 2.0f);
        model.scale(playerScale);
        iniAnimations(modelName);

        control = model.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim(animationIdle);

        if (modelName.equals("Ninja")) {
            final Spatial backup = model;
            model = new com.jme3.scene.Node();
            ((com.jme3.scene.Node) model).attachChild(backup);
        }

        isRunning = true;
    }

    public void initKeys(final InputManager inputManager) {
        inputManager.addListener(
                this,
                Actions.PAUSE.toString(),
                Actions.LEFT.toString(),
                Actions.RIGHT.toString(),
                Actions.UP.toString(),
                Actions.DOWN.toString());
        inputManager.addListener(
                this,
                Actions.LEFT.toString(),
                Actions.RIGHT.toString(),
                Actions.UP.toString(),
                Actions.DOWN.toString());
    }
    
    public void removeKeys(final InputManager inputManager) {
        inputManager.removeListener(this);
    }

    private void iniAnimations(final String modelName) {
        switch (modelName) {
            case "Ninja":
                animationIdle = "Idle";
                animationWalk = "Walk";
                break;
            case "Goblin":
                animationIdle = "idleA";
                animationWalk = "walk";
                break;
            case "Oto":
                animationIdle = "stand";
                animationWalk = "Walk";
                break;
        }
    }

    public Spatial getModel() {
        return model;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        channel.setAnim(animationIdle, 0.50f);
        channel.setLoopMode(LoopMode.DontLoop);
        channel.setSpeed(1f);
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && isRunning) {
            if (name.equals(Actions.LEFT.toString())
                    || name.equals(Actions.RIGHT.toString())
                    || name.equals(Actions.UP.toString())
                    || name.equals(Actions.DOWN.toString())) {
                channel.setTime(channel.getAnimMaxTime());
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (isRunning) {
            if (name.equals(Actions.LEFT.toString())
                    || name.equals(Actions.RIGHT.toString())
                    || name.equals(Actions.UP.toString())
                    || name.equals(Actions.DOWN.toString())) {
                if (!channel.getAnimationName().equals(animationWalk)) {
                    channel.setAnim(animationWalk, 0.50f);
                    channel.setLoopMode(LoopMode.Loop);
                }
            }
        }
    }
}
