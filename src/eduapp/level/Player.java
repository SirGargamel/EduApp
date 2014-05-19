package eduapp.level;

import com.jme3.math.Vector3f;

/**
 *
 * @author Petr Jecmen
 */
public class Player extends ActionItem {

    public static final String ID = "player";
    final Vector3f initialPosition;

    public Player(Vector3f initialPosition) {
        this.initialPosition = initialPosition;
        setId(ID);
    }

    @Override
    public void performActionEnter(String action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void performActionLeave(String action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
