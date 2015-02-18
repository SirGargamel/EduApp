package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.level.quest.QuestAdding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiAdding implements ScreenController, DroppableDropFilter {

    private static final String TAG_DESCR = "drop";
    private static final String SPLITTER_INNER = "[*]";
    private Nifty nifty;
    private QuestAdding quest;
    private Element panelValues;

    public void setQuest(QuestAdding quest) {
        this.quest = quest;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelValues = nifty.getCurrentScreen().findElementByName("panelValues");
    }

    @Override
    public void onStartScreen() {
        buildText();
    }

    private void buildText() {
        for (Element e : panelValues.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();

        final String[] data = quest.getText();
        final String[] fill = quest.getFill();

        PanelBuilder pb;
        TextBuilder tb;
        Element e, el;
        DroppableBuilder db;
        DraggableBuilder dgb;
        EffectBuilder eb;

        tb = new TextBuilder();
        tb.style("baseB");
        tb.text("Doplňte chybějící slova do textu.");
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        tb.build(nifty, current, panelValues);

        pb = new PanelBuilder("pGap");
        pb.height("1%");
        pb.build(nifty, current, panelValues);

        int dataPointer = 0;
        String[] split;
        String s;
        for (int row = 0; row < data.length; row++) {
            pb = new PanelBuilder("p" + row);
            pb.childLayoutHorizontal();
            pb.alignLeft();
            pb.valignCenter();
            pb.height("15%");
            e = pb.build(nifty, current, panelValues);

            pb = new PanelBuilder("pG" + row + ":");
            pb.width("1%");
            pb.build(nifty, current, e);

            split = data[row].split(SPLITTER_INNER);
            for (int i = 0; i < split.length; i++) {
                s = split[i];

                pb = new PanelBuilder("pG" + row + ":" + i);
                pb.width("1%");
                pb.build(nifty, current, e);

                tb = new TextBuilder("text" + row + ":" + i);
                tb.valignCenter();
                tb.style("base");
                tb.text(s);
                tb.color(Color.BLACK);
                tb.build(nifty, current, e);

                if (i < split.length - 1) {
                    s = fill[dataPointer];
                    dataPointer++;

                    pb = new PanelBuilder("pG".concat(s));
                    pb.width("1%");
                    pb.build(nifty, current, e);

                    db = new DroppableBuilder(TAG_DESCR + s);
                    db.width("20%");
                    db.height("75%");
                    db.valignCenter();
                    db.childLayoutCenter();
                    eb = new EffectBuilder("border");
                    eb.effectParameter("color", "#ffffff");
                    eb.effectParameter("border", "1px");
                    db.onActiveEffect(eb);
                    el = db.build(nifty, current, e);
                    el.getNiftyControl(Droppable.class).addFilter(this);
                }
            }
        }

        pb = new PanelBuilder("pGap2");
        pb.height("2%");
        pb.build(nifty, current, panelValues);

        final List<String> fillList = new ArrayList<>(fill.length);
        fillList.addAll(Arrays.asList(fill));
        Collections.shuffle(fillList);

        pb = new PanelBuilder("pItems");
        pb.childLayoutHorizontal();        
        e = pb.build(nifty, current, panelValues);
        for (String item : fillList) {
            pb = new PanelBuilder("pGap".concat(item));
            pb.width("2%");
            pb.build(nifty, current, e);

            dgb = new DraggableBuilder(item);
            tb = new TextBuilder("text" + item);
            tb.text(item);
            tb.style("base");
            tb.color(Color.BLACK);
            tb.margin("2px");
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            dgb.onActiveEffect(eb);
            dgb.text(tb);
            dgb.childLayoutCenter();
            dgb.valignCenter();
            dgb.build(nifty, current, e);
        }

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        int counter = 0;
        final String[] fill = quest.getFill();

        Element el, el2;
        for (String s : fill) {
            el = nifty.getCurrentScreen().findElementByName(TAG_DESCR.concat(s));
            el2 = extractElement(el);
            if (el2.getId().equals(s)) {
                counter++;
                el2.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#00ff00"));
            } else {
                el2.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
            }
        }

        final int fCounter = counter;
        panelValues.startEffect(EffectEventId.onCustom, () -> {
            quest.setResult(fCounter);
        }, "Ok");
    }

    @Override
    public boolean accept(Droppable dropSource, Draggable draggedItem, Droppable droppedAt) {
        return droppedAt.getElement().getElements().get(0).getElements().isEmpty();
    }

    private Element extractElement(final Element container) {
        Element result = null;
        if (!container.getElements().isEmpty() && !container.getElements().get(0).getElements().isEmpty()) {
            result = container.getElements().get(0).getElements().get(0);
        }
        return result;
    }
}
