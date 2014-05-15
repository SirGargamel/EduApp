package eduapp;

import eduapp.level.xml.LevelLoader;
import eduapp.level.Level;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Petr Jecmen
 */
public class TestScene extends SimpleApplication {

    private static final float PLAYER_SPEED = 1.0f;
    private final Vector3f LEFT = new Vector3f(-1, 0, 0);
    private final Vector3f UP = new Vector3f(0, 0, -1);
    private BulletAppState bulletAppState;
    private BetterCharacterControl player;
    private PlayerAvatar playerAvatar;
    private RigidBodyControl landscape;
    private final Vector3f walkDirection = new Vector3f();
    private final Vector3f viewDirection = new Vector3f();
    boolean left, right, up, down;
    private boolean isRunning = true;
    private Level currentLevel;

    @Override
    public void simpleInitApp() {
        assetManager.registerLoader(LevelLoader.class, Level.LEVEL_FILE_EXTENSION);

        initPlayer();
        initWorld();
        currentLevel.getRootNode().attachChild(playerAvatar.getModel());

        initKeys();
        initCamera();

        initCollisions();

        speed = PLAYER_SPEED;
    }

    private void initCamera() {
        flyCam.setEnabled(false);
        final ChaseCamera chaseCam = new ChaseCamera(cam, playerAvatar.getModel(), inputManager);
//        chaseCam.setTrailingEnabled(true);
//        chaseCam.setDownRotateOnCloseViewOnly(false);
//        chaseCam.setSmoothMotion(true);
        chaseCam.setDefaultDistance(7.5f);
//        chaseCam.setMaxDistance(15.5f);
//        chaseCam.setDragToRotate(true);
        chaseCam.setDefaultHorizontalRotation(90 * FastMath.DEG_TO_RAD);
        chaseCam.setDefaultVerticalRotation(89.9f * FastMath.DEG_TO_RAD);
    }

    private void initPlayer() {
        playerAvatar = new PlayerAvatar(this);
    }

    private void initWorld() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        currentLevel = Level.loadLevel("egypt", assetManager);
        rootNode.attachChild(currentLevel.getRootNode());
    }

    private void initCollisions() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
//        bulletAppState.setDebugEnabled(true);        

        final CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(currentLevel.getWorldNode());
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

    private void initKeys() {
        // You can map one or several inputs to one named action        
        inputManager.addMapping(Actions.PAUSE.toString(), new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping(Actions.LEFT.toString(), new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping(Actions.RIGHT.toString(), new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping(Actions.UP.toString(), new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping(Actions.DOWN.toString(), new KeyTrigger(KeyInput.KEY_K));
        // Add the names to the action listener.
        final ActionListener actionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(Actions.PAUSE.toString()) && !isPressed) {
                    isRunning = !isRunning;
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
        inputManager.addListener(actionListener, Actions.PAUSE.toString());
        inputManager.addListener(actionListener, Actions.LEFT.toString());
        inputManager.addListener(actionListener, Actions.RIGHT.toString());
        inputManager.addListener(actionListener, Actions.UP.toString());
        inputManager.addListener(actionListener, Actions.DOWN.toString());

    }

    @Override
    public void simpleUpdate(float tpf) {
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
        final Vector3f p = playerAvatar.getModel().getWorldTranslation();
        currentLevel.visit(p);
//        System.out.println(p);        
    }

}
