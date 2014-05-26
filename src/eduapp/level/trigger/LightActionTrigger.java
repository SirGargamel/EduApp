package eduapp.level.trigger;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
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

    public LightActionTrigger(Spatial volume, Light target, String action, boolean once) {
        super(volume, target, action, once);        
    }
    
}
