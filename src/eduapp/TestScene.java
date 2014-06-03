package eduapp;

import eduapp.level.xml.LevelLoader;
import eduapp.level.Level;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiManager;
import eduapp.state.StateManager;

/**
 *
 * @author Petr Jecmen
 */
public class TestScene extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        assetManager.registerLoader(LevelLoader.class, Level.LEVEL_FILE_EXTENSION);

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
}
