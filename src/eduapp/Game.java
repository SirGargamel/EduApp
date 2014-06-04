package eduapp;

import eduapp.loaders.LevelLoader;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiManager;
import eduapp.level.Item;
import eduapp.loaders.DictionaryLoader;
import eduapp.state.StateManager;
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

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("interface/Gui.xml", "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

//        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_MEMORY);
//        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_CAMERA_POS);

        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_HIDE_STATS);
//        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);

        AppContext.setApp(this);
        GuiManager.setNifty(nifty);

        GuiManager.gotoGameScreen();        
        StateManager.debug();
    }

    private void loadDictionary(final AssetManager assetManager, final ItemRegistry ir, final String dicName) {
        final AssetKey<Set<Item>> key = new AssetKey<>("texts/".concat(dicName).concat(".").concat(DictionaryLoader.EXTENSION));
        final Set<Item> elements = assetManager.loadAsset(key);
        for (Item it : elements) {
            ir.put(it);
        }
    }
}
