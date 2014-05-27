package eduapp.level.trigger;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import eduapp.level.Light;

public class LightMoveTrigger extends MoveTrigger<Light> {

    @Override
    public void onEnter() {
        if (action.equals("Switch")) {
            target.enableLight(true);
        }
    }

    @Override
    public void onLeave() {
        if (action.equals("Switch")) {
            target.enableLight(false);
        }
    }

    public LightMoveTrigger(Spatial volume, Light target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);
    }
}
