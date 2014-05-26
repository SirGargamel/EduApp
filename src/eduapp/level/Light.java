package eduapp.level;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.Map;

/**
 *
 * @author Petr Jecmen
 */
public final class Light extends Item {

    private static final float DEFAULT_ANGLE_INNER = 10.0f;
    private static final float DEFAULT_ANGLE_OUTER = 15.0f;
    private static final ColorRGBA DEFAULT_COLOR = ColorRGBA.White;
    private static final Vector3f DEFAULT_DIRECTION = Vector3f.UNIT_Y.negate();
    private static final float DEFAULT_HEIGHT = 2.5f;
    private static final int DEFAULT_RANGE = 10;
    private static final String PARAM_COLOR = "Color";
    private static final String PARAM_DIRECTION = "Dir";
    private static final String PARAM_ANGLE_INNER = "InnerAngle";
    private static final String PARAM_ANGLE_OUTER = "OuterAngle";
    private static final String PARAM_INTENSITY = "Intensity";
    private static final String PARAM_OFF = "Off";
    private static final String PARAM_POSITION = "Coords";
    private static final String PARAM_RANGE = "Range";
    private static final String SPLIT = ";";
    private final com.jme3.light.Light light;
    private String pos;
    private ColorRGBA color;

    public Light(String type, Map<String, String> params) {
        pos = null;
        light = generateLight(type, params);
    }

    public com.jme3.light.Light getLight() {
        return light;
    }

    private com.jme3.light.Light generateLight(String type, Map<String, String> params) {
        com.jme3.light.Light result;
        switch (type) {
            case "Ambient":
                result = generateAmbientLight();
                break;
            case "Directional":
                result = generateDirectionalLight(params);
                break;
            case "Point":
                result = generatePointLight(params);
                break;
            case "Spot":
                result = generateSpotLight(params);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type of light - " + type);
        }

        if (params.containsKey(PARAM_COLOR)) {
            try {
                final String name = params.get(PARAM_COLOR);
                color = ((ColorRGBA) ColorRGBA.class.getDeclaredField(name).get(name)).clone();
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                System.err.println("Illegal light color - " + ex.getLocalizedMessage());
            }
        } else {
            color = DEFAULT_COLOR.clone();
        }
        if (params.containsKey(PARAM_INTENSITY)) {
            final float intensity = Float.valueOf(params.get(PARAM_INTENSITY));
            color.multLocal(intensity);
        }
        if (params.containsKey(PARAM_OFF)) {
            result.setColor(ColorRGBA.BlackNoAlpha);
        } else {
            result.setColor(color);
        }
        return result;
    }

    private static com.jme3.light.Light generateAmbientLight() {
        return new AmbientLight();
    }

    private static com.jme3.light.Light generateDirectionalLight(final Map<String, String> params) {
        final DirectionalLight result = new DirectionalLight();

        if (params.containsKey(PARAM_DIRECTION)) {
            result.setDirection(generateDirection(params.get(PARAM_DIRECTION)).normalizeLocal());
        } else {
            result.setDirection(DEFAULT_DIRECTION);
        }

        return result;
    }

    private static Vector3f generateDirection(final String coords) {
        final Vector3f result;
        final String split[] = coords.split(SPLIT);
        if (split.length == 2) {
            result = new Vector3f(Float.valueOf(split[0]), DEFAULT_HEIGHT, Float.valueOf(split[1]));
        } else if (split.length == 3) {
            result = new Vector3f(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
        } else {
            throw new IllegalArgumentException("Coords must be 2D or 3D separated by \';\' - " + coords);
        }

        return result;
    }

    private com.jme3.light.Light generateSpotLight(final Map<String, String> params) {
        final SpotLight result = new SpotLight();
        if (params.containsKey(PARAM_POSITION)) {
            result.setPosition(generatePosition(params.get(PARAM_POSITION)));
        } else {
            System.err.println("No position set for light.");
        }
        if (params.containsKey(PARAM_DIRECTION)) {
            result.setDirection(generatePosition(params.get(PARAM_DIRECTION)));
        } else {
            result.setDirection(DEFAULT_DIRECTION);
        }
        if (params.containsKey(PARAM_RANGE)) {
            result.setSpotRange(Float.valueOf(params.get(PARAM_RANGE)));
        } else {
            result.setSpotRange(DEFAULT_RANGE);
        }
        if (params.containsKey(PARAM_ANGLE_INNER)) {
            result.setSpotInnerAngle(Float.valueOf(params.get(PARAM_ANGLE_INNER)));
        } else {
            result.setSpotInnerAngle(DEFAULT_ANGLE_INNER);
        }
        if (params.containsKey(PARAM_ANGLE_OUTER)) {
            result.setSpotOuterAngle(Float.valueOf(params.get(PARAM_ANGLE_OUTER)));
        } else {
            result.setSpotInnerAngle(DEFAULT_ANGLE_OUTER);
        }

        return result;
    }

    private com.jme3.light.Light generatePointLight(final Map<String, String> params) {
        final PointLight result = new PointLight();

        if (params.containsKey(PARAM_POSITION)) {
            result.setPosition(generatePosition(params.get(PARAM_POSITION)));
        } else {
            System.err.println("No position set for light.");
        }
        if (params.containsKey(PARAM_RANGE)) {
            result.setRadius(Float.valueOf(params.get(PARAM_RANGE)));
        } else {
            result.setRadius(DEFAULT_RANGE);
        }

        return result;
    }

    private Vector3f generatePosition(final String coords) {
        final Vector3f result;
        if (coords.contains(SPLIT)) {
            final String split[] = coords.split(SPLIT);
            if (split.length == 2) {
                result = new Vector3f(Float.valueOf(split[0]), DEFAULT_HEIGHT, Float.valueOf(split[1]));
            } else if (split.length == 3) {
                result = new Vector3f(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
            } else {
                throw new IllegalArgumentException("Coords must be 2D or 3D separated by \';\' - " + coords);
            }
        } else {
            result = Vector3f.NAN;
            pos = coords;
        }
        return result;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void actualizePos(final ItemRegistry reg) {
        if (pos != null) {
            if (light instanceof SpotLight) {
                final SpotLight l = (SpotLight) light;
                final Item i = reg.get(pos);
                if (i != null && i instanceof Model) {
                    Model m = (Model) i;
                    l.setPosition(m.getModel().getWorldBound().getCenter());
                } else {
                    System.err.println("Illegal target for light - " + pos);
                }
            } else if (light instanceof PointLight) {
                PointLight l = (PointLight) light;
                final Item i = reg.get(pos);
                if (i != null && i instanceof Model) {
                    Model m = (Model) i;
                    l.setPosition(m.getModel().getWorldBound().getCenter().add(0,0.5f,0));
                } else {
                    System.err.println("Illegal target for light - " + pos);
                }
            }
        }
    }
}
