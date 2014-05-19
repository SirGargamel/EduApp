package eduapp;

import eduapp.level.xml.LevelLoader;
import eduapp.level.Level;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiManager;

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
        // disable the fly cam
        flyCam.setDragToRotate(true);
//        inputManager.setCursorVisible(true);        
        
        AppContext.setApp(this);
        GuiManager.setNifty(nifty);        
    }

    
}
