package eduapp.level.trigger;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Ječmen
 */
public abstract class ActionTrigger<T> extends Trigger<T> {

    private static final ColorRGBA DEFAULT_COLOR = ColorRGBA.Green;
    private static final float DEFAULT_INTENSITY = 3;
    private final PointLight light;

    public ActionTrigger(Spatial volume, T target, String action, boolean once, boolean active) {
        super(volume, target, action, once, active);

        final BoundingVolume bv = volume.getWorldBound();
        final float yExtent;
        if (bv instanceof BoundingSphere) {
            final BoundingSphere bs = (BoundingSphere) bv;
            yExtent = bs.getRadius();
        } else {
            final BoundingBox bb = (BoundingBox) bv;
            yExtent = bb.getYExtent();
        }
        final Vector3f newPos = bv.getCenter().add(0, yExtent, 0);
        
        light = new PointLight();
        light.setRadius(1);
        light.setColor(DEFAULT_COLOR.mult(DEFAULT_INTENSITY));
        light.setPosition(newPos);
    }

    public abstract void onActivate();

    public Light getLight() {
        return light;
    }
    
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            light.setColor(DEFAULT_COLOR.mult(DEFAULT_INTENSITY));
        } else {
            light.setColor(ColorRGBA.BlackNoAlpha);
        }
    }
    
    public String description() {
        return action;
    }
}
