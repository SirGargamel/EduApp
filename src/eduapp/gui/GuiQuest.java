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
    private boolean descr, ending;
    private Element panelQuest;
    private String descriptionControls;

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public void setDescriptionControls(String descriptionControls) {
        this.descriptionControls = descriptionControls;
    }

    public void displayDescription() {
        descr = true;
    }

    public void displayEnding() {
        ending = true;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelQuest = nifty.getCurrentScreen().findElementByName("panelQuest");
    }

    @Override
    public void onStartScreen() {
        if (descr) {
            buildSimpleText(quest.getDescription() + "\n\n" + descriptionControls);
            descr = false;
        } else if (ending) {
            buildSimpleText(quest.getEnding());
            ending = false;
        } else {
            buildQuestText();
        }
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

    private void buildSimpleText(final String text) {
        for (Element e : panelQuest.getElements()) {
            e.markForRemoval();
        }

        TextBuilder tb;
        tb = new TextBuilder();
        tb.text(text);
        tb.style("base");
        tb.alignCenter();
        tb.marginLeft("5px");
        tb.color("#ffffffff");
        tb.build(nifty, nifty.getCurrentScreen(), panelQuest);
    }

    @Override
    public void onEndScreen() {
    }
}
