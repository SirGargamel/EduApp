package eduapp;

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
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jogamp.newt.Window;
import java.awt.Desktop;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Jecmen
 */
public class TestScene extends SimpleApplication {

    private static final float PLAYER_POSITION_Z = 0.05f;
    private static final float PLAYER_SPEED = 1.0f;
    private final List<Level> levels;
    private ItemSpawner is;
    private BulletAppState bulletAppState;
    private BetterCharacterControl player;
    private PlayerAvatar playerAvatar;
    private RigidBodyControl landscape;
    private final Vector3f walkDirection = new Vector3f();
    private final Vector3f moveLeft = new Vector3f(-2.0f, 0, 0);
    boolean left, right;
    private boolean isRunning = true;
    private Node world;

    public TestScene() {
        levels = new LinkedList<>();
    }

    @Override
    public void simpleInitApp() {
        is = new ItemSpawner(assetManager);
        world = new Node("world");
        rootNode.attachChild(world);

        initWorld(world);
        initSceneItems();
        initPlayer();

        initKeys();
        initCamera();

        initCollisions();

        speed = PLAYER_SPEED;
    }

    private void initCamera() {
//        cam.setParallelProjection(true);        
//        final float halfY = 1.5f;
//        final float aspect = 4 / 3.0f;
//        cam.setFrustum(-100, 100, -halfY * aspect, halfY * aspect, 1.5f*halfY, -halfY);

        flyCam.setEnabled(false);
        final ChaseCamera chaseCam = new ChaseCamera(cam, playerAvatar.getModel(), inputManager);
        chaseCam.setTrailingEnabled(true);
        chaseCam.setDownRotateOnCloseViewOnly(false);
        chaseCam.setSmoothMotion(true);
        chaseCam.setDefaultDistance(5.5f);
        chaseCam.setMaxDistance(5.5f);
        chaseCam.setDragToRotate(false);
        chaseCam.setDefaultHorizontalRotation(90 * FastMath.DEG_TO_RAD);
        chaseCam.setDefaultVerticalRotation(12.5f * FastMath.DEG_TO_RAD);
        chaseCam.setLookAtOffset(new Vector3f(0, 1.0f, 0));
    }

    private void initPlayer() {
        playerAvatar = new PlayerAvatar(this);
//        playerAvatar.getModel().setLocalTranslation(0.0f, Level.LEVEL_HEIGHT / 2.0f, -Level.FLOOR_DEPTH / 2.0f);
//        playerAvatar.getModel().setLocalTranslation(0.0f, 0, 0.15f);
        final Level l = levels.get(0);
        l.getRootNode().attachChild(playerAvatar.getModel());
    }

    private void initWorld(final Node node) {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        levels.add(new Egypt(assetManager, Vector3f.ZERO));
        levels.add(new Egypt(assetManager, new Vector3f(0, Level.LEVEL_HEIGHT, 0)));
        levels.add(new Egypt(assetManager, new Vector3f(0, 2 * Level.LEVEL_HEIGHT, 0)));

        for (Level l : levels) {
            node.attachChild(l.getRootNode());
        }

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0, -0.01f, -1).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    private void initSceneItems() {
        final float boxSize = Level.LEVEL_HEIGHT / 4.0f;

        Spatial box = is.spawnBox(boxSize, boxSize, Level.FLOOR_DEPTH / 4.0f * 3);
        box.move(new Vector3f(3, Level.FLOOR_THICKNESS, Level.FLOOR_DEPTH / 8.0f));
        levels.get(0).attachChild(box);

        box = is.spawnBox(boxSize, boxSize, Level.FLOOR_DEPTH / 4.0f * 3);
        box.move(new Vector3f(1, Level.FLOOR_THICKNESS + 0.01f, Level.FLOOR_DEPTH / 8.0f));
        levels.get(0).attachChild(box);
    }

    private void initCollisions() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
//        bulletAppState.setDebugEnabled(true);

        final CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(world);
        landscape = new RigidBodyControl(sceneShape, 0);

        player = new BetterCharacterControl(.1f, .35f, 50f);
        player.setJumpForce(new Vector3f(0, 250f, 0));

        playerAvatar.getModel().addControl(player);
        player.warp(new Vector3f(0, Level.FLOOR_THICKNESS + Level.LEVEL_HEIGHT / 2.0f, Level.FLOOR_DEPTH / 2.0f));
        player.setViewDirection(Vector3f.UNIT_Z.negate());

        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Level", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(Actions.PAUSE.toString(), new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping(Actions.LEFT.toString(), new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping(Actions.RIGHT.toString(), new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping(Actions.JUMP.toString(), new KeyTrigger(KeyInput.KEY_I));
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
                } else if (name.equals(Actions.JUMP.toString())) {
                    if (isPressed) {
                        player.jump();
                    }
                } else if (name.equals("Level")) {
                    if (!isPressed) {
                        player.warp(playerAvatar.getModel().getLocalTranslation().add(0, Level.LEVEL_HEIGHT, 0));
                    }
                }
            }
        };
        inputManager.addListener(actionListener, "Level");
        inputManager.addListener(actionListener, Actions.PAUSE.toString());
        inputManager.addListener(actionListener, Actions.LEFT.toString());
        inputManager.addListener(actionListener, Actions.RIGHT.toString());
        inputManager.addListener(actionListener, Actions.JUMP.toString());

    }

    @Override
    public void simpleUpdate(float tpf) {
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(moveLeft);
            player.setViewDirection(moveLeft.negate());
        }
        if (right) {
            walkDirection.addLocal(moveLeft.negate());
            player.setViewDirection(moveLeft);
        }
        player.setWalkDirection(walkDirection);
    }

}
