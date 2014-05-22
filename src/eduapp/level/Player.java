package eduapp.level;

import com.jme3.math.Vector3f;

/**
 *
 * @author Petr Jecmen
 */
public class Player extends Item {

    private static final String ID = "player";
    final Vector3f initialPosition;
    final String modelName;

    public Player(Vector3f initialPosition, String modelName) {
        this.initialPosition = initialPosition;
        this.modelName = modelName;
        setId(ID);
    }

    public Vector3f getInitialPosition() {
        return initialPosition;
    }

    public String getModelName() {
        return modelName;
    }
}
