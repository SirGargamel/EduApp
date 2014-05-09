package eduapp;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Egypt extends Level {

    public Egypt(AssetManager am, final Vector3f translate) {
        super(am, translate);
        prepareLevel();
        prepareLights();
    }

    private void prepareLevel() {
        // wall        
        Material mat = new Material(assetManager, "lights/Lighting.j3md");
        Texture texture = assetManager.loadTexture("egypt/wallTile.png");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", texture);
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.Black);
        mat.setFloat("Shininess", 2);

        Box background = new Box(Vector3f.ZERO, new Vector3f(LEVEL_WIDTH, LEVEL_HEIGHT - 2 * Level.FLOOR_THICKNESS, 0f));
        Geometry b = new Geometry("background", background);
        b.move(0, Level.FLOOR_THICKNESS, 0);
        b.move(translate);
        b.setMaterial(mat);
        background.scaleTextureCoordinates(new Vector2f(Level.LEVEL_WIDTH, 2 * Level.LEVEL_HEIGHT));
        rootNode.attachChild(b);

        // floor + ceiling
        final Box floor = new Box(Vector3f.ZERO, new Vector3f(LEVEL_WIDTH, FLOOR_THICKNESS, FLOOR_DEPTH));
        final Geometry f = new Geometry("floor", floor);
        f.move(translate);
        mat = new Material(assetManager, "lights/Lighting.j3md");
        texture = assetManager.loadTexture("egypt/floorTile.png");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", texture);        
        f.setMaterial(mat);
        floor.scaleTextureCoordinates(new Vector2f(Level.LEVEL_WIDTH * 4, 1.0f));
        rootNode.attachChild(f);

        final Box ceiling = new Box(Vector3f.ZERO, new Vector3f(LEVEL_WIDTH, FLOOR_THICKNESS, FLOOR_DEPTH));
        final Geometry c = new Geometry("ceiling", ceiling);
        c.move(0f, LEVEL_HEIGHT - FLOOR_THICKNESS, 0);
        c.move(translate);
        c.setMaterial(mat);
        mat = new Material(assetManager, "lights/Lighting.j3md");
        texture = assetManager.loadTexture("egypt/floorTile.png");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", texture);
        ceiling.scaleTextureCoordinates(new Vector2f(Level.LEVEL_WIDTH * 4, 1f));
        rootNode.attachChild(c);
    }

    private void prepareLights() {
        final AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.Yellow.mult(1.5f));
        rootNode.addLight(ambient);

        for (float x = -Level.LEVEL_WIDTH / 2.0f; x <= Level.LEVEL_WIDTH / 2.0f; x += 2) {
            PointLight l = new PointLight();
            l.setPosition(new Vector3f(x, Level.LEVEL_HEIGHT / 2.0f, FLOOR_DEPTH / 2.0f).addLocal(translate));
            l.setRadius(1f);
            l.setColor(ColorRGBA.Yellow.mult(1.5f));
            rootNode.addLight(l);
        }
    }

}
