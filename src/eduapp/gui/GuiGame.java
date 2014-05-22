package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Petr Ječmen
 */
public class GuiGame implements ScreenController {

    private Element icon;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        icon = nifty.getCurrentScreen().findElementByName("panelImage");
    }

    @Override
    public void onStartScreen() {
        icon.hide();
    }

    @Override
    public void onEndScreen() {
    }

    public void enableQuestMarker(boolean enable) {
        if (icon != null) {
            if (enable) {
                icon.show();
            } else {
                icon.hide();
            }
        }
    }
}
