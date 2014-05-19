package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.AppContext;
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
        System.out.println("bind (" + screen.getScreenId() + ")");

        listBox = nifty.getCurrentScreen().findNiftyControl(LISTBOX_NAME, ListBox.class);
        for (LevelName ln : LevelName.values()) {
            listBox.addItem(ln);
        }
    }

    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void startGame() {
        System.out.println("Starting the game !!!");
        StateManager.getInstance().setLevelName(listBox.getFocusItem().toString());
        GuiManager.gotoGameScreen();
    }
}
