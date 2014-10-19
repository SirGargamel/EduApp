package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import eduapp.level.item.ItemParameter;
import eduapp.level.item.Item;
import eduapp.level.quest.ConversionQuest;

/**
 *
 * @author Petr Ječmen
 */
public class GuiConversion implements ScreenController {

    private Nifty nifty;
    private ConversionQuest quest;
    private Element panelValues;

    public void setQuest(ConversionQuest quest) {
        this.quest = quest;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelValues = nifty.getCurrentScreen().findElementByName("panelValues");
    }

    @Override
    public void onStartScreen() {
        buildConversionTable();
    }

    private void buildConversionTable() {
        for (Element e : panelValues.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();

        final Item conversion = quest.getConversion();
        final String from = conversion.getParam(ItemParameter.FROM);

        PanelBuilder pb;
        TextBuilder tb;
        TextFieldBuilder tfb;
        Element e, text;

        tb = new TextBuilder();
        tb.style("baseB");
        tb.text(conversion.getParam(ItemParameter.DESCRIPTION));
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        e = tb.build(nifty, current, panelValues);
        panelValues.add(e);

        final RenderFont rf = nifty.createFont("interface/Fonts/Base20.fnt");
        for (Item it : quest.getItems()) {
            pb = new PanelBuilder("p".concat(it.getId()));
            pb.childLayoutHorizontal();
            e = pb.build(nifty, current, panelValues);

            pb = new PanelBuilder("pG1".concat(it.getId()));
            pb.width("1%");
            pb.build(nifty, current, e);

            tb = new TextBuilder("t".concat(it.getId()));
            tb.textHAlignLeft();
            tb.style("base");
            tb.text(it.getParam(from));
            tb.color(Color.WHITE);
            tb.width("30%");
            tb.build(nifty, current, e);

            pb = new PanelBuilder("pG2".concat(it.getId()));
            pb.width("29%");
            pb.build(nifty, current, e);

            tfb = new TextFieldBuilder("tf".concat(it.getId()));
            tfb.alignRight();
            tfb.width("40%");
            text = tfb.build(nifty, current, e);
            text.addInputHandler(new KeyInputHandler() {
                @Override
                public boolean keyEvent(NiftyInputEvent nie) {
                    return true;
                }
            });
            text.getElements().get(0).getElements().get(0).getRenderer(TextRenderer.class).setFont(rf);
        }

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        final String to = quest.getConversion().getParam(ItemParameter.TO);
        int counter = 0;
        TextField tf;
        String user, answer;
        for (Item it : quest.getItems()) {
            tf = nifty.getCurrentScreen().findNiftyControl("tf".concat(it.getId()), TextField.class);
            user = tf.getRealText().trim();
            answer = it.getParam(to);
            if (user.equalsIgnoreCase(answer)) {
                counter++;
                tf.getElement().getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#00ff00"));
            } else {
                tf.getElement().getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
            }
        }


        final int fCounter = counter;
        panelValues.startEffect(EffectEventId.onCustom, new EndNotify() {
            @Override
            public void perform() {
                quest.setResult(fCounter);
            }
        }, "Ok");

    }
}
