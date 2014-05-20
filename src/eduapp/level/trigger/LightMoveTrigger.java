package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.ColorRGBA;
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

    public LightMoveTrigger(BoundingVolume volume, Light target, String action, boolean once) {
        super(volume, target, action, once);                
    }
    
}
