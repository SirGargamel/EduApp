package eduapp.gui;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Hint;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.AppContext;
import eduapp.level.item.ItemParameters;
import eduapp.ItemRegistry;
import eduapp.level.Player;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Petr Ječmen
 */
public class GuiWorld implements ScreenController {

    private static final String NAME_PANEL_TEXT = "panelText";
    private static final String NAME_PANEL_IMAGE = "panelDescr";
    private static final String NAME_TEXT = "text";
    private static final String NAME_IMAGE = "guide";
    private static final String NAME_DESCR = "textDescr";
    private Nifty nifty;
    private List<Element> inv;
    private Player player;
    private String notification, descr;
    private Element questionText;
    private Element panelQuest;
    private Element image;
    private Element description;
    private Element panelImage;

    public void displayDescription(String message) {
        if (message == null || message.isEmpty()) {
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

        inv = new ArrayList<>(5);
        inv.add(nifty.getCurrentScreen().findElementByName("inv1"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv2"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv3"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv4"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv5"));

        panelQuest = nifty.getCurrentScreen().findElementByName(NAME_PANEL_TEXT);
        questionText = nifty.getCurrentScreen().findElementByName(NAME_TEXT);
        image = nifty.getCurrentScreen().findElementByName(NAME_IMAGE);
        description = nifty.getCurrentScreen().findElementByName(NAME_DESCR);
        panelImage = nifty.getCurrentScreen().findElementByName(NAME_PANEL_IMAGE);
    }

    @Override
    public void onStartScreen() {
        for (Element e : inv) {
            e.hide();
        }

        refreshInventoryItems();

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

    public void refreshInventoryItems() {
        if (player != null) {
            final List<String> items = player.getAllItems();
            final AssetManager assetManager = AppContext.getApp().getAssetManager();
            final ItemRegistry itemRegistry = AppContext.getItemRegistry();

            Element e;
            ImageRenderer ir;
            String key;
            for (int i = 0; i < Math.min(items.size(), inv.size()); i++) {
                e = inv.get(i);
                key = "data/icons/" + itemRegistry.get(items.get(i)).getParam(ItemParameters.ICON);

                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(key));
                } catch (IOException ex) {
                    System.err.println(ex);
                }

                final AWTLoader loader = new AWTLoader();
                final Texture tex = new Texture2D(loader.load(img, true));
                ((DesktopAssetManager) assetManager).addToCache(new TextureKey(key), tex);

                ir = e.getRenderer(ImageRenderer.class);
                ir.setImage(nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), key, true));
                List<Effect> list = e.getEffects(EffectEventId.onHover, Hint.class);
                list.get(0).getParameters().setProperty("hintText", itemRegistry.get(items.get(i)).getParam(ItemParameters.NAME));

//            final HoverEffectBuilder eb = new HoverEffectBuilder("hint");
//            eb.getAttributes().setAttribute("hintDelay", "100");
//            eb.getAttributes().setAttribute("hintText", di.getText());            
//            pb.onHoverEffect(eb);

                e.show();
            }
        }
    }
}
