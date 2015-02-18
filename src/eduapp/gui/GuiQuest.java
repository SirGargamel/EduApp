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
    private boolean descr, ending;
    private Element panelQuest;
    private String controlsDescription, controlsTitle;

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public void setDescriptionControls(String controlsTitle, String descriptionControls) {
        this.controlsDescription = descriptionControls;
        this.controlsTitle = controlsTitle;
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
            buildSimpleText(quest.getDescription().concat("\n"), controlsTitle, controlsDescription);
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
        tb.color(Color.WHITE);
        tb.build(nifty, current, panelQuest);
        for (QuestItem qi : quest.getData()) {
            if (!qi.isFinished()) {
                tb = new TextBuilder();
                tb.text(" - " + qi.toNiftyString());
                tb.style("base");
                tb.margin("5px");
                tb.width("100%");
                tb.textHAlignLeft();
                tb.wrap(true);
                if (qi.isFailed()) {
                    tb.color("#ff0000");
                } else {
                    tb.color("#000000");
                }

                tb.build(nifty, current, panelQuest);
            }
        }

        panelQuest.layoutElements();
    }

    private void buildSimpleText(final String... text) {
        for (Element e : panelQuest.getElements()) {
            e.markForRemoval();
        }

        Color c = Color.WHITE;
        for (String s : text) {
            TextBuilder tb;
            tb = new TextBuilder();
            tb.text(s);
            if (c == Color.BLACK) {
                tb.style("baseB");
            } else {
                tb.style("base");
            }
            tb.alignCenter();
            tb.wrap(true);
            tb.width("100%");
            tb.marginLeft("5px");
            tb.color(c);
            tb.build(nifty, nifty.getCurrentScreen(), panelQuest);

            c = c == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
    }

    @Override
    public void onEndScreen() {
    }
}
