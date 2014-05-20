package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.ColorRGBA;
import eduapp.level.Light;


public class LightActionTrigger extends ActionTrigger<Light> {       

    @Override
    public void onActivate() {
        if (action.equals("Switch")) {
            if (target.getLight().getColor().a == 0) {
                target.getLight().setColor(target.getColor());
            } else {
                target.getLight().setColor(ColorRGBA.BlackNoAlpha);
            }
        }
    }

    public LightActionTrigger(BoundingVolume volume, Light target, String action, boolean once) {
        super(volume, target, action, once);        
    }
    
}
