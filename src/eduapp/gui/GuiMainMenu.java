package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.level.LevelName;
import eduapp.state.StateManager;

/**
 *
 * @author Petr Ječmen
 */
public class GuiMainMenu implements ScreenController {

    private static final String LISTBOX_NAME = "LevelList";
    private ListBox<LevelName> listBox;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        listBox = nifty.getCurrentScreen().findNiftyControl(LISTBOX_NAME, ListBox.class);
        for (LevelName ln : LevelName.values()) {
            listBox.addItem(ln);
        }
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void startGame() {
        System.out.println("Starting the game !!!");
        StateManager.loadLevel(listBox.getFocusItem().toString());
        GuiManager.gotoGameScreen();
    }
}
