package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
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
import eduapp.level.quest.QuestMatching;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Petr Ječmen
 */
public class GuiMatching implements ScreenController, DroppableDropFilter {

    private static final String TAG_DESCR = "drop";
    private Nifty nifty;
    private QuestMatching quest;
    private Element panelValues;

    public void setQuest(QuestMatching quest) {
        this.quest = quest;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelValues = nifty.getCurrentScreen().findElementByName("panelValues");
    }

    @Override
    public void onStartScreen() {
        buildMatchingTable();
    }

    private void buildMatchingTable() {
        for (Element e : panelValues.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();

        final Map<String, String> data = quest.getData();
        final List<String> items = new ArrayList<>(data.keySet());
        final List<String> descr = new ArrayList<>(data.values());        

        PanelBuilder pb;
        TextBuilder tb;
        Element e, el;
        DroppableBuilder db;
        DraggableBuilder dgb;
        EffectBuilder eb;

        tb = new TextBuilder();
        tb.style("baseB");
        tb.text("Přiřadtě ke každé položce správný popis.");
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        tb.build(nifty, current, panelValues);
        
        pb = new PanelBuilder("pGap");
        pb.height("1%");
        pb.build(nifty, current, panelValues);
        

        String s;
        for (int i = 0; i < items.size(); i++) {
            s = items.get(i);

            pb = new PanelBuilder("p".concat(s));
            pb.childLayoutHorizontal();
            e = pb.build(nifty, current, panelValues);

            pb = new PanelBuilder("pG1".concat(s));
            pb.width("1%");
            pb.build(nifty, current, e);

            db = new DroppableBuilder(TAG_DESCR + s);
            db.width("30%");
            db.alignCenter();
            db.childLayoutCenter();
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#000000");
            eb.effectParameter("border", "1px");
            db.onActiveEffect(eb);
            el = db.build(nifty, current, e);
            el.getNiftyControl(Droppable.class).addFilter(this);

            pb = new PanelBuilder("pG2".concat(s));
            pb.width("2%");
            pb.build(nifty, current, e);

            tb = new TextBuilder("t".concat(s));
            tb.textHAlignLeft();
            tb.wrap(true);
            tb.alignRight();
            tb.style("base");
            tb.text(descr.get(i));
            tb.color(Color.WHITE);
            tb.width("65%");
            tb.build(nifty, current, e);
        }

        pb = new PanelBuilder("pGap2");
        pb.height("2%");
        pb.build(nifty, current, panelValues);
        
        Collections.shuffle(items);

        pb = new PanelBuilder("pItems");
        pb.childLayoutHorizontal();
        e = pb.build(nifty, current, panelValues);
        for (String item : items) {
            dgb = new DraggableBuilder(item);
            tb = new TextBuilder("text" + item);
            tb.text(item);
            tb.style("base");
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            dgb.onActiveEffect(eb);
            dgb.text(tb);
            dgb.childLayoutCenter();
            dgb.build(nifty, current, e);

            pb = new PanelBuilder("pGap".concat(item));
            pb.width("2%");
            pb.build(nifty, current, e);
        }

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        int counter = 0;
        final Map<String, String> data = quest.getData();
        
        Element el, el2;
        for (Entry<String, String> e : data.entrySet()) {
            el = nifty.getCurrentScreen().findElementByName(TAG_DESCR.concat(e.getKey()));
            el2 = extractElement(el);            
            if (el2.getId().equals(e.getKey())) {
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
