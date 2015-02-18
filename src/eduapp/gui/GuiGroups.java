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
import eduapp.AppContext;
import eduapp.ItemRegistry;
import eduapp.level.item.Item;
import eduapp.level.item.ItemParameter;
import eduapp.level.quest.QuestGrouping;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiGroups implements ScreenController, DroppableDropFilter {

    private static final String SIZE_UNIT = "px";
    private static final int SIZE_GAP = 5;
    private static final int COUNT_ITEMS_PER_LINE = 4;
    private static final int SIZE_ITEM_HEIGHT = 32;
    private QuestGrouping quest;
    private Element groups, items, panelData, text;
    private Nifty nifty;

    public void setQuest(QuestGrouping gq) {
        this.quest = gq;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        text = nifty.getCurrentScreen().findElementByName("panelText");
        groups = nifty.getCurrentScreen().findElementByName("panelGroups");
        items = nifty.getCurrentScreen().findElementByName("panelItems");
        panelData = nifty.getCurrentScreen().findElementByName("panelData");
    }

    @Override
    public void onStartScreen() {
        for (Element e : groups.getElements()) {
            e.markForRemoval();
        }
        for (Element e : items.getElements()) {
            e.markForRemoval();
        }
        nifty.executeEndOfFrameElementActions();

        final Screen currentScreen = nifty.getCurrentScreen();

        String[] groupNames;
        if (quest.getGroup() == null) {
            final ItemRegistry ir = AppContext.getItemRegistry();
            final String groupName = ir.get("description").getParam(quest.getGroupId()).toLowerCase();
            groupNames = new String[]{
                "Je " + groupName,
                "Není " + groupName};
        } else {
            groupNames = quest.getGroup().getParam(quest.getGroupId()).split(";");
        }
        final int itemCount = quest.getItems().length;
        final int groupCount = groupNames.length;
        final int groupWidth = (groups.getWidth() - (groupCount + 1) * SIZE_GAP) / groupCount;
        final int itemWidth = (items.getWidth() - (COUNT_ITEMS_PER_LINE + 1) * SIZE_GAP) / COUNT_ITEMS_PER_LINE;
        final int itemLineCount = (int) Math.ceil(itemCount / (double) COUNT_ITEMS_PER_LINE);
        final int itemPanelHeight = (items.getHeight() - (itemLineCount + 1) * SIZE_GAP) / itemLineCount;

        TextBuilder tb = new TextBuilder();
        tb.style("baseB");
        tb.text(quest.toNiftyString());
        tb.textHAlignCenter();
        tb.alignCenter();
        tb.color(Color.WHITE);
        tb.build(nifty, currentScreen, text);

        DroppableBuilder db;
        PanelBuilder pb;
        EffectBuilder eb;
        Element p = null, e;
        pb = new PanelBuilder("gap");
        pb.width(buildSize(SIZE_GAP));
        pb.build(nifty, currentScreen, groups);
        for (String s : groupNames) {
            pb = new PanelBuilder(s);
            pb.childLayoutVertical();
            pb.alignCenter();
            pb.width(buildSize(groupWidth));
            tb = new TextBuilder("text" + s);
            tb.text(s);
            tb.style("base");
            tb.color(Color.BLACK);
            tb.alignCenter();
            pb.text(tb);
            p = pb.build(nifty, currentScreen, groups);

            pb = new PanelBuilder("pG1".concat(s));
            pb.height("1%");
            pb.build(nifty, currentScreen, p);

            for (int i = 0; i < itemCount; i++) {
                db = new DroppableBuilder(s + "-" + Integer.toString(i));
                eb = new EffectBuilder("border");
                eb.effectParameter("color", "#ffffff");
                eb.effectParameter("border", "1px");
                db.onActiveEffect(eb);

                db.childLayoutCenter();
                db.alignCenter();
                db.width(buildSize(itemWidth));
                db.height(buildSize(SIZE_ITEM_HEIGHT));
                db.build(nifty, currentScreen, p).getNiftyControl(Droppable.class).addFilter(this);

                pb = new PanelBuilder("gap" + s + "-" + Integer.toString(i));
                pb.height("2px");
                pb.build(nifty, currentScreen, p);
            }
            pb = new PanelBuilder("gap" + s);
            pb.width(buildSize(SIZE_GAP));
            pb.build(nifty, currentScreen, groups);
        }

        int counter = COUNT_ITEMS_PER_LINE;
        DraggableBuilder dgb;
        String id;
        pb = new PanelBuilder("gap2");
        pb.width(buildSize(SIZE_GAP));
        pb.build(nifty, currentScreen, items);
        for (Item i : quest.getItems()) {
            id = i.getId();
            if (counter >= COUNT_ITEMS_PER_LINE) {
                pb = new PanelBuilder("itemPanel" + id);
                pb.childLayoutHorizontal();
                pb.height(buildSize(itemPanelHeight));
                p = pb.build(nifty, currentScreen, items);
                counter = 0;
            }
            counter++;

            db = new DroppableBuilder("drop" + id);
            db.width(buildSize(itemWidth));
            db.height(buildSize(SIZE_ITEM_HEIGHT));
            db.childLayoutCenter();
            e = db.build(nifty, currentScreen, p);
            e.getNiftyControl(Droppable.class).addFilter(this);

            dgb = new DraggableBuilder(id);
            dgb.width(buildSize(itemWidth));
            dgb.height(buildSize(SIZE_ITEM_HEIGHT));
            eb = new EffectBuilder("border");
            eb.effectParameter("color", "#ffffff");
            eb.effectParameter("border", "1px");
            dgb.onActiveEffect(eb);
            tb = new TextBuilder("text" + id);
            tb.text(i.getParam(ItemParameter.NAME));
            tb.style("base");
            tb.color(Color.BLACK);
            dgb.text(tb);
            dgb.childLayoutCenter();
            dgb.build(nifty, currentScreen, e);

            pb = new PanelBuilder("gap" + id);
            pb.width(buildSize(SIZE_GAP));
            pb.build(nifty, currentScreen, items);
        }
    }

    @Override
    public void onEndScreen() {
    }

    public void ok() {
        final ItemRegistry ir = AppContext.getItemRegistry();
        final List<Element> elements = groups.getElements();
        final String paramId = quest.getGroupId();
        int correctCount = 0;
        String itemId, groupId, val;
        Item item;
        Element element;
        if (quest.getGroup() != null) {
            for (Element e : elements) {
                groupId = e.getId();
                for (Element el : e.getElements()) {
                    element = extractElement(el);
                    if (element != null) {
                        itemId = element.getId();
                        item = ir.get(itemId);
                        val = item.getParam(paramId);
                        if (val.contains(groupId)) {
                            correctCount++;
                            element.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#00ff00"));
                        } else {
                            element.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
                        }
                    }
                }
            }
        } else {
            final Element has = elements.get(1);
            final Element hasNot = elements.get(3);
            for (Element e : has.getElements()) {
                element = extractElement(e);
                if (element != null) {
                    itemId = element.getId();
                    item = ir.get(itemId);
                    val = item.getParam(paramId);
                    if (val != null) {
                        correctCount++;
                        element.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#00ff00"));
                    } else {
                        element.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
                    }
                }
            }
            for (Element e : hasNot.getElements()) {
                element = extractElement(e);
                if (element != null) {
                    itemId = element.getId();
                    item = ir.get(itemId);
                    val = item.getParam(paramId);
                    if (val == null) {
                        correctCount++;
                        element.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#00ff00"));
                    } else {
                        element.getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
                    }
                }
            }
        }
        for (Element e : items.getElements()) {
            for (Element el : e.getElements()) {
                if (el.getElements().size() > 1) {
                    el.getElements().get(0).getRenderer(PanelRenderer.class).setBackgroundColor(new Color("#ff0000"));
                }
            }
        }

        final int fCorrectCount = correctCount;
        panelData.startEffect(EffectEventId.onCustom, () -> {
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

    private String buildSize(final int size) {
        return Integer.toString(size).concat(SIZE_UNIT);
    }

    @Override
    public boolean accept(Droppable dropSource, Draggable draggedItem, Droppable droppedAt) {
        return droppedAt.getElement().getElements().get(0).getElements().isEmpty();
    }
}
