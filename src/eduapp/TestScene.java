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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Petr Jecmen
 */
public class TestScene extends SimpleApplication {

    private static final float PLAYER_POSITION_Z = 0.05f;
    private static final float PLAYER_SPEED = 1.0f;
    private static final float WORLD_WIDTH = 10.0f;
    private static final float FLOOR_HEIGHT = 0.75f;
    private static final float FLOOR_THICKNESS = 0.1f;
    private static final float FLOOR_DEPTH = 0.25f;
    private BulletAppState bulletAppState;
    private BetterCharacterControl player;
    private PlayerAvatar playerAvatar;
    private ItemSpawner is;
    private RigidBodyControl landscape;
    private final Vector3f walkDirection = new Vector3f();
    private final Vector3f camLeft = new Vector3f(-1f, 0, 0);
    boolean left, right;
    private boolean isRunning = true;
    private Node world;

    @Override
    public void simpleInitApp() {
        is = new ItemSpawner(assetManager);
        world = new Node("world");

        initWorld();
        initSceneItems();
        initPlayer();
        initLights();

        initKeys();
        initCamera();

        initCollisions();

        rootNode.attachChild(world);

        speed = PLAYER_SPEED;
    }

    private void initCamera() {
        //        cam.setParallelProjection(true);
        flyCam.setEnabled(false);
        final ChaseCamera chaseCam = new ChaseCamera(cam, playerAvatar.getModel(), inputManager);
        chaseCam.setTrailingEnabled(true);
        chaseCam.setDownRotateOnCloseViewOnly(false);
        chaseCam.setSmoothMotion(true);
        chaseCam.setDefaultDistance(1.5f);
        chaseCam.setMaxDistance(1.5f);
        chaseCam.setDragToRotate(false);
        chaseCam.setDefaultHorizontalRotation(90 * FastMath.DEG_TO_RAD);
        chaseCam.setDefaultVerticalRotation(12.5f * FastMath.DEG_TO_RAD);
        chaseCam.setLookAtOffset(new Vector3f(0, 0.35f, 0));
    }

    private void initPlayer() {
        playerAvatar = new PlayerAvatar(this);
//        playerAvatar.getModel().setLocalTranslation(0.0f, -(FLOOR_HEIGHT / 2.0f), FLOOR_DEPTH / 2.0f);
//        playerAvatar.getModel().setLocalTranslation(0.0f, 0, 0.15f);
    }

    private void initWorld() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        // wall        
        Material mat = new Material(assetManager, "lights/Lighting.j3md");
        Texture texture = assetManager.loadTexture("materials/brickWall/BrickWall.jpg");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap",
                texture);
//        mat.setTexture("NormalMap",
//                assetManager.loadTexture("materials/brickWall/BrickWall_normal.jpg"));                
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.Gray);
        mat.setFloat("Shininess", 2);

        Box background = new Box(WORLD_WIDTH / 2.0f, FLOOR_HEIGHT / 2.0f, 0f);
        Geometry b = new Geometry("background", background);
        b.setMaterial(mat);
        background.scaleTextureCoordinates(new Vector2f(10, 3));
        world.attachChild(b);

        // floor + ceiling
        final Box floor = new Box(WORLD_WIDTH / 2.0f, FLOOR_THICKNESS / 2.0f, FLOOR_DEPTH / 2.0f);
        final Geometry f = new Geometry("floor", floor);
        f.move(0f, -FLOOR_HEIGHT / 2.0f - FLOOR_THICKNESS / 2.0f, FLOOR_DEPTH / 2.0f);
        mat = new Material(assetManager, "lights/Lighting.j3md");
        texture = assetManager.loadTexture("materials/rock2/rock.jpg");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", texture);
//        mat.setTexture("NormalMap",
//                assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg"));                
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.Gray);  // minimum material color
        mat.setColor("Specular", ColorRGBA.White); // for shininess
        mat.setFloat("Shininess", 32); // [1,128] for shininess
        f.setMaterial(mat);
        floor.scaleTextureCoordinates(new Vector2f(10, 0.5f));
        f.rotate(0, 0, 180 * FastMath.DEG_TO_RAD);
        world.attachChild(f);

        final Box ceiling = new Box(WORLD_WIDTH / 2.0f, FLOOR_THICKNESS / 2.0f, FLOOR_DEPTH / 2.0f);
        final Geometry c = new Geometry("ceiling", ceiling);
        c.move(0f, FLOOR_HEIGHT / 2.0f + FLOOR_THICKNESS / 2.0f, FLOOR_DEPTH / 2.0f);
        c.setMaterial(mat);
        mat = new Material(assetManager, "lights/Lighting.j3md");
        texture = assetManager.loadTexture("materials/rock2/rock.jpg");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", texture);
//        mat.setTexture("NormalMap",
//                assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg"));                
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1);
        ceiling.scaleTextureCoordinates(new Vector2f(10, 0.5f));
        world.attachChild(c);
    }

    private void initSceneItems() {
        final float boxSize = FLOOR_HEIGHT / 4.0f;
        final Spatial box = is.spawnBox(boxSize, boxSize, FLOOR_DEPTH);
        box.move(new Vector3f(-2, -1.5f * boxSize, FLOOR_DEPTH / 2.0f));
        world.attachChild(box);
    }

    private void initCollisions() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);

        final CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(world);
        landscape = new RigidBodyControl(sceneShape, 0);

        player = new BetterCharacterControl(.1f, .35f, 80f);
        player.setJumpForce(new Vector3f(0, 200f, 0));

        playerAvatar.getModel().addControl(player);
        player.warp(new Vector3f(0, .1f, .1f));
        player.setViewDirection(Vector3f.UNIT_Z.negate());

        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
    }

    private void initLights() {
        final AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.Yellow.mult(1.5f));
        rootNode.addLight(ambient);

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0, 0, -1).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(0.25f));
        rootNode.addLight(sun);

        for (float x = -WORLD_WIDTH / 2.0f; x <= WORLD_WIDTH / 2.0f; x += 2) {
            PointLight l = new PointLight();
            l.setPosition(new Vector3f(x, FLOOR_HEIGHT / 2.0f, PLAYER_POSITION_Z));
            l.setRadius(1f);
            l.setColor(ColorRGBA.White.mult(0.5f));
            rootNode.addLight(l);
        }
    }

    private void initKeys() {
        // You can map one or several inputs to one named action
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
                }
            }
        };
        inputManager.addListener(actionListener, Actions.PAUSE.toString());
        inputManager.addListener(actionListener, Actions.LEFT.toString());
        inputManager.addListener(actionListener, Actions.RIGHT.toString());
        inputManager.addListener(actionListener, Actions.JUMP.toString());

    }

    @Override
    public void simpleUpdate(float tpf) {
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
            player.setViewDirection(camLeft.negate());
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            player.setViewDirection(camLeft);
        }
        player.setWalkDirection(walkDirection);
    }

}
