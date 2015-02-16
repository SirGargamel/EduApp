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
import eduapp.level.quest.QuestPicking;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Petr Ječmen
 */
public class GuiPicking implements ScreenController, DroppableDropFilter {

    private static final String TAG_DESCR = "drop";
    private static final int COUNT_ITEMS_ROW = 3;
    private QuestPicking quest;
    private Element panelValues;
    private Nifty nifty;

    public void setData(QuestPicking gq) {
        this.quest = gq;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelValues = nifty.getCurrentScreen().findElementByName("panelValues");
    }

    @Override
    public void onStartScreen() {
        buildItems();
    }

    private void buildItems() {
        for (Element e : panelValues.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();
        final Map<String, List<String>> data = quest.getData();

        DroppableBuilder db;
        DraggableBuilder dgb;
        PanelBuilder pb;
        EffectBuilder eb;
        TextBuilder tb;
        Element e = null, el;
        String key;
        List<String> temp = new ArrayList<>();

        tb = new TextBuilder();
        tb.style("baseB");
        tb.text(quest.getQuestion());
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        tb.build(nifty, current, panelValues);

        int counter = COUNT_ITEMS_ROW;
        for (Entry<String, List<String>> en : data.entrySet()) {
            key = en.getKey();

            counter++;
            if (counter >= COUNT_ITEMS_ROW) {
                pb = new PanelBuilder("pGroup" + counter);
                pb.childLayoutHorizontal();
                e = pb.build(nifty, current, panelValues);
                counter = 0;

                pb = new PanelBuilder("pGap1".concat(key));
                pb.height("3%");
                pb.build(nifty, current, panelValues);
            }

            pb = new PanelBuilder("p".concat(key));
            pb.childLayoutVertical();
            pb.valignTop();
            el = pb.build(nifty, current, e);

            tb = new TextBuilder("t".concat(key));
            tb.wrap(true);
            tb.style("base");
            tb.text(en.getKey());
            tb.color(Color.WHITE);
            tb.build(nifty, current, el);

            pb = new PanelBuilder("pGap1".concat(key));
            pb.height("1%");
            pb.build(nifty, current, el);

            db = new DroppableBuilder(TAG_DESCR + key);
            db.height("20%");
            db.alignCenter();
            db.childLayoutCenter();
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#000000");
            eb.effectParameter("border", "1px");
            db.onActiveEffect(eb);
            db.build(nifty, current, el).getNiftyControl(Droppable.class).addFilter(this);

            pb = new PanelBuilder("pD".concat(key));
            pb.childLayoutVertical();
            pb.valignTop();
            pb.height("60%");
            el = pb.build(nifty, current, el);

            temp.clear();
            temp.addAll(en.getValue());
            Collections.shuffle(temp);
            for (String s2 : temp) {
                pb = new PanelBuilder("pGap" + s2);
                pb.height("3%");
                pb.build(nifty, current, el);

                dgb = new DraggableBuilder(s2);
                tb = new TextBuilder("text" + s2);
                tb.text(s2);
                tb.style("base");
                dgb.text(tb);
                dgb.childLayoutCenter();
                dgb.alignCenter();
                dgb.build(nifty, current, el);
            }
        }

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        int counter = 0;
        final Map<String, List<String>> data = quest.getData();

        Element el, el2;
        for (Entry<String, List<String>> e : data.entrySet()) {
            el = nifty.getCurrentScreen().findElementByName(TAG_DESCR.concat(e.getKey()));
            el2 = extractElement(el);
            if (el2.getId().equals(e.getValue().get(0))) {
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

    private Element extractElement(final Element container) {
        Element result = null;
        if (!container.getElements().isEmpty() && !container.getElements().get(0).getElements().isEmpty()) {
            result = container.getElements().get(0).getElements().get(0);
        }
        return result;
    }

    @Override
    public boolean accept(Droppable dropSource, Draggable draggedItem, Droppable droppedAt) {
        return droppedAt.getElement().getElements().get(0).getElements().isEmpty();
    }
}
