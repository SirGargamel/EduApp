package eduapp;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Jecmen
 */
public class PlayerAvatar implements AnimEventListener {

    private static final float PLAYER_HEIGHT = 1.5f;    
    private final Spatial model;
    private final AnimChannel channel;
    private final AnimControl control;
    private boolean isRunning;

    public PlayerAvatar(final SimpleApplication app) {
        model = app.getAssetManager().loadModel("models/ninja/Ninja.mesh.xml");
        final BoundingBox plB = (BoundingBox) model.getWorldBound();
        final float playerScale = PLAYER_HEIGHT / (plB.getYExtent() * 2.0f);
        model.scale(playerScale, playerScale, playerScale);          
        

        control = model.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim("Idle1");

        final ActionListener actionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    if (name.equals(Actions.PAUSE.toString())) {
                        isRunning = !isRunning;
                    } else if (name.equals(Actions.LEFT.toString())
                            || name.equals(Actions.RIGHT.toString())) {
                        channel.setTime(channel.getAnimMaxTime());
                    }
                } else {
                    if (name.equals(Actions.JUMP.toString())) {
                        channel.setAnim("Jump", 0.50f);
                        channel.setLoopMode(LoopMode.DontLoop);
                        channel.setSpeed(1f);
                    }
                }
            }
        };
        app.getInputManager().addListener(actionListener, Actions.PAUSE.toString(), Actions.LEFT.toString(), Actions.RIGHT.toString(), Actions.JUMP.toString());
        final AnalogListener analogListener = new AnalogListener() {

            @Override
            public void onAnalog(String name, float value, float tpf) {
                if (isRunning) {
                    if (name.equals(Actions.LEFT.toString()) || name.equals(Actions.RIGHT.toString())) {                        
                        if (!channel.getAnimationName().equals("Walk")) {
                            channel.setAnim("Walk", 0.50f);
                            channel.setLoopMode(LoopMode.Loop);
                        }
                    }
                }
            }
        };
        app.getInputManager().addListener(analogListener, Actions.LEFT.toString(), Actions.RIGHT.toString());

        isRunning = true;
    }

    public Spatial getModel() {
        return model;
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        channel.setAnim("Idle1", 0.50f);
        channel.setLoopMode(LoopMode.DontLoop);
        channel.setSpeed(1f);
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }

}
