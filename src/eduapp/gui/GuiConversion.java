package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.FlowManager;
import eduapp.level.ItemParameters;
import eduapp.level.Item;
import eduapp.level.quest.ConversionQuest;

/**
 *
 * @author Petr Ječmen
 */
public class GuiConversion implements ScreenController {

    private static final String SIZE_LINE_HEIGHT = "20";
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
        final String from = conversion.getParam(ItemParameters.FROM);

        PanelBuilder pb;
        TextBuilder tb;
        TextFieldBuilder tfb;
        Element e, text;

        tb = new TextBuilder();
        tb.font("interface/Fonts/BaseB.fnt");
        tb.text(conversion.getParam(ItemParameters.DESCRIPTION));
        tb.alignCenter();
        tb.color(Color.BLACK);
        e = tb.build(nifty, current, panelValues);
        panelValues.add(e);

        for (Item it : quest.getItems()) {
            tb = new TextBuilder("t".concat(it.getId()));
            tb.width("10%");
            tb.textHAlignLeft();
            tb.font("interface/Fonts/Base.fnt");
            tb.text(it.getParam(from));
            tb.color(Color.WHITE);
            tb.height(SIZE_LINE_HEIGHT);

            tfb = new TextFieldBuilder("tf".concat(it.getId()));
            tfb.width("30%");
            tfb.height(SIZE_LINE_HEIGHT);
            tfb.alignRight();

            pb = new PanelBuilder("p".concat(it.getId()));
            pb.childLayoutHorizontal();
            pb.backgroundColor(Color.BLACK);
            pb.width("100%");
            e = pb.build(nifty, current, panelValues);

            pb = new PanelBuilder("ps".concat(it.getId()));
            pb.width("10%");
            pb.height(SIZE_LINE_HEIGHT);

            e.add(tb.build(nifty, current, e));
            e.add(pb.build(nifty, current, e));

            text = tfb.build(nifty, current, e);
            text.addInputHandler(new KeyInputHandler() {
                @Override
                public boolean keyEvent(NiftyInputEvent nie) {
                    return true;
                }
            });
            e.add(text);

            panelValues.add(e);
        }

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        final String to = quest.getConversion().getParam(ItemParameters.TO);
        int counter = 0;
        TextField tf;
        String user, answer;
        for (Item it : quest.getItems()) {
            tf = nifty.getCurrentScreen().findNiftyControl("tf".concat(it.getId()), TextField.class);
            user = tf.getRealText().trim();
            answer = it.getParam(to);
            if (user.equalsIgnoreCase(answer)) {
                counter++;
            }
        }
        FlowManager.getInstance().finishConversion(counter, quest.getItems().length);
    }
}
