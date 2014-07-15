/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eduapp.level;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class Background {

    private static final Vector3f TILE_SIZE_FLOOR = new Vector3f(Level.TILE_SIZE, -0.01f, Level.TILE_SIZE);
    private static final Vector3f TILE_SIZE_WALL = new Vector3f(Level.TILE_SIZE, 2.0f, Level.TILE_SIZE);
    private static final char CHAR_EMPTY = ' ';
    private static final char CHAR_BLOCK = ';';
    private static final char CHAR_PZ = '0';
    private final String values;
    private final Set<Character> walls;
    private final Map<Character, String> textureMapping;
    private char empty;
    private Node rootNode;

    public Background(String values) {
        this.values = values;
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
        rootNode = new Node("Pozadi");
        final Map<Character, Material> matCache = new HashMap<>();
        Material mat;
        Box tile;
        char ch;
        Geometry g;
        // load all tiles
        final int[] shift = new int[2];
        int width = 0;
        List<List<Character>> tiles = new LinkedList<>();
        List<Character> line = new LinkedList<>();
        for (int index = 0; index < values.length(); index++) {
            ch = values.charAt(index);
            if (ch == '\n') {                
                width = Math.max(width, line.size());
                line = new LinkedList<>();
                tiles.add(line);
            } else {
                line.add(ch);
                if (ch == CHAR_PZ) {
                    shift[0] = line.size() - 1;
                    shift[1] = tiles.size() - 1;
                }
            }

        }
        shift[1] = tiles.size() - shift[1];
        // generte tiles        
        int x, z;
        final int height = tiles.size();
        for (int dz = 0; dz < tiles.size(); dz++) {
            line = tiles.get(dz);
            z = -height + shift[1] + dz;

            for (int dx = 0; dx < line.size(); dx++) {
                ch = line.get(dx);
                x = shift[0] + dx;

                if (ch != empty && ch != CHAR_EMPTY && ch != CHAR_PZ) {
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
                    g.move(x, 0, z);
                    g.setMaterial(mat);
                    rootNode.attachChild(g);
                } else {
                    if (matCache.containsKey(CHAR_BLOCK)) {
                        mat = matCache.get(CHAR_BLOCK);
                    } else {
                        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                        mat.setColor("Color", new ColorRGBA(1.0f, 0, 0, 0.0f));
                        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                        matCache.put(CHAR_BLOCK, mat);
                    }
                    tile = new Box(Vector3f.ZERO, TILE_SIZE_WALL);
                    g = new Geometry("block", tile);
                    g.setMaterial(mat);
                    g.setQueueBucket(Bucket.Transparent);
                    g.move(dx, 0, dz);
                    rootNode.attachChild(g);
                }
            }
        }
        // generate fence around the level        
        mat = matCache.get(CHAR_BLOCK);
        // left
        tile = new Box(Vector3f.ZERO, new Vector3f(1.0f, 2.0f, height + 2));
        g = new Geometry("blockL", tile);
        g.setMaterial(mat);
        g.setQueueBucket(Bucket.Transparent);
        g.move(-1, 0, -height);
        rootNode.attachChild(g);
        // right
        tile = new Box(Vector3f.ZERO, new Vector3f(1.0f, 2.0f, height + 2));
        g = new Geometry("blockR", tile);
        g.setMaterial(mat);
        g.setQueueBucket(Bucket.Transparent);
        g.move(width, 0, -height);
        rootNode.attachChild(g);
        // bottom
        tile = new Box(Vector3f.ZERO, new Vector3f(width, 2.0f, 1.0f));
        g = new Geometry("block", tile);
        g.setMaterial(mat);
        g.setQueueBucket(Bucket.Transparent);
        g.move(0, 0, 1);
        rootNode.attachChild(g);
        // top
        tile = new Box(Vector3f.ZERO, new Vector3f(width, 2.0f, 1.0f));
        g = new Geometry("block", tile);
        g.setMaterial(mat);
        g.setQueueBucket(Bucket.Transparent);
        g.move(0, 0, -height);
        rootNode.attachChild(g);
    }

    public Node getRootNode() {
        return rootNode;
    }
}
