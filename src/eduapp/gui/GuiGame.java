package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiGame implements ScreenController {

    private Element icon;
    private List<Element> inv;
    private Player player;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        icon = nifty.getCurrentScreen().findElementByName("panelImage");
        
        inv = new ArrayList<>(5);
        inv.add(nifty.getCurrentScreen().findElementByName("inv1"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv2"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv3"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv4"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv5"));
    }

    @Override
    public void onStartScreen() {
        icon.hide();
        
        for (Element e : inv) {
            e.hide();
        }
        
        refreshInventoryItems();
    }

    @Override
    public void onEndScreen() {
    }

    public void setPlayer(Player player) {
        this.player = player;
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
    
    public void refreshInventoryItems() {
        // TODO retrieve and display icon
        final Collection<String> items = player.getAllItems();
        
        for (int i = 0; i < Math.min(items.size(), inv.size()); i++) {
            inv.get(i).show();
        }
    }    
}
