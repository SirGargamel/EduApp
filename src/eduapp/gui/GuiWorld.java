package eduapp.gui;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.AppContext;
import eduapp.level.ItemParameters;
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

    private static final String NAME_PANEL = "panelText";
    private static final String NAME_TEXT = "text";
    private static final String NAME_IMAGE = "albert";
    private Nifty nifty;
    private Element icon;
    private List<Element> inv;
    private Player player;
    private String message;
    private Element questionText;
    private Element panelQuest;
    private Element image;

    public void setMessage(String message) {
        this.message = message;
    }

    public void displayMessage() {
        if (message != null) {
            questionText.show();
            panelQuest.show();
            image.show();

            questionText.getRenderer(TextRenderer.class).setText(message);
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

        icon = nifty.getCurrentScreen().findElementByName("panelImage");

        inv = new ArrayList<>(5);
        inv.add(nifty.getCurrentScreen().findElementByName("inv1"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv2"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv3"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv4"));
        inv.add(nifty.getCurrentScreen().findElementByName("inv5"));

        panelQuest = nifty.getCurrentScreen().findElementByName(NAME_PANEL);
        questionText = nifty.getCurrentScreen().findElementByName(NAME_TEXT);
        image = nifty.getCurrentScreen().findElementByName(NAME_IMAGE);
    }

    @Override
    public void onStartScreen() {
        icon.hide();

        for (Element e : inv) {
            e.hide();
        }

        refreshInventoryItems();

        if (message != null) {
            displayMessage();
        }
    }

    @Override
    public void onEndScreen() {
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void enableQuestMarker(boolean enable) {
        if (icon != null) {
            if (enable) {
                icon.show();
            } else {
                icon.hide();
            }
        }
    }

    public void refreshInventoryItems() {
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
            e.show();
        }
    }
}
