package eduapp;

import eduapp.level.LevelLoader;
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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
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
//        final Vector3f playerLocation = playerAvatar.getModel().getWorldTranslation();
//        cam.setLocation(playerLocation.add(CAMERA_OFFSET));
//        cam.lookAt(playerLocation, Vector3f.UNIT_Y);
//        flyCam.setRotationSpeed(0);
//
//        cam.setParallelProjection(true);
//        final float halfY = 3.5f;
//        final float aspect = settings.getWidth() / settings.getHeight();
//        cam.setFrustum(-100, 100, -halfY * aspect, halfY * aspect, halfY, -halfY);

        flyCam.setEnabled(false);
        final ChaseCamera chaseCam = new ChaseCamera(cam, playerAvatar.getModel(), inputManager);        
//        chaseCam.setTrailingEnabled(true);
//        chaseCam.setDownRotateOnCloseViewOnly(false);
//        chaseCam.setSmoothMotion(true);
        chaseCam.setDefaultDistance(7.5f);
//        chaseCam.setMaxDistance(15.5f);
//        chaseCam.setDragToRotate(true);
//        chaseCam.setInvertVerticalAxis(true);
//        chaseCam.setInvertHorizontalAxis(true);
        chaseCam.setDefaultHorizontalRotation(90 * FastMath.DEG_TO_RAD);
        chaseCam.setDefaultVerticalRotation(89.9f * FastMath.DEG_TO_RAD);
//        chaseCam.setLookAtOffset(new Vector3f(0, 1.0f, 0));        
    }

    private void initPlayer() {
        playerAvatar = new PlayerAvatar(this);
    }

    private void initWorld() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        currentLevel = Level.loadLevel("egypt", assetManager);
        rootNode.attachChild(currentLevel.getRootNode());

        final AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.multLocal(0.5f));
        rootNode.addLight(ambient);
        
        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0, -1, 0).normalizeLocal());
        sun.setColor(ColorRGBA.White.multLocal(0.5f));
        rootNode.addLight(sun);

        SpotLight l = new SpotLight();
        l.setColor(ColorRGBA.White.mult(10));
        l.setPosition(new Vector3f(1.5f, 1.5f, 1.5f));
        l.setDirection(Vector3f.UNIT_Y);
        l.setSpotRange(100);
        l.setSpotInnerAngle(10f * FastMath.DEG_TO_RAD);
        l.setSpotOuterAngle(15f * FastMath.DEG_TO_RAD);
        rootNode.addLight(l);                

//        Spatial table = assetManager.loadModel("models/bench2-table.j3o");
//        table.scale(0.5f);
//        table.move(1, 0, 3);
//        currentLevel.addItem(table);

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
        player.setJumpForce(new Vector3f(0, 250f, 0));

        playerAvatar.getModel().addControl(player);
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
        currentLevel.visitNode(p.x, p.z);
        System.out.println(p);
    }

}
