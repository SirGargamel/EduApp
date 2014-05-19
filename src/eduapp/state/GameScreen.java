package eduapp.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eduapp.Actions;
import eduapp.AppContext;
import eduapp.PlayerAvatar;
import eduapp.level.Level;

/**
 *
 * @author Petr Ječmen
 */
public class GameScreen extends AbstractAppState {

    private final Vector3f LEFT = new Vector3f(-1, 0, 0);
    private final Vector3f UP = new Vector3f(0, 0, -1);
    private final Vector3f walkDirection = new Vector3f();
    private final Vector3f viewDirection = new Vector3f();
    private String levelName;
    private BulletAppState bulletAppState;
    private BetterCharacterControl player;
    private PlayerAvatar playerAvatar;
    private RigidBodyControl landscape;
    private Level currentLevel;
    boolean left, right, up, down;
    private ActionListener keyListener;

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        System.out.println("Init GameScreen.");
        loadLevel(app);
    }

    private void loadLevel(Application app) {
        initPlayer(app.getAssetManager(), app.getInputManager());
        initWorld(app);
        currentLevel.getRootNode().attachChild(playerAvatar.getModel());

        initKeys(app.getInputManager());
        initCamera(app);

        initCollisions(app);
    }

    private void initCamera(Application app) {
        final ChaseCamera chaseCam = new ChaseCamera(app.getCamera(), playerAvatar.getModel(), app.getInputManager());
        chaseCam.setDefaultDistance(7.5f);
        chaseCam.setDefaultHorizontalRotation(90 * FastMath.DEG_TO_RAD);
        chaseCam.setDefaultVerticalRotation(89.9f * FastMath.DEG_TO_RAD);
    }

    private void initPlayer(final AssetManager assetManager, final InputManager inputManager) {
        playerAvatar = new PlayerAvatar(assetManager, inputManager);
    }

    private void initWorld(Application app) {
        app.getViewPort().setBackgroundColor(ColorRGBA.LightGray);

        currentLevel = Level.loadLevel(levelName, app.getAssetManager());
        ((SimpleApplication) app).getRootNode().attachChild(currentLevel.getRootNode());
    }

    private void initCollisions(Application app) {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        app.getStateManager().attach(bulletAppState);
//        bulletAppState.setDebugEnabled(true);        

        final CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentLevel.getWorldNode());
        landscape = new RigidBodyControl(sceneShape, 0);

        player = new BetterCharacterControl(.15f, 1.5f, 50f);
        playerAvatar.getModel().addControl(player);

        player.setJumpForce(new Vector3f(0, 250f, 0));
        player.warp(currentLevel.getInitialPlayerPos());
        player.setViewDirection(Vector3f.UNIT_Z);

        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);

        CollisionShape cs;
        RigidBodyControl rbc;
        for (Spatial s : currentLevel.getItems()) {
            cs = CollisionShapeFactory.createBoxShape((Node) s);
            rbc = new RigidBodyControl(cs, 0);
            s.addControl(rbc);
            bulletAppState.getPhysicsSpace().add(rbc);
        }
    }

    private void initKeys(final InputManager inputManager) {
        // You can map one or several inputs to one named action        
        inputManager.addMapping(Actions.PAUSE.toString(), new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping(Actions.LEFT.toString(), new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping(Actions.RIGHT.toString(), new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping(Actions.UP.toString(), new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping(Actions.DOWN.toString(), new KeyTrigger(KeyInput.KEY_K));
        // Add the names to the action listener.
        keyListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(Actions.PAUSE.toString()) && !isPressed) {
                    setEnabled(!isEnabled());
                }
                if (name.equals(Actions.LEFT.toString())) {
                    left = isPressed;
                } else if (name.equals(Actions.RIGHT.toString())) {
                    right = isPressed;
                } else if (name.equals(Actions.UP.toString())) {
                    up = isPressed;
                } else if (name.equals(Actions.DOWN.toString())) {
                    down = isPressed;
                }
            }
        };
        inputManager.addListener(keyListener, Actions.PAUSE.toString());
        inputManager.addListener(keyListener, Actions.LEFT.toString());
        inputManager.addListener(keyListener, Actions.RIGHT.toString());
        inputManager.addListener(keyListener, Actions.UP.toString());
        inputManager.addListener(keyListener, Actions.DOWN.toString());

    }

    @Override
    public void update(float tpf) {
        boolean upd = false;
        walkDirection.set(0, 0, 0);
        viewDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(LEFT);
            viewDirection.addLocal(LEFT.negate());
            upd = true;
        }
        if (right) {
            walkDirection.addLocal(LEFT.negate());
            viewDirection.addLocal(LEFT);
            upd = true;
        }
        if (up) {
            upd = true;
            walkDirection.addLocal(UP);
            viewDirection.addLocal(UP.negate());
        }
        if (down) {
            upd = true;
            walkDirection.addLocal(UP.negate());
            viewDirection.addLocal(UP);
        }
        if (upd) {
            player.setViewDirection(viewDirection);
        }
        player.setWalkDirection(walkDirection);
        final Vector3f p = playerAvatar.getModel().getWorldBound().getCenter();
        currentLevel.visit(p);
//        System.out.println(p); 
    }

    @Override
    public void cleanup() {
        super.cleanup();
        AppContext.getApp().getInputManager().clearMappings();
        AppContext.getApp().getRootNode().detachAllChildren();
        AppContext.getApp().getRootNode().getWorldLightList().clear();
        playerAvatar = null;
        player = null;
        bulletAppState = null;
        landscape = null;
        currentLevel = null;
    }
}
