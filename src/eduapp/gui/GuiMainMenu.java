package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;
import eduapp.level.LevelList;

/**
 *
 * @author Petr Ječmen
 */
public class GuiMainMenu implements ScreenController {

    private static final String LISTBOX_NAME = "LevelList";
    private ListBox<LevelList> listBox;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        listBox = nifty.getCurrentScreen().findNiftyControl(LISTBOX_NAME, ListBox.class);
        for (LevelList ln : LevelList.values()) {
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
        FlowManager.getInstance().loadLevel(listBox.getFocusItem().toString());        
    }

    @NiftyEventSubscriber(pattern = "LevelList.*")
    public void onListBoxSelectionChanged(final String id,
            final ListBoxSelectionChangedEvent<LevelList> changed) {
        System.out.println("selection changed");
        startGame();
    }
}
