package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;
import eduapp.Utils;
import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Petr Ječmen
 */
public class GuiMainMenu implements ScreenController {

    private static final String LISTBOX_NAME = "LevelList";
    private static final String LEVEL_EXTENSION = ".xml";
    private Nifty nifty;
    private ListBox<String> listBox;
    private Element rewards;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        listBox = nifty.getCurrentScreen().findNiftyControl(LISTBOX_NAME, ListBox.class);
        rewards = nifty.getCurrentScreen().findElementByName("rewards");
    }

    @Override
    public void onStartScreen() {
        buildRewards();
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
        if (listBox.itemCount() > 0) {
            listBox.clear();
        }
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

        buildRewards();
    }

    private void buildRewards() {
        if (rewards != null) {
            for (Element e : rewards.getElements()) {
                e.markForRemoval();
            }
            nifty.executeEndOfFrameElementActions();

            final Screen current = nifty.getCurrentScreen();
            PanelBuilder pb;
            ImageBuilder ib;
            for (int i = 0; i < listBox.itemCount() - 1; i++) {
                pb = new PanelBuilder("rew" + i);
                pb.width("15%");
//                pb.backgroundColor("#8b9dc388");
                pb.childLayoutCenter();

                ib = new ImageBuilder("image" + i);
                ib.filename(Utils.generateIconFilename("award-" + Integer.toString(i + 1)));
                ib.height("100%");
                ib.width("100%");
                pb.image(ib);

                pb.build(nifty, current, rewards);

                pb = new PanelBuilder("gap" + i);
                pb.width("10%");
                pb.build(nifty, current, rewards);
            }
        }
    }

    @NiftyEventSubscriber(pattern = "LevelList.*")
    public void onListBoxSelectionChanged(final String id,
            final ListBoxSelectionChangedEvent<String> changed) {
        startGame();
    }
}
