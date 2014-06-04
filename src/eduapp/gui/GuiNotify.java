package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;

/**
 *
 * @author Petr Ječmen
 */
public class GuiNotify implements ScreenController {

    private static final String NAME_QUESTION_PANEL = "panelQuest";
    private static final String NAME_QUESTION_TEXT = "questText";    
    private String notification;
    private Element questionText;
    private Element panelQuest;

    public void setMessage(String notification) {
        this.notification = notification;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        panelQuest = nifty.getCurrentScreen().findElementByName(NAME_QUESTION_PANEL);
        questionText = nifty.getCurrentScreen().findElementByName(NAME_QUESTION_TEXT);        
    }

    @Override
    public void onStartScreen() {
        questionText.getRenderer(TextRenderer.class).setText(notification);
        panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
            @Override
            public void perform() {
                panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                    @Override
                    public void perform() {
                        FlowManager.enableGame(true);
                    }
                }, "Hide");
            }
        }, "Show");
    }

    @Override
    public void onEndScreen() {
    }
}
