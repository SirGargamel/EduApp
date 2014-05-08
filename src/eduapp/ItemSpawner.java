package eduapp;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Petr Jecmen
 */
public class ItemSpawner {
    
    private final AssetManager assetManager;

    public ItemSpawner(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public Spatial spawnBox(final float width, final float height, final float depth) {
        final Box box = new Box(width / 2.0f, height / 2.0f, depth / 2.0f);
        final Geometry b = new Geometry("box", box);
        final Material mat = new Material(assetManager, "lights/Lighting.j3md");
        final Texture tex = assetManager.loadTexture("materials/crate/WoodCrate_lighter.png");

        mat.setTexture("DiffuseMap", tex);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 2);

        b.setMaterial(mat);
        return b;
    }
    
}
