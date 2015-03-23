package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.level.item.ItemParameter;
import eduapp.ItemRegistry;
import eduapp.JmolUtils;
import eduapp.gui.listbox.Line;
import eduapp.level.item.Item;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiDictionary implements ScreenController {

    private static final String TAG_DICTIONARY = "dic";
    private static final String TAG_WORDBOOK = "word";
    private static final String TAG_ICONS = "ikony";
    private static final String SIZE_LINE_HEIGHT = "20";
    private static final String[] DISPLAY_ELEMENT = new String[]{ItemParameter.FORMULA, ItemParameter.STATE};
    private static final String[] ICONS = new String[]{ItemParameter.TOXIC, ItemParameter.FLAMABLE, ItemParameter.CHARGE, ItemParameter.SOLUBLE};
    private Nifty nifty;
    private ItemRegistry ir;
    private Element panelContent;
    private ListBox tree;
    private List<Item> displayedItems;
    private Item selectedItem;

    public GuiDictionary() {
        displayedItems = new ArrayList<>();
    }

    public void setItemRegistry(ItemRegistry ir) {
        this.ir = ir;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelContent = nifty.getCurrentScreen().findElementByName("content");
        tree = nifty.getCurrentScreen().findNiftyControl("tree", ListBox.class);
        buildItemTree();
    }

    @Override
    public void onStartScreen() {
    }

    private void buildItemTree() {
        displayedItems.clear();
        tree.clear();

        String val;
        for (Item it : ir.listItems()) {
            val = it.getParam(ItemParameter.SOURCE);
            if ((TAG_DICTIONARY.equals(val) || TAG_WORDBOOK.equals(val)) && it.getParam(ItemParameter.NAME) != null) {
                displayedItems.add(it);
            }
        }
        Collections.sort(displayedItems);

        for (Item it : displayedItems) {
            tree.addItem(new Line(it.getParam(ItemParameter.NAME)));
        }

        tree.getElement().layoutElements();
    }

    private void buildDataPanel() {
        for (Element e : panelContent.getElements()) {
            e.markForRemoval();
        }
        nifty.executeEndOfFrameElementActions();

        if (selectedItem != null) {
            if (TAG_DICTIONARY.equals(selectedItem.getParam(ItemParameter.SOURCE))) {
                buildElementData();
            } else {
                buildWordbookData();
            }
        }

        panelContent.layoutElements();
    }

    private void buildElementData() {
        final Item descr = ir.get(ItemParameter.NAMES_CONVERT);
        final Screen current = nifty.getCurrentScreen();
        PanelBuilder pb;
        TextBuilder tb;
        String val;
        Element p;
        String[] split;
        ImageBuilder ib;
        // icons
        pb = new PanelBuilder("gapTop");
        pb.childLayoutVertical();
        pb.height("2%");
        pb.build(nifty, current, panelContent);

        pb = new PanelBuilder("p".concat("Icons"));
        pb.childLayoutHorizontal();
        pb.height("10%");
        p = pb.build(nifty, current, panelContent);
        for (String s : ICONS) {
            val = selectedItem.getParam(s);
            if (val != null) {
                ib = new ImageBuilder("i".concat(s));
                ib.filename("icons/" + ir.get(TAG_ICONS).getParam(s));

                pb = new PanelBuilder("p".concat(s));
                pb.childLayoutCenter();
                pb.width("15%");
                pb.image(ib);
                pb.build(nifty, current, p);
            }
        }
        // other parameters
        for (String s : DISPLAY_ELEMENT) {
            val = selectedItem.getParam(s);
            if (val != null) {
                pb = new PanelBuilder("gap".concat(s));
                pb.childLayoutVertical();
                pb.height("2%");
                pb.build(nifty, current, panelContent);

                pb = new PanelBuilder("p".concat(s));
                pb.childLayoutVertical();
                p = pb.build(nifty, current, panelContent);

                tb = new TextBuilder("t1".concat(s));
                tb.style("baseB");
                tb.text(descr.getParam(s));
                tb.color(Color.WHITE);
                tb.height(SIZE_LINE_HEIGHT);
                tb.build(nifty, current, p);

                pb = new PanelBuilder("gap2".concat(s));
                pb.childLayoutVertical();
                pb.height("2%");
                pb.build(nifty, current, p);

                tb = new TextBuilder("t2".concat(s));
                tb.style("base");
                tb.text(val);
                tb.color(Color.BLACK);
                tb.height(SIZE_LINE_HEIGHT);
                tb.build(nifty, current, p);
            }
        }
        // hyperlinks
        val = selectedItem.getParam(ItemParameter.LINKS);
        if (val != null) {
            pb = new PanelBuilder("p".concat(ItemParameter.LINKS));
            pb.childLayoutVertical();
            p = pb.build(nifty, current, panelContent);

            pb = new PanelBuilder("gap".concat(ItemParameter.LINKS));
            pb.childLayoutVertical();
            pb.height("5%");
            pb.build(nifty, current, p);

            tb = new TextBuilder("t1".concat(ItemParameter.LINKS));
            tb.style("baseB");
            tb.text(descr.getParam(ItemParameter.LINKS));
            tb.color(Color.WHITE);
            tb.height(SIZE_LINE_HEIGHT);
            tb.build(nifty, current, p);

            pb = new PanelBuilder("gap2".concat(ItemParameter.LINKS));
            pb.childLayoutVertical();
            pb.height("2%");
            pb.build(nifty, current, p);

            split = val.split(ItemParameter.SPLITTER);

            HoverEffectBuilder heb = new HoverEffectBuilder("textColor");
            heb.effectParameter("color", "#dfe3ee");
            for (String s2 : split) {
                tb = new TextBuilder("t2".concat(ItemParameter.LINKS).concat(s2));
                tb.style("base");
                tb.text(s2);
                tb.color("#0000ff");
                tb.height(SIZE_LINE_HEIGHT);
                tb.visibleToMouse(true);
                tb.interactOnClick("hyperlink(" + s2 + ")");
                tb.onHoverEffect(heb);
                tb.build(nifty, current, p);
            }
        }

        JmolUtils.displayModel(selectedItem.getId());
    }

    private void buildWordbookData() {
        final Screen current = nifty.getCurrentScreen();

        PanelBuilder pb;
        TextBuilder tb;
        Element p;
        final String val = selectedItem.getParam(ItemParameter.DESCRIPTION);
        if (val != null) {
            pb = new PanelBuilder("p".concat(ItemParameter.DESCRIPTION));
            pb.childLayoutCenter();
            pb.height("100%");
            p = pb.build(nifty, current, panelContent);

            tb = new TextBuilder("t".concat(ItemParameter.DESCRIPTION));
            tb.style("base");
            tb.width("100%");
            tb.textHAlignLeft();
            tb.wrap(true);
            tb.text(val);
            tb.color(Color.BLACK);
            tb.height(SIZE_LINE_HEIGHT);
            tb.build(nifty, current, p);
        }

        JmolUtils.displayModel(selectedItem.getId());
    }

    @Override
    public void onEndScreen() {
        JmolUtils.hideViewer();
    }

    @NiftyEventSubscriber(id = "tree")
    public void onMyListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        for (int i : event.getSelectionIndices()) {
            selectedItem = displayedItems.get(i);
        }
        buildDataPanel();
    }

    public void hyperlink(String link) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(link));
        } else {
            System.err.println("Link opening not supported.");
        }
    }
}
