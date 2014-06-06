package eduapp;

import eduapp.loaders.LevelLoader;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import eduapp.level.Item;
import eduapp.loaders.DictionaryLoader;
import java.util.Set;

/**
 *
 * @author Petr Jecmen
 */
public class Game extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        assetManager.registerLoader(LevelLoader.class, LevelLoader.EXTENSION);
        assetManager.registerLoader(DictionaryLoader.class, DictionaryLoader.EXTENSION);

        loadDictionary(assetManager, AppContext.getItemRegistry(), "Elements");
        loadDictionary(assetManager, AppContext.getItemRegistry(), "Groups");
        
        initKeyMappings();

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("interface/Gui.xml", "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        AppContext.setApp(this);
        FlowManager.setNifty(nifty);

        FlowManager.gotoMainMenu();
    }

    private void loadDictionary(final AssetManager assetManager, final ItemRegistry ir, final String dicName) {
        final AssetKey<Set<Item>> key = new AssetKey<>("texts/".concat(dicName).concat(".").concat(DictionaryLoader.EXTENSION));
        final Set<Item> elements = assetManager.loadAsset(key);
        for (Item it : elements) {
            ir.put(it);
        }
    }

    private void initKeyMappings() {
//        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_MEMORY);
//        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_CAMERA_POS);

        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_HIDE_STATS);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        
        inputManager.addMapping(Actions.PAUSE.toString(), new KeyTrigger(KeyInput.KEY_ESCAPE)); 
        
        inputManager.addMapping(Actions.LEFT.toString(), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(Actions.RIGHT.toString(), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(Actions.UP.toString(), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(Actions.DOWN.toString(), new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping(Actions.ACTION.toString(), new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(Actions.QUEST.toString(), new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping(Actions.LEFT_CLICK.toString(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    }
}
