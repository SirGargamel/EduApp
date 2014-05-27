package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;

/**
 *
 * @author Petr Ječmen
 */
public class GuiQuest implements ScreenController {

    private Nifty nifty;
    private Quest quest;
    private Element panelQuest;

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelQuest = nifty.getCurrentScreen().findElementByName("panelQuest");
    }

    @Override
    public void onStartScreen() {
        buildQuestText();
    }

    private void buildQuestText() {
        for (Element e : panelQuest.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();
        TextBuilder tb = new TextBuilder();
        tb.text(quest.getName());
        tb.font("interface/Fonts/BaseB.fnt");
        tb.alignCenter();
        tb.color(Color.BLACK);
        Element e = tb.build(nifty, current, panelQuest);
        panelQuest.add(e);

        for (QuestItem qi : quest.getData()) {
            tb = new TextBuilder();
            tb.text(qi.toNiftyString());
            tb.font("interface/Fonts/Base.fnt");
            if (qi.isFinished()) {
                tb.color(Color.BLACK);
            }
            tb.marginLeft("5");
            e = tb.build(nifty, current, panelQuest);
            panelQuest.add(e);
        }

        panelQuest.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }
}
