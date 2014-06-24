package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.level.ItemParameters;
import eduapp.ItemRegistry;
import eduapp.gui.listbox.Line;
import eduapp.level.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiDictionary implements ScreenController {

    private static final String TAG_DICTIONARY = "dic";
    private static final String SIZE_LINE_HEIGHT = "20";
    private static final String[] DISPLAY = new String[]{ItemParameters.FORMULA, ItemParameters.STATE, ItemParameters.LINKS};    
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
//        buildItemTree();
    }

    private void buildItemTree() {       
        displayedItems.clear();
        tree.clear();

        String val;
        for (Item it : ir.listItems()) {
            val = it.getParam(ItemParameters.SOURCE);
            if (TAG_DICTIONARY.equals(val)) {
                displayedItems.add(it);
            }
        }
        Collections.sort(displayedItems);
                        
        for (Item it : displayedItems) {  
           tree.addItem(new Line(it.getParam(ItemParameters.NAME)));
        }

        tree.getElement().layoutElements();
    }

    private void buildDataPanel() {
        for (Element e : panelContent.getElements()) {
            e.markForRemoval();
        }

        final Item descr = ir.get(ItemParameters.NAMES_CONVERT);
        if (selectedItem != null) {
            final Screen current = nifty.getCurrentScreen();
            PanelBuilder pb;
            TextBuilder tb;
            String val;
            Element p;

            for (String s : DISPLAY) {
                val = selectedItem.getParam(s);
                if (val != null) {
                    pb = new PanelBuilder("p".concat(s));
                    pb.childLayoutVertical();
                    p = pb.build(nifty, current, panelContent);

                    tb = new TextBuilder("t1".concat(s));
                    tb.style("baseB");
                    tb.text(descr.getParam(s));
                    tb.color(Color.WHITE);
                    tb.height(SIZE_LINE_HEIGHT);
                    p.add(tb.build(nifty, current, panelContent));

                    tb = new TextBuilder("t2".concat(s));
                    tb.style("base");
                    tb.text(val);
                    tb.color(Color.BLACK);
                    tb.height(SIZE_LINE_HEIGHT);
                    p.add(tb.build(nifty, current, panelContent));

                    panelContent.add(p);
                }
            }
        }

        panelContent.layoutElements();
    }

    @Override
    public void onEndScreen() {
    }

    @NiftyEventSubscriber(id = "tree")
    public void onMyListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        for (int i : event.getSelectionIndices()) {
            selectedItem = displayedItems.get(i);
        }
        buildDataPanel();
    }
    
    public void listBoxItemClicked() {
    }
}
