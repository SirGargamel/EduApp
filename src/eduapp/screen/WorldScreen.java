package eduapp.screen;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eduapp.Actions;
import eduapp.AppContext;
import eduapp.EduApp;
import eduapp.FlowManager;
import eduapp.level.Player;
import eduapp.level.Level;

/**
 *
 * @author Petr Ječmen
 */
public class WorldScreen extends AbstractAppState {

    private static final float PLAYER_SPEED = 1.5f;
    private static final float CAM_ELEVATION = 7.5f;
    private static final Vector3f LEFT = new Vector3f(-1, 0, 0);
    private static final Vector3f UP = new Vector3f(0, 0, -1);
    private final Vector3f walkDirection = new Vector3f();
    private BulletAppState bulletAppState;
    private BetterCharacterControl playerPhysics;
    private Player player;
    private RigidBodyControl landscape;
    private Level currentLevel;
    private boolean left, right, up, down;
    private ActionListener keyListener;
    private Camera cam;

    public void setLevelName(String levelName) {
        currentLevel = Level.loadLevel(levelName, AppContext.getApp().getAssetManager(), AppContext.getApp().getInputManager());
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        prepareWorld(app);
    }

    private void prepareWorld(Application app) {
        if (player == null) {
            initPlayer();
            currentLevel.getRootNode().attachChild(player.getModel());
            initCollisions(app);
        } else {
            player.setEnabled(true);
        }
        ((SimpleApplication) app).getRootNode().attachChild(currentLevel.getRootNode());

        initKeys(app.getInputManager());
        initCamera(app);
    }

    private void initPlayer() {
        player = currentLevel.getPlayer();
        FlowManager.getInstance().assignPlayer(player);
    }

    private void initKeys(final InputManager inputManager) {
        keyListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    if (name.equals(Actions.PAUSE.toString())) {
                        FlowManager.getInstance().handlePause();
                    } else if (name.equals(Actions.QUEST.toString())) {
                        FlowManager.getInstance().questAction();
                    } else if (name.equals(Actions.ACTION.toString())) {
                        currentLevel.activate(player.getModel().getWorldBound().getCenter());
                    } else if (name.equals(Actions.DICTIONARY.toString())) {
                        FlowManager.getInstance().dictionaryAction();
                    } else if (name.equals("Debug")) {
                        debugAction();
                    }
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
        inputManager.addListener(keyListener, Actions.ACTION.toString());
        inputManager.addListener(keyListener, Actions.QUEST.toString());
        inputManager.addListener(keyListener, Actions.DICTIONARY.toString());
    }

    private void initCamera(Application app) {
        SimpleApplication appS = (SimpleApplication) app;
        appS.getFlyByCamera().setEnabled(false);

        cam = app.getCamera();
        cam.setLocation(new Vector3f(0, CAM_ELEVATION, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z.negate());
    }

    private void initCollisions(Application app) {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        app.getStateManager().attach(bulletAppState);
        bulletAppState.setDebugEnabled(false);

        final CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentLevel.getWorldNode());
        landscape = new RigidBodyControl(sceneShape, 0);

        playerPhysics = new BetterCharacterControl(.25f, 1.5f, 50f);
        player.getModel().addControl(playerPhysics);

        playerPhysics.setJumpForce(new Vector3f(0, 250f, 0));
        playerPhysics.warp(currentLevel.getPlayer().getInitialPosition());
        playerPhysics.setViewDirection(Vector3f.UNIT_Z);

        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(playerPhysics);

        CollisionShape cs;
        RigidBodyControl rbc;
        for (Spatial s : currentLevel.getItems()) {
            cs = CollisionShapeFactory.createBoxShape((Node) s);
            rbc = new RigidBodyControl(cs, 0);
            s.addControl(rbc);
            bulletAppState.getPhysicsSpace().add(rbc);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (player != null) {
            player.setEnabled(enabled);
            playerPhysics.setWalkDirection(Vector3f.ZERO);
        }        
    }

    @Override
    public void update(float tpf) {
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(LEFT);
        }
        if (right) {
            walkDirection.addLocal(LEFT.negate());
        }
        if (up) {
            walkDirection.addLocal(UP);
        }
        if (down) {
            walkDirection.addLocal(UP.negate());
        }
        playerPhysics.setWalkDirection(walkDirection.normalizeLocal().multLocal(PLAYER_SPEED));
        final Vector3f loc = player.getModel().getWorldBound().getCenter();
        currentLevel.visit(loc);
        cam.setLocation(loc.add(0, CAM_ELEVATION, 0));        
        player.update(tpf);
        
        if (EduApp.DEBUG) {
            System.out.println(loc);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();

        final InputManager inputManager = AppContext.getApp().getInputManager();
        inputManager.removeListener(keyListener);

        currentLevel.getRootNode().removeFromParent();
    }

    public void debugAction() {
    }
}