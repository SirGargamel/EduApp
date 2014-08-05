package eduapp.screen;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import eduapp.AppContext;

/**
 *
 * @author Petr Ječmen
 */
public final class StartScreen extends AbstractAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        AppContext.getApp().getFlyByCamera().setEnabled(false);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
