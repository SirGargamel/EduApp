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
import eduapp.Actions;
import eduapp.AppContext;
import eduapp.FlowManager;
import eduapp.ItemParameters;
import eduapp.level.Item;
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
    private Item group;
    private List<Item> itemsList;
    private InputManager inputManager;
    private Camera cam;
    private Node rootNode, guiNode, floor, buckets, items;
    private Geometry picked;

    public GroupScreen() {
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
        inputManager.addMapping(Actions.LEFT_CLICK.toString(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(Actions.MOUSE_MOVE.toString(),
                new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addListener(this, Actions.LEFT_CLICK.toString(), Actions.MOUSE_MOVE.toString(), Actions.PAUSE.toString());

        rootNode = AppContext.getApp().getRootNode();
        guiNode = AppContext.getApp().getGuiNode();
        DesktopAssetManager assetManager = (DesktopAssetManager) app.getAssetManager();
        BitmapFont bf = assetManager.loadFont("interface/Fonts/Base32.fnt");

        Material mat;
        Geometry g;

        // generate circles for groups        
        final String[] groups = group.getParam(ItemParameters.STATE).split(ItemParameters.SPLITTER);

        mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        int i = 0;
        int w = 1024 / groups.length;
        int gap = 10;
        buckets = new Node();
        String sT;
        for (String s : groups) {
            sT = s.trim();
            g = new Geometry(sT, new Cylinder(50, 50, SIZE_GROUP_R, SIZE_GROUP_H, true));
            g.setMaterial(mat);
            g.move(POS_OFFSET_GROUP_X + i * (SIZE_GROUP_R * 2 + SIZE_GAP), POS_OFFSET_GROUP_Y, -SIZE_GROUP_H / 2.0f);
            buckets.attachChild(g);

            BitmapText hudText = new BitmapText(bf, false);
            hudText.setSize(bf.getCharSet().getRenderedSize());
            hudText.setColor(ColorRGBA.White);
            hudText.setText(sT);
            hudText.setLocalTranslation(w / 2 + i * (w - gap), 768, 0);
            guiNode.attachChild(hudText);

            i++;
        }
        rootNode.attachChild(buckets);

        // generate cubes for items                         
        i = 0;
        items = new Node("items");
        for (Item it : itemsList) {
            mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            Texture tex = assetManager.loadTexture("materials/wallConcrete.png");
            assetManager.deleteFromCache(tex.getKey());
            Image img = tex.getImage();
            ImagePainter ip = new ImagePainter(img);
            ip.paintTextArea(0, 0, img.getWidth(), img.getHeight(), bf, it.getParam(ItemParameters.FORMULA), ImagePainter.TextHAlign.Center, ImagePainter.TextVAlign.Center, ColorRGBA.Black, ImagePainter.BlendMode.SET);
            mat.setTexture("ColorMap", tex);

            g = new Geometry(it.getParam(ItemParameters.STATE), new Box(Vector3f.ZERO, new Vector3f(SIZE_BOX_X, SIZE_BOX_Y, SIZE_BOX_Z)));
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
        rootNode.detachAllChildren();
        guiNode.detachAllChildren();

        inputManager.removeListener(this);
        inputManager.deleteMapping(Actions.LEFT_CLICK.toString());
        inputManager.deleteMapping(Actions.MOUSE_MOVE.toString());
    }

    public void setGrouping(Item group) {
        if (group != null) {
            this.group = group;
            final String id = group.getId();
            final String gr = group.getParam(id);
            if (gr == null) {
                System.err.println("Illegal group - " + group);
            }
        } else {
            System.err.println("NULL group.");
        }
    }

    public void setItems(Item... items) {
        this.itemsList.clear();
        final String id = group.getId();
        if (id == null) {
            System.err.println("Group does not set.");
        } else {
            String val;
            for (Item it : items) {
                val = it.getParam(id);
                if (val == null) {
                    System.err.println("Item missing group param - " + id);
                } else {
                    itemsList.add(it);
                }
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(Actions.LEFT_CLICK.toString())) {
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
        } else if (name.equals(Actions.PAUSE.toString())) {
            if (!isPressed && name.equals(Actions.PAUSE.toString())) {
                FlowManager.enableGame(!isEnabled());
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(Actions.MOUSE_MOVE.toString()) && picked != null) {
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

    public int[] checkGrouping() {
        CollisionResults results = new CollisionResults();
        int counter = 0;
        String[] vals;
        String gr;
        boolean match;
        for (Spatial s : buckets.getChildren()) {
            results.clear();
            items.collideWith(s.getWorldBound(), results);

            gr = s.getName();
            for (int i = 9; i < results.size(); i += 10) {
                vals = results.getCollision(i).getGeometry().getName().split(ItemParameters.SPLITTER);
                match = false;
                for (String str : vals) {
                    if (str.trim().equalsIgnoreCase(gr)) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    counter += 1;
                }
            }
        }
        System.out.println(counter + " correct");
        return new int[]{counter, itemsList.size()};
    }
}
