/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class Background {

    private static final Vector3f TILE_SIZE_FLOOR = new Vector3f(Level.TILE_SIZE, -0.01f, Level.TILE_SIZE);
    private static final Vector3f TILE_SIZE_WALL = new Vector3f(Level.TILE_SIZE, 2.0f, Level.TILE_SIZE);
    private final String values;
    private final float[] pointZero;
    private final int width;
    private final Set<Character> walls;
    private final Map<Character, String> textureMapping;
    private char empty;
    private Node rootNode;    

    public Background(String values, float[] pointZero, int width) {
        this.values = values;
        this.pointZero = pointZero;
        this.width = width;
        walls = new HashSet<>();
        textureMapping = new HashMap<>();
    }

    public void addWall(final Character wallChar) {
        walls.add(wallChar);
    }

    public void addTextureMapping(final Character key, final String value) {
        textureMapping.put(key, value);
    }

    public void setEmptyKey(final char key) {
        empty = key;
    }

    void generateBackground(final AssetManager assetManager) {
        rootNode = new Node("background");
        final int height = values.length() / width;
        final Map<Character, Material> matCache = new HashMap<>();
        float x = pointZero[0];
        float y = pointZero[1] - height;
        Material mat;
        Box tile;
        char ch;
        Geometry g;
        for (int index = 0; index < values.length(); index++) {
            if (index % width == 0) {
                x = pointZero[0];
                y += Level.TILE_SIZE;
            } else {
                x += Level.TILE_SIZE;
            }
            ch = values.charAt(index);
            if (ch != empty) {
                if (matCache.containsKey(ch)) {
                    mat = matCache.get(ch);
                } else {
                    //                        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    mat = new Material(assetManager, "lights/Lighting.j3md");
                    //                        mat.setTexture("ColorMap", assetManager.loadTexture(new StringBuilder("materials").append("/").append(textureMapping.get(ch)).toString()));
                    mat.setTexture("DiffuseMap", assetManager.loadTexture(new StringBuilder("materials").append("/").append(textureMapping.get(ch)).toString()));
                    matCache.put(ch, mat);
                }
                if (walls.contains(ch)) {
                    tile = new Box(Vector3f.ZERO, TILE_SIZE_WALL);
                } else {
                    tile = new Box(Vector3f.ZERO, TILE_SIZE_FLOOR);
                }
                //                tile.clearBuffer(Type.Normal);
                g = new Geometry("floor", tile);
                g.move(x, 0, y);
                g.setMaterial(mat);
                rootNode.attachChild(g);
            }
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

}
