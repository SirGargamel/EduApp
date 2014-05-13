package eduapp.level;

import com.jme3.math.Vector3f;

/**
 *
 * @author Petr Jecmen
 */
public class Player {
    final Vector3f initialPosition;

    public static Player buildPlayer(final String initialPosition) {
        final String[] coords = initialPosition.split(";");
        if (coords.length != 2) {
            throw new IllegalArgumentException("Initial position must be 2D - " + initialPosition);
        }
        return new Player(new Vector3f(Float.valueOf(coords[0]), 0, Float.valueOf(coords[1])));
    }

    private Player(Vector3f initialPosition) {
        this.initialPosition = initialPosition;
    }
    
}
