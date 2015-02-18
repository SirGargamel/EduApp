package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
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
import de.lessvoid.nifty.tools.Color;
import eduapp.AppContext;
import eduapp.ItemRegistry;
import eduapp.Utils;
import eduapp.level.item.ItemParameter;
import eduapp.level.quest.QuestEquation;
import eduapp.level.quest.QuestEquation.Equation;
import eduapp.level.quest.QuestEquation.Mode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiEquation implements ScreenController, DroppableDropFilter {

    private static final String DROP_ID_IN = "drop-in-";
    private static final String DROP_ID_OUT = "drop-out-";
    private static final String DROP_ID_CAT = "drop-cat-";
    private static final String PANEL_ID = "panel-";
    private static final String PANEL_BOTTOM_ID = "panelBottom-";
    private static final String SUB_PANEL_ID = "subpanel-";
    private static final String TEXT_ID = "text-";
    private static final String SIZE_UNIT = "px";
    private static final int SIZE_GAP = 5;
    private static final String ADD = "+";
    private static final String SPLITTER = "===";
    private Nifty nifty;
    private QuestEquation quest;
    private Element panelDrag, panelDrop;
    private int itemWidth = 32, itemHeight = 32, itemsPerLine = 5;

    public void setQuest(QuestEquation quest) {
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
        //calculate sizes
        final int width = nifty.getRenderEngine().getWidth() * 9 / 10;

        int maxItemCount = 0;
        int counter;
        for (Equation eq : quest.getEquations()) {
            counter = 0;
            counter += eq.getInputs().size();
            counter += eq.getOutputs().size();
            if (counter > maxItemCount) {
                maxItemCount = counter;
            }
        }

        int widthWithoutGaps = width - maxItemCount * 2 * SIZE_GAP;
        itemWidth = widthWithoutGaps / (maxItemCount * 2);
        itemHeight = quest.getMode().equals(Mode.text) ? 32 : itemWidth;
        itemsPerLine = width / (itemWidth + SIZE_GAP) - 1;

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
        final Mode mode = quest.getMode();

        TextBuilder tb = new TextBuilder();
        tb.style("baseB");
        tb.text(quest.toNiftyString());
        tb.alignCenter();
        tb.valignCenter();
        tb.color(Color.WHITE);
        tb.build(nifty, current, panelDrop);

        PanelBuilder pb = new PanelBuilder("gap");
        pb.height("2%");
        pb.build(nifty, current, panelDrop);
        // target boxes        
        Element dg, p = null;
        int panelHeight = panelDrop.getHeight() / quest.getEquations().size();
        int counter = 0;
        for (Equation eq : quest.getEquations()) {
            pb = new PanelBuilder(PANEL_ID.concat(Integer.toString(counter++)));
            pb.height(buildSize(panelHeight));
            pb.valignCenter();
            pb.childLayoutHorizontal();
            p = pb.build(nifty, current, panelDrop);

            buildEquationPart(eq.getInputs(), current, true, p);
            pb = new PanelBuilder(PANEL_ID.concat(Integer.toString(counter++)));
            pb.width(buildSize(SIZE_GAP));
            pb.build(nifty, current, p);
            buildCatalysts(eq.getCatalysts(), current, p);
            pb = new PanelBuilder(PANEL_ID.concat(Integer.toString(counter++)));
            pb.width(buildSize(SIZE_GAP * 2));
            pb.build(nifty, current, p);
            buildEquationPart(eq.getOutputs(), current, false, p);
        }

        // bottom row
        final List<String> items = new LinkedList<>();
        for (Equation eq : quest.getEquations()) {
            eq.getInputs().stream().forEach((s) -> {
                items.add(s);
            });
            for (String s : eq.getOutputs()) {
                items.add(s);
            }
            for (String s : eq.getCatalysts()) {
                items.add(s);
            }
        }
        for (String s : quest.getExtra()) {
            items.add(s);
        }
        Collections.shuffle(items);

        final ItemRegistry ir = AppContext.getItemRegistry();

        ImageBuilder ib;
        DraggableBuilder dgb;
        EffectBuilder eb;

        counter = itemsPerLine;
        panelHeight = panelDrag.getHeight() / (int) (Math.ceil(items.size() / (double) itemsPerLine));
        for (String s : items) {
            if (counter >= itemsPerLine) {
                pb = new PanelBuilder(PANEL_BOTTOM_ID.concat(Integer.toString(counter++)));
                pb.height(buildSize(panelHeight));
                pb.childLayoutHorizontal();
                pb.valignCenter();
                p = pb.build(nifty, current, panelDrag);
                counter = 0;
            }
            counter++;

            pb = new PanelBuilder(PANEL_ID.concat(Integer.toString(counter)));
            pb.width(buildSize(SIZE_GAP * 2));
            pb.build(nifty, current, p);

            dgb = new DraggableBuilder(s);
            dgb.width(buildSize(itemWidth));
            dgb.height(buildSize(itemHeight));
            dgb.padding(buildSize(SIZE_GAP));
            dgb.valignCenter();
            dgb.childLayoutCenter();
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            dgb.onActiveEffect(eb);

            switch (mode) {
                case text:
                    tb = new TextBuilder(TEXT_ID.concat(s));
                    tb.text(ir.get(s).getParam(ItemParameter.FORMULA));
                    tb.style("base");
                    tb.color("#000000");

                    dg = dgb.build(nifty, current, p);
                    tb.build(nifty, current, dg);
                    break;
                case ikony:
                    ib = new ImageBuilder("i".concat(s));
                    ib.filename(Utils.generateIconFilename(s));
                    ib.width("100%");
                    ib.height("100%");
                    final HoverEffectBuilder heb = new HoverEffectBuilder("hint");
                    heb.getAttributes().setAttribute("hintDelay", "50");
                    heb.getAttributes().setAttribute("hintText", ir.get(s).getParam(ItemParameter.NAME));
                    dgb.onHoverEffect(heb);
                    dgb.image(ib);

                    dgb.build(nifty, current, p);
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
        int counter = 0;
        Equation eq;
        for (Element elm : panelDrop.getElements()) {
            eq = quest.getEquations().get(counter);
            counter++;

            for (Element e : elm.getElements()) {
                if (e.getControl(DroppableControl.class) != null) {
                    name = e.getId();
                    if (name.contains(DROP_ID_IN)) {
                        if (e.getElements().get(0).getElements().size() == 1) {
                            el = e.getElements().get(0).getElements().get(0);
                            if (!eq.getInputs().contains(el.getId())) {
                                result = false;
                                break;
                            }
                        } else {
                            result = false;
                            break;
                        }
                    } else if (name.contains(DROP_ID_OUT)) {
                        if (e.getElements().get(0).getElements().size() == 1) {
                            el = e.getElements().get(0).getElements().get(0);
                            if (!eq.getOutputs().contains(el.getId())) {
                                result = false;
                                break;
                            }
                        } else {
                            result = false;
                            break;
                        }
                    } else if (name.contains(DROP_ID_CAT)) {
                        if (e.getElements().get(0).getElements().size() == 1) {
                            el = e.getElements().get(0).getElements().get(0);
                            if (!eq.getCatalysts().contains(el.getId())) {
                                result = false;
                                break;
                            }
                        } else {
                            result = false;
                            break;
                        }
                    }
                }
            }
        }
        quest.setResult(result);
    }

    @Override
    public boolean accept(Droppable dropSource, Draggable draggedItem, Droppable droppedAt) {
        return droppedAt.getElement().getElements().get(0).getElements().isEmpty();
    }

    private void buildEquationPart(List<String> data, final Screen current, boolean input, Element panel) {
        String text;
        DroppableBuilder db;
        PanelBuilder pb;
        Element p;
        TextBuilder tb;
        EffectBuilder eb;
        for (int i = 0; i < data.size() - 1; i++) {
            text = data.get(i);
            if (input) {
                db = new DroppableBuilder(DROP_ID_IN.concat(text).concat(Integer.toString(i)));
            } else {
                db = new DroppableBuilder(DROP_ID_OUT.concat(text).concat(Integer.toString(i)));
            }
            db.width(buildSize(itemWidth));
            db.height(buildSize(itemHeight));
            db.padding(buildSize(SIZE_GAP));
            db.margin(buildSize(SIZE_GAP));
            db.valignCenter();
            db.childLayoutCenter();
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            db.onActiveEffect(eb);
            db.build(nifty, current, panel).getNiftyControl(Droppable.class).addFilter(this);

            pb = new PanelBuilder(SUB_PANEL_ID.concat(text));
            pb.width(buildSize(itemWidth));
            pb.height(buildSize(itemHeight));
            pb.padding(buildSize(SIZE_GAP));
            pb.margin(buildSize(SIZE_GAP));
            pb.valignCenter();
            pb.childLayoutCenter();
            p = pb.build(nifty, current, panel);

            tb = new TextBuilder(TEXT_ID.concat(ADD).concat(text).concat(Integer.toString(i)));
            tb.text(ADD);
            tb.style("base");
            tb.color("#000000");
            tb.build(nifty, current, p);
        }
        text = data.get(data.size() - 1);
        if (input) {
            db = new DroppableBuilder(DROP_ID_IN.concat(text));
        } else {
            db = new DroppableBuilder(DROP_ID_OUT.concat(text));
        }
        db.width(buildSize(itemWidth));
        db.height(buildSize(itemHeight));
        db.padding(buildSize(SIZE_GAP));
        db.margin(buildSize(SIZE_GAP));
        db.valignCenter();
        db.childLayoutCenter();
        eb = new EffectBuilder("border");
        eb.effectParameter("color", "#ffffff");
        eb.effectParameter("border", "1px");
        db.onActiveEffect(eb);
        db.build(nifty, current, panel).getNiftyControl(Droppable.class).addFilter(this);
    }

    private String buildSize(final int value) {
        return Integer.toString(value).concat(SIZE_UNIT);
    }

    private void buildCatalysts(final List<String> data, final Screen current, final Element panel) {
        PanelBuilder pb;
        Element e;
        TextBuilder tb;
        EffectBuilder eb;

        pb = new PanelBuilder(SUB_PANEL_ID.concat(SPLITTER));
        pb.width(buildSize(itemWidth));
        pb.padding(buildSize(SIZE_GAP));
        pb.margin(buildSize(SIZE_GAP));
        pb.valignCenter();
        pb.childLayoutVertical();
        e = pb.build(nifty, current, panel);

        DroppableBuilder db;
        if (!data.isEmpty()) {
            db = new DroppableBuilder(DROP_ID_CAT.concat(data.get(0)));
            db.width(buildSize(itemWidth));
            db.height(buildSize(itemHeight));
            db.padding(buildSize(SIZE_GAP));
            db.margin(buildSize(SIZE_GAP));
            db.childLayoutCenter();
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            db.onActiveEffect(eb);
            db.build(nifty, current, e).getNiftyControl(Droppable.class).addFilter(this);
        } else {
            pb = new PanelBuilder(SUB_PANEL_ID.concat(SPLITTER));
            pb.width(buildSize(itemWidth));
            pb.height(buildSize(itemHeight));
            pb.padding(buildSize(SIZE_GAP));
            pb.margin(buildSize(SIZE_GAP));
            pb.build(nifty, current, e);
        }

        tb = new TextBuilder(TEXT_ID.concat(SPLITTER));
        tb.text(SPLITTER);
        tb.alignCenter();
        tb.style("base");
        tb.color("#000000");
        tb.padding(buildSize(SIZE_GAP));
        tb.margin(buildSize(SIZE_GAP));
        tb.build(nifty, current, e);

        if (data.size() == 2) {
            db = new DroppableBuilder(DROP_ID_CAT.concat(data.get(1)));
            db.width(buildSize(itemWidth));
            db.height(buildSize(itemHeight));
            db.padding(buildSize(SIZE_GAP));
            db.margin(buildSize(SIZE_GAP));
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            db.onActiveEffect(eb);
            db.build(nifty, current, e).getNiftyControl(Droppable.class).addFilter(this);
        } else {
            pb = new PanelBuilder(SUB_PANEL_ID.concat(SPLITTER).concat("2"));
            pb.width(buildSize(itemWidth));
            pb.height(buildSize(itemHeight));
            pb.padding(buildSize(SIZE_GAP));
            pb.margin(buildSize(SIZE_GAP));
            pb.build(nifty, current, e);
        }
    }
}
