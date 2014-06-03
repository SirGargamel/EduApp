package eduapp.state;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.zero_separation.plugins.imagepainter.ImagePainter;
import eduapp.AppContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GroupScreen extends AbstractAppState implements ActionListener, AnalogListener {

    private static final float SIZE_BOX_X = 1.0f;
    private static final float SIZE_BOX_Y = 1.0f;
    private static final float SIZE_BOX_Z = 0.1f;
    private static final float SIZE_GAP = 0.5f;
    private static final float SIZE_GROUP_R = 2.5f;
    private static final float SIZE_GROUP_H = 0.1f;
    private static final float POS_OFFSET_GROUP_X = -5.5f;
    private static final float POS_OFFSET_GROUP_Y = 3.0f;
    private static final float POS_OFFSET_BOX_X = -7.0f;
    private static final float POS_OFFSET_BOX_Y = -5.0f;
    private static final float POS_OFFSET_CAM_Z = 15.0f;
    private List<String> groups, itemsList;
    private InputManager inputManager;
    private Camera cam;
    private Node rootNode, guiNode, floor, buckets, items;
    private Geometry picked;

    public GroupScreen() {
        groups = new ArrayList<>();
        itemsList = new ArrayList<>();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        AppContext.getApp().getFlyByCamera().setEnabled(false);
        cam = AppContext.getApp().getCamera();
        cam.setLocation(Vector3f.UNIT_Z.mult(POS_OFFSET_CAM_Z));

        inputManager = AppContext.getApp().getInputManager();
        inputManager.setCursorVisible(true);
        inputManager.addMapping("LeftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("MouseMove",
                new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addListener(this, "LeftClick", "MouseMove");

        rootNode = AppContext.getApp().getRootNode();
        guiNode = AppContext.getApp().getGuiNode();
        DesktopAssetManager assetManager = (DesktopAssetManager) app.getAssetManager();
        BitmapFont bf = assetManager.loadFont("interface/Fonts/Base32.fnt");

        Material mat;
        Geometry g;

        // generate circles for groups        
        mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        int i = 0;
        int w = 1024 / groups.size();
        int gap = 10;
        buckets = new Node();
        for (String s : groups) {
            g = new Geometry(s, new Cylinder(50, 50, SIZE_GROUP_R, SIZE_GROUP_H, true));
            g.setMaterial(mat);
            g.move(POS_OFFSET_GROUP_X + i * (SIZE_GROUP_R * 2 + SIZE_GAP), POS_OFFSET_GROUP_Y, -SIZE_GROUP_H / 2.0f);
            buckets.attachChild(g);

            BitmapText hudText = new BitmapText(bf, false);
            hudText.setSize(bf.getCharSet().getRenderedSize());
            hudText.setColor(ColorRGBA.White);
            hudText.setText(s);
            hudText.setLocalTranslation(w / 2 + i * (w - gap), 768, 0);
            guiNode.attachChild(hudText);

            i++;
        }
        rootNode.attachChild(buckets);

        // generate cubes for items                         
        i = 0;
        items = new Node("items");
        for (String s : itemsList) {
            mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            Texture tex = assetManager.loadTexture("materials/wallConcrete.png");
            assetManager.deleteFromCache(tex.getKey());
            Image img = tex.getImage();
            ImagePainter ip = new ImagePainter(img);
            ip.paintTextArea(0, 0, img.getWidth(), img.getHeight(), bf, s, ImagePainter.TextHAlign.Center, ImagePainter.TextVAlign.Center, ColorRGBA.Black, ImagePainter.BlendMode.SET);
            mat.setTexture("ColorMap", tex);

            g = new Geometry(s, new Box(Vector3f.ZERO, new Vector3f(SIZE_BOX_X, SIZE_BOX_Y, SIZE_BOX_Z)));
            g.setMaterial(mat);
            g.move(POS_OFFSET_BOX_X + i * (SIZE_BOX_X + SIZE_GAP), POS_OFFSET_BOX_Y, -SIZE_BOX_Z / 2.0f);

            items.attachChild(g);
            i++;
        }
        rootNode.attachChild(items);

        // create background
        floor = new Node("floor");
        mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.BlackNoAlpha);
        g = new Geometry("floorG", new Quad(100, 100));
        g.setMaterial(mat);
        g.move(-50, -50, -0.05f);
        floor.attachChild(g);
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    public void setGroups(String... groups) {
        this.groups.clear();
        for (String s : groups) {
            this.groups.add(s);
        }
    }

    public void setItems(String... items) {
        this.itemsList.clear();
        for (String s : items) {
            this.itemsList.add(s);
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("LeftClick")) {
            if (isPressed) {
                CollisionResults results = new CollisionResults();
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(
                        new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(
                        new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                Ray ray = new Ray(click3d, dir);
                rootNode.collideWith(ray, results);
                if (results.size() > 0) {
                    picked = results.getClosestCollision().getGeometry();
                }
            } else if (picked != null) {
                picked = null;
                checkGrouping();
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals("MouseMove") && picked != null) {
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            floor.collideWith(ray, results);
            if (results.size() > 0) {
                picked.setLocalTranslation(results.getClosestCollision().getContactPoint().add(-0.5f, -0.5f, 0));
            }
        }
    }

    private void checkGrouping() {
        CollisionResults results = new CollisionResults();
        int counter = 0;
        for (Spatial s : buckets.getChildren()) {
            results.clear();
            items.collideWith(s.getWorldBound(), results);
            counter += results.size() / 10;
        }
        if (counter >= items.getChildren().size()) {
            System.out.println("All DONE");
        }
    }
}
