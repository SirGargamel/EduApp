package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.state.StateManager;

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
        StateManager.displayMainMenu();
        GuiManager.gotoMainMenu();
    }
    
    public void backToGame() {
        StateManager.enableGame(true);
        GuiManager.enableGame(true);
    }
}
