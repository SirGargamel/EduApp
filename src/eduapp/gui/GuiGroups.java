package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;

/**
 *
 * @author Petr Ječmen
 */
public class GuiGroups implements ScreenController {

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {        
        FlowManager.finishGroupScreen();
    }
}
