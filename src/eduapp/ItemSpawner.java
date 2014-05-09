package eduapp;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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
        final Box box = new Box(Vector3f.ZERO, new Vector3f(width, height, depth));
        final Geometry b = new Geometry("box", box);        
        final Material mat = new Material(assetManager, "lights/Lighting.j3md");        

        mat.setTexture("DiffuseMap", assetManager.loadTexture("materials/crate/crate.png"));
//        mat.setTexture("NormalMap", assetManager.loadTexture("materials/crate/crate - NormalMapInvert.png"));
        mat.setTexture("ParallaxMap", assetManager.loadTexture("materials/crate/crate - HeightMap.png"));        
        mat.setColor("Diffuse", ColorRGBA.Black);
        mat.setColor("Specular", ColorRGBA.Black);
        mat.setFloat("Shininess", 1);

        b.setMaterial(mat);
        return b;
    }

}
