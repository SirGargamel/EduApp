package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;
import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Petr Ječmen
 */
public class GuiMainMenu implements ScreenController {

    private static final String LISTBOX_NAME = "LevelList";
    private static final String LEVEL_EXTENSION = ".xml";
    private ListBox<String> listBox;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        listBox = nifty.getCurrentScreen().findNiftyControl(LISTBOX_NAME, ListBox.class);        
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void startGame() {
        FlowManager.getInstance().loadLevel(listBox.getFocusItem());
    }

    public void setLevelCount(int finishedLevelCount) {
        final File f = new File("data\\levels");
        int counter = 0;
        for (String s : f.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(LEVEL_EXTENSION);
            }
        })) {
            listBox.addItem(s.replace(LEVEL_EXTENSION, ""));
            counter++;
            if (counter > finishedLevelCount) {
                break;
            }
        }
    }

    @NiftyEventSubscriber(pattern = "LevelList.*")
    public void onListBoxSelectionChanged(final String id,
            final ListBoxSelectionChangedEvent<String> changed) {
        startGame();
    }
}
