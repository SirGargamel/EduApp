package eduapp.level.trigger;

import com.jme3.scene.Spatial;
import eduapp.level.Light;


public class LightActionTrigger extends ActionTrigger<Light> {       

    @Override
    public void onActivate() {
        if (action.equals("Switch")) {
            if (target.isLightEnabled()) {
                target.enableLight(false);
            } else {
                target.enableLight(true);
            }
        }
    }

    public LightActionTrigger(Spatial volume, Light target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);        
    }
    
}
