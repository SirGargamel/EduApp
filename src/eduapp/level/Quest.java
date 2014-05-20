package eduapp.level;

import eduapp.gui.GuiManager;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class Quest extends Item {

    private final List<String> data;

    public Quest(List<String> data) {
        this.data = data;
    }
    
    public void makeActive() {
        GuiManager.displayQuest(this);
    }

    public List<String> getData() {
        return data;
    }
    
}
