package eduapp.level;

import com.jme3.math.Vector3f;

/**
 *
 * @author Petr Jecmen
 */
public class Player extends Item {

    public static final String ID = "player";
    final Vector3f initialPosition;

    public Player(Vector3f initialPosition) {
        this.initialPosition = initialPosition;
        setId(ID);
    }
}
