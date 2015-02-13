package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
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
import eduapp.level.quest.QuestOrdering;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiOrdering implements ScreenController, DroppableDropFilter {

    private QuestOrdering quest;
    private Element panelValues;
    private Nifty nifty;

    public void setData(QuestOrdering gq) {
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

        DroppableBuilder db;
        DraggableBuilder dgb;
        EffectBuilder eb;
        TextBuilder tb;
        Element e;

        tb = new TextBuilder();
        tb.style("baseB");
        tb.text(quest.getQuestion());
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        tb.build(nifty, current, panelValues);
        
        final List<String> data = new ArrayList<>();
        for (String s : quest.getData()) {
            data.add(s);
        }
        Collections.shuffle(data);

        for (String s : data) {
            db = new DroppableBuilder("drop" + s);
            db.width("50%");
            db.height("10%");
            db.alignCenter();
            db.childLayoutCenter();
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#000000");
            eb.effectParameter("border", "1px");
            db.onActiveEffect(eb);
            e = db.build(nifty, current, panelValues);
            e.getNiftyControl(Droppable.class).addFilter(this);

            dgb = new DraggableBuilder(s);
            tb = new TextBuilder("text" + s);
            tb.text(s);
            tb.style("base");
            dgb.text(tb);
            dgb.childLayoutCenter();
            dgb.build(nifty, current, e.getElements().get(0));
        }

        db = new DroppableBuilder("drop");
        db.width("50%");
        db.height("10%");
        db.alignCenter();
        db.childLayoutCenter();
        eb = new EffectBuilder("border");
        eb.effectParameter("color", "#000000");
        eb.effectParameter("border", "1px");
        db.onActiveEffect(eb);
        e = db.build(nifty, current, panelValues);
        e.getNiftyControl(Droppable.class).addFilter(this);

        panelValues.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        final String[] data = quest.getData();
        final List<Element> elements = panelValues.getElements();
        int correctCount = 0;
        int dataPointer = 0;
        Element e;
        String text;
        for (int i = 1; i < elements.size(); i++) {
            e = extractElement(elements.get(i));
            if (e == null) {
                continue;
            }
            text = e.getId();
            if (text.equals(data[dataPointer])) {
                correctCount++;
                e.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#00ff00"));
            } else {
                e.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
            }
            dataPointer++;
        }
        final int fCorrectCount = correctCount;
        panelValues.startEffect(EffectEventId.onCustom, () -> {
            quest.setResult(fCorrectCount);
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
