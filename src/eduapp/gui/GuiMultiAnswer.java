package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.level.quest.QuestQuestionMultiAnswer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiMultiAnswer implements ScreenController {

    private Nifty nifty;
    private QuestQuestionMultiAnswer quest;
    private Element panelValues;

    public void setQuestion(QuestQuestionMultiAnswer quest) {
        this.quest = quest;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelValues = nifty.getCurrentScreen().findElementByName("panelValues");
    }

    @Override
    public void onStartScreen() {
        buildAnswers();
    }

    private void buildAnswers() {
        for (Element e : panelValues.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();

        PanelBuilder pb;
        TextBuilder tb;
        CheckboxBuilder chb;
        Element e;

        tb = new TextBuilder();
        tb.style("baseB");
        tb.text(quest.getQuestion());
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        e = tb.build(nifty, current, panelValues);
        panelValues.add(e);

        for (String s : quest.getAnswers()) {
            pb = new PanelBuilder("p".concat(s));
            pb.childLayoutHorizontal();
            e = pb.build(nifty, current, panelValues);

            pb = new PanelBuilder("p1".concat(s));
            pb.width("2%");
            pb.build(nifty, current, e);

            chb = new CheckboxBuilder("chb".concat(s));
            chb.build(nifty, current, e);

            pb = new PanelBuilder("p2".concat(s));
            pb.width("2%");
            pb.build(nifty, current, e);

            tb = new TextBuilder("t".concat(s));
            tb.textHAlignLeft();
            tb.style("base");
            tb.text(s);
            tb.color(Color.BLACK);
            tb.width("50%");
            tb.build(nifty, current, e);
        }

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        int counter = 0;
        CheckBox chb;
        boolean result = true;
        final String[] correctAnswers = quest.getCorrectAnswers();
        Color c;
        final List<String> userInput = new LinkedList<>();
        for (String s : quest.getAnswers()) {
            chb = nifty.getCurrentScreen().findNiftyControl("chb".concat(s), CheckBox.class);
            if (chb.isChecked()) {
                userInput.add(s);
                if (!contains(s, correctAnswers)) {
                    result = false;
                    c = new Color("#ff0000");
                } else {
                    counter++;
                    c = new Color("#00ff00");
                }
            } else {
                if (contains(s, correctAnswers)) {
                    result = false;
                    c = new Color("#ff0000");
                } else {
                    c = new Color("#00ff00");
                }
            }
            chb.getElement().getRenderer(PanelRenderer.class).setBackgroundColor(c);            
            chb.setEnabled(false);            
        }        

        final int fCounter = counter;
        final boolean fResult = result;
        panelValues.startEffect(EffectEventId.onCustom, () -> {
            quest.setUserInput(userInput.toArray(new String[0]), fCounter == correctAnswers.length && fResult);
        }, "Ok");

    }

    private boolean contains(final String str, final String[] data) {
        boolean result = false;
        for (String s : data) {
            if (str.equals(s)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
