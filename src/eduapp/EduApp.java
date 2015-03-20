package eduapp;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import eduapp.loaders.LevelLoader;
import eduapp.loaders.StateLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Petr Jecmen
 */
public class EduApp extends SimpleApplication {

    public static final boolean DEBUG = false;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        final Logger l = Logger.getGlobal();
        l.addHandler(new ConsoleHandler());
        if (DEBUG) {
            l.setLevel(Level.ALL);
        } else {
            l.setLevel(Level.INFO);
        }

        try {
            final SimpleApplication app = new EduApp();
            final AppSettings settings = new AppSettings(true);
            settings.setRenderer(AppSettings.LWJGL_OPENGL_ANY);
            settings.setWidth(WIDTH);
            settings.setHeight(HEIGHT);
            settings.setTitle("EduApp");
            settings.setDepthBits(16);
            settings.setBitsPerPixel(16);
            settings.setFrequency(30);
            settings.setVSync(true);
            settings.setAudioRenderer(null);
            settings.setIcons(new BufferedImage[]{ImageIO.read(new File("data/icon.png"))});

            app.setShowSettings(false);
            app.setSettings(settings);
            app.setPauseOnLostFocus(true);

            if (!DEBUG) {
                app.setDisplayStatView(false);
                app.setDisplayFps(false);
            }

            app.start();

            JmolUtils.initializeJmolPanel();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("data/", FileLocator.class);
        assetManager.registerLoader(LevelLoader.class, LevelLoader.EXTENSION_DEF);
        assetManager.registerLoader(StateLoader.class, StateLoader.EXTENSION);

        initKeyMappings();

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("interface/Gui.xml", "start");
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        AppContext.setApp(this);
        FlowManager.getInstance().setNifty(nifty);

        final String path = "state." + StateLoader.EXTENSION;
        final AssetKey<Integer> key = new AssetKey<>(path);
        int levelState = 0;
        try {
            levelState = assetManager.loadAsset(key);
        } catch (AssetNotFoundException e) {
            // do nothing
        }
        levelState = 5;
        FlowManager.getInstance().setLevelState(levelState);
        FlowManager.getInstance().gotoMainMenu();

        // DEBUG
//        final DragQuest quest = new DragQuest("h2so4");
//        quest.addItem(new DragQuest.DragItem(DragQuest.DragItemType.DRAG, "s"));
//        quest.addItem(new DragQuest.DragItem(DragQuest.DragItemType.STATIC, " + "));
//        quest.addItem(new DragQuest.DragItem(DragQuest.DragItemType.DRAG, "o2"));
//        quest.addItem(new DragQuest.DragItem(DragQuest.DragItemType.STATIC, " = "));
//        quest.addItem(new DragQuest.DragItem(DragQuest.DragItemType.DRAG, "so2"));        
//        quest.addExtra("h2o");
//        quest.addExtra("o3");        
//        FlowManager.getInstance().displayDragScreen(quest);
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
        inputManager.addMapping(Actions.DICTIONARY.toString(), new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(Actions.LEFT_CLICK.toString(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    }

    @Override
    public void destroy() {
        super.destroy();
        JmolUtils.closeViewer();
        System.exit(0);
    }
}
