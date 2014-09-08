package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.AppContext;
import eduapp.level.item.ItemParameter;
import eduapp.ItemRegistry;
import eduapp.Utils;
import eduapp.level.Player;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiWorld implements ScreenController {

    private static final String NAME_PANEL_TEXT = "panelText";
    private static final String NAME_PANEL_IMAGE = "panelDescr";
    private static final String NAME_PANEL_INV = "panelInv";
    private static final String NAME_TEXT = "text";
    private static final String NAME_IMAGE = "guide";
    private static final String NAME_DESCR = "textDescr";
    private static final int SIZE_GAP = 1;
    private Nifty nifty;
    private Player player;
    private String notification, descr;
    private Element questionText;
    private Element panelQuest;
    private Element image;
    private Element description;
    private Element panelImage, panelInv;
    private int itemCount = 10;

    public void displayDescription(String message) {
        if (message == null || message.isEmpty()) {
            descr = null;
            if (panelImage != null && panelImage.isVisible()) {
                panelImage.hide();
            }
        } else if (!message.equals(descr)) {
            description.getRenderer(TextRenderer.class).setText(message);
            panelImage.show();
            descr = message;
        }
    }

    public void setNotification(String message) {
        this.notification = message;
    }

    public void displayNotification() {
        if (notification != null) {
            questionText.show();
            panelQuest.show();
            image.show();

            questionText.getRenderer(TextRenderer.class).setText(notification);
            panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                @Override
                public void perform() {
                    panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                        @Override
                        public void perform() {
                            questionText.hide();
                            panelQuest.hide();
                            image.hide();
                        }
                    }, "Hide");
                }
            }, "Show");
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;

        panelQuest = nifty.getCurrentScreen().findElementByName(NAME_PANEL_TEXT);
        questionText = nifty.getCurrentScreen().findElementByName(NAME_TEXT);
        image = nifty.getCurrentScreen().findElementByName(NAME_IMAGE);
        description = nifty.getCurrentScreen().findElementByName(NAME_DESCR);
        panelImage = nifty.getCurrentScreen().findElementByName(NAME_PANEL_IMAGE);
        panelInv = nifty.getCurrentScreen().findElementByName(NAME_PANEL_INV);
    }

    @Override
    public void onStartScreen() {
        if (notification != null) {
            displayNotification();
        }
    }

    @Override
    public void onEndScreen() {
    }

    public void setPlayer(Player player) {
        this.player = player;
        refreshInventoryItems();
    }

    public void setItemCout(final int itemCount) {
        this.itemCount = itemCount;
        refreshInventoryItems();
    }

    public void refreshInventoryItems() {
        if (player != null && panelInv != null) {
            final List<String> items = player.getAllItems();
            final ItemRegistry itemRegistry = AppContext.getItemRegistry();

            for (Element e : panelInv.getElements()) {
                e.markForRemoval();
            }
            nifty.executeEndOfFrameElementActions();

            final int sizeFullItem = 100 / itemCount;
            final String sizeItem = Integer.toString(sizeFullItem - SIZE_GAP);

            PanelBuilder pb;
            for (String s : items) {
                pb = new PanelBuilder("gap" + s);
                pb.width(Integer.toString(SIZE_GAP) + "%");
                pb.build(nifty, nifty.getCurrentScreen(), panelInv);

                pb = new PanelBuilder("panel" + s);
                pb.width(sizeItem + "%");
                pb.childLayoutCenter();
                pb.backgroundColor("#8b9dc388");

                final HoverEffectBuilder eb = new HoverEffectBuilder("hint");
                eb.getAttributes().setAttribute("hintDelay", "100");
                eb.getAttributes().setAttribute("hintText", itemRegistry.get(s).getParam(ItemParameter.NAME));
                pb.onHoverEffect(eb);

                final ImageBuilder ib = new ImageBuilder("image" + s);
                ib.filename(Utils.generateIconFilename(s));
                ib.height("100%");
                ib.width("100%");
                pb.image(ib);

                pb.build(nifty, nifty.getCurrentScreen(), panelInv);
            }
        }
    }
}
