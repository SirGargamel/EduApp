package eduapp.level.trigger;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import eduapp.level.Light;


public class LightMoveTrigger extends MoveTrigger<Light> {        

    @Override
    public void onEnter() {
        if (action.equals("Switch")) {
            target.getLight().setColor(target.getColor());
        }
    }

    @Override
    public void onLeave() {
        if (action.equals("Switch")) {
            target.getLight().setColor(ColorRGBA.BlackNoAlpha);
        }
    }

    public LightMoveTrigger(Spatial volume, Light target, String action, boolean once) {
        super(volume, target, action, once);                
    }
    
}
