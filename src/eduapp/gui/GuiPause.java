package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;

/**
 *
 * @author Petr Ječmen
 */
public class GuiPause implements ScreenController {

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void backToMenu() {       
        FlowManager.gotoMainMenu();
    }

    public void backToGame() {        
        FlowManager.handlePause();
    }

    public void exit() {
        FlowManager.exitGame();
    }
}
