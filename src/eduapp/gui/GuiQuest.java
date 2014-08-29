package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
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
        TextBuilder tb;
        tb = new TextBuilder();
        tb.text("-- Seznam úkolů --");
        tb.style("baseB");
        tb.alignCenter();
        tb.color("#ffffffff");        
        tb.build(nifty, current, panelQuest);
        for (QuestItem qi : quest.getData()) {
            if (!qi.isFinished()) {
                tb = new TextBuilder();
                tb.text(" - " + qi.toNiftyString());
                tb.style("base");
                tb.margin("10");
                if (qi.isFailed()) {
                    tb.color("#ff0000ff");
                } else {
                    tb.color("#ffffffff");
                }

                tb.build(nifty, current, panelQuest);
            }
        }

        panelQuest.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }
}
