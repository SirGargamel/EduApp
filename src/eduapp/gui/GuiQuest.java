package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Petr Ječmen
 */
public class GuiQuest implements ScreenController {
    
    private String questText;
    private Element textField;

    public void setQuestText(String questText) {
        this.questText = questText;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        textField = nifty.getCurrentScreen().findElementByName("textQuest");
    }

    @Override
    public void onStartScreen() {        
        textField.getRenderer(TextRenderer.class).setText(questText);
    }

    @Override
    public void onEndScreen() {
    }
}
