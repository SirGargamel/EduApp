package eduapp.state;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.controls.ListBox;
import eduapp.level.LevelName;

/**
 *
 * @author Petr Ječmen
 */
public final class StartScreen extends AbstractAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached    
        System.out.println("Init StartScreen.");
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
//        System.out.println(gameScreen);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        System.out.println("cleanup");
    }
}
