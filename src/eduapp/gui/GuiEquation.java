package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.dragndrop.DroppableControl;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.AppContext;
import eduapp.FlowManager;
import eduapp.ItemRegistry;
import eduapp.level.item.ItemParameters;
import eduapp.level.quest.EquationQuest;
import eduapp.level.quest.EquationQuest.DragItem;
import eduapp.level.quest.EquationQuest.Mode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiEquation implements ScreenController, DroppableDropFilter {

    private static final String DROP_ID = "drop-";
    private static final String PANEL_ID = "panel-";
    private static final String TEXT_ID = "text-";
    private static final String SIZE_WIDTH = "100px";
    private static final String SIZE_HEIGHT = "100px";
    private static final String SIZE_GAP = "10px";
    private Nifty nifty;
    private EquationQuest quest;
    private Element panelDrag, panelDrop;

    public void setData(EquationQuest quest) {
        this.quest = quest;
    }    

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelDrag = nifty.getCurrentScreen().findElementByName("panelDrag");
        panelDrop = nifty.getCurrentScreen().findElementByName("panelDrop");
    }

    @Override
    public void onStartScreen() {
        buildDrag();
    }

    private void buildDrag() {
        for (Element e : panelDrag.getElements()) {
            e.markForRemoval();
        }
        for (Element e : panelDrop.getElements()) {
            e.markForRemoval();
        }

        final Screen current = nifty.getCurrentScreen();

        DroppableBuilder db;
        DraggableBuilder dgb;
        PanelBuilder pb;
        TextBuilder tb;
        Element d, dg;
        String text;
        for (DragItem di : quest.getItems()) {
            // target box
            text = di.getText();
            switch (di.getType()) {
                case DRAG:
                    db = new DroppableBuilder(DROP_ID.concat(text));
                    db.width(SIZE_WIDTH);
                    db.height(SIZE_HEIGHT);
                    db.padding(SIZE_GAP);
                    db.marginLeft(SIZE_GAP);
                    db.backgroundColor("#00ffff");
                    db.valignCenter();
                    db.childLayoutCenter();
                    db.build(nifty, current, panelDrop).getNiftyControl(Droppable.class).addFilter(this);
                    break;
                case STATIC:
                    pb = new PanelBuilder(PANEL_ID.concat(di.getText()));
                    pb.width(SIZE_WIDTH);
                    pb.height(SIZE_HEIGHT);
                    pb.padding(SIZE_GAP);
                    pb.marginLeft(SIZE_GAP);
                    pb.valignCenter();
                    pb.childLayoutCenter();
                    d = pb.build(nifty, current, panelDrop);

                    tb = new TextBuilder(TEXT_ID.concat(text));
                    tb.text(text);
                    tb.style("base");
                    tb.color("#000000");
                    tb.build(nifty, current, d);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported type od DragItemType - " + di.getType());
            }
        }

        // bottom row    
        List<String> items = new LinkedList<>();
        for (DragItem di : quest.getItems()) {
            if (di.getType().equals(EquationQuest.DragItemType.DRAG)) {
                items.add(di.getText());
            }
        }
        for (String s : quest.getExtra()) {
            items.add(s);
        }
        Collections.shuffle(items);

        final ItemRegistry ir = AppContext.getItemRegistry();
        final Mode mode = quest.getMode();
        ImageBuilder ib;
        String key;
        for (String s : items) {
            db = new DroppableBuilder("d".concat(s));
            db.width(SIZE_WIDTH);
            db.height(SIZE_HEIGHT);
            db.padding(SIZE_GAP);
            db.backgroundColor("#00ffff");
            db.valignCenter();
            db.childLayoutCenter();
            db.margin(SIZE_GAP);

            dgb = new DraggableBuilder(s);
            dgb.width(SIZE_WIDTH);
            dgb.height(SIZE_HEIGHT);
            dgb.padding(SIZE_GAP);            
            dgb.valignCenter();
            dgb.childLayoutCenter();

            d = db.build(nifty, current, panelDrag);

            switch (mode) {
                case text:
                    tb = new TextBuilder(TEXT_ID.concat(s));
                    tb.text(ir.get(s).getParam(ItemParameters.FORMULA));
                    tb.style("base");
                    tb.color("#000000");

                    dg = dgb.build(nifty, current, d);
                    tb.build(nifty, current, dg);
                    break;
                case ikony:
                    key = "icons/" + ir.get(s).getParam(ItemParameters.ICON);
                    
                    
                    ib = new ImageBuilder("i".concat(s));
                    ib.filename(key);                    
                    ib.width("100%");
                    ib.height("100%");
                    dgb.image(ib);

                    dgb.build(nifty, current, d);
                    break;
            }
        }

        panelDrag.layoutElements();
        panelDrop.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        boolean result = true;
        Element el;
        String name;
        for (Element e : panelDrop.getElements()) {
            if (e.getControl(DroppableControl.class) != null) {
                name = e.getId().replace(DROP_ID, "");
                if (e.getElements().get(0).getElements().size() == 1) {
                    el = e.getElements().get(0).getElements().get(0);
                    if (!el.getId().equals(name)) {
                        result = false;
                        break;
                    }
                } else {
                    result = false;
                    break;
                }
            }
        }
        quest.setFinished(result);
        FlowManager.getInstance().finishDrag(result);
    }

    @Override
    public boolean accept(Droppable dropSource, Draggable draggedItem, Droppable droppedAt) {
        return droppedAt.getElement().getElements().get(0).getElements().isEmpty();
    }
}
