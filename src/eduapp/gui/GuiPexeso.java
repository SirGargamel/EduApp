/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.AppContext;
import eduapp.Utils;
import eduapp.level.item.ItemParameter;
import eduapp.level.quest.QuestPexeso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class GuiPexeso implements ScreenController {

    private static final String ID_PANEL = "panelPexeso";
    private static final String SIZE_UNIT = "%";
    private static final int SIZE_GAP = 1;
    private Nifty nifty;
    private Element panelPexeso;
    private QuestPexeso pq;
    private Element selectedElement;
    private int correct;
    private Hider h;

    public void setData(QuestPexeso pq) {
        this.pq = pq;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        panelPexeso = nifty.getCurrentScreen().findElementByName(ID_PANEL);
    }

    @Override
    public void onStartScreen() {
        for (Element e : panelPexeso.getElements()) {
            e.markForRemoval();
        }

        final List<Card> cards = new ArrayList<>();
        for (String s : pq.getItemNames()) {
            cards.add(new Card(s, true));
            cards.add(new Card(s, false));
        }
        Collections.shuffle(cards);
        correct = 0;

        final int itemsPerLine = (int) Math.ceil(Math.sqrt(cards.size()));
        final int lineCount = (int) Math.ceil(cards.size() / (double) itemsPerLine);
        final int panelHeight = (100 - lineCount * SIZE_GAP) / lineCount;
        final int itemWidth = (100 - lineCount * SIZE_GAP) / itemsPerLine;

        final Screen current = nifty.getCurrentScreen();
        int counter = itemsPerLine;
        int line = 0;
        PanelBuilder pb;
        Element p = null;
        ImageBuilder ib;
        EffectBuilder eb;
        TextBuilder tb;
        for (Card c : cards) {
            if (counter >= itemsPerLine) {
                pb = new PanelBuilder("gap".concat(Integer.toString(line)));
                pb.height(buildSize(SIZE_GAP));
                pb.build(nifty, current, panelPexeso);

                pb = new PanelBuilder("panel".concat(Integer.toString(line)));
                pb.height(buildSize(panelHeight));
                pb.childLayoutHorizontal();
                p = pb.build(nifty, current, panelPexeso);

                counter = 0;
                line++;
            }
            counter++;

            pb = new PanelBuilder("gap".concat(c.id).concat(Boolean.toString(c.isText())));
            pb.width(buildSize(SIZE_GAP));
            pb.build(nifty, current, p);

            if (c.isText()) {
                pb = new PanelBuilder("text".concat(c.id));
            } else {
                pb = new PanelBuilder("image".concat(c.id));
            }
            pb.width(buildSize(itemWidth));
            pb.backgroundColor(Color.WHITE);
            pb.childLayoutCenter();
            pb.visibleToMouse();

            if (c.isText()) {
                pb.interactOnClick("text(" + c.id + ")");
                tb = new TextBuilder("t".concat(c.id));
                tb.style("base");
                tb.width("100%");
                tb.height("100%");
                tb.wrap(true);
                tb.color(Color.BLACK);
                tb.text(AppContext.getItemRegistry().get(c.id).getParam(ItemParameter.DESCRIPTION));
                pb.text(tb);
            } else {
                pb.interactOnClick("image(" + c.id + ")");
            }

            ib = new ImageBuilder("i".concat(c.id).concat(Boolean.toString(c.isText())));
            ib.filename("interface/pexeso.png");
            ib.width("100%");
            ib.height("100%");
            pb.image(ib);

            eb = new EffectBuilder("border");
            eb.effectParameter("border", "2px");
            eb.effectParameter("length", "infinite");
            pb.onClickEffect(eb);

            pb.build(nifty, current, p);
        }
    }

    public void text(String val) {
        final Element e = nifty.getCurrentScreen().findElementByName("i".concat(val).concat("true"));
        e.getRenderer(ImageRenderer.class).setImage(null);
        e.hide();

        handleElementAction(e);
    }

    public void image(String val) {
        final Element e = nifty.getCurrentScreen().findElementByName("i".concat(val).concat("false"));
        final NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), Utils.generateIconFilename(val), false);
        e.getRenderer(ImageRenderer.class).setImage(img);

        handleElementAction(e);
    }

    private void handleElementAction(final Element e) {
        if (h != null) {
            h.hide();
        }

        if (selectedElement == null) {
            selectedElement = e;
        } else {
            final Element e2 = selectedElement;
            selectedElement = null;

            final String name1 = digName(e.getId());
            final String name2 = digName(e2.getId());
            if (name1.equals(name2)) {
                e.getElementInteraction().getPrimary().setOnClickMethod(null);
                e2.getElementInteraction().getPrimary().setOnClickMethod(null);
                correct++;
                if (correct >= pq.getItemNames().size()) {
                    pq.setFinished(true);
                    pq.finish();
                }
            } else {
                h = new Hider(e, e2, nifty);
                h.start();
            }
        }
    }

    private static String digName(final String id) {
        return id.substring(1).replace("false", "").replace("true", "");
    }

    @Override
    public void onEndScreen() {
    }

    private String buildSize(final int value) {
        return Integer.toString(value).concat(SIZE_UNIT);
    }

    private static class Card {

        private final String id;
        private final boolean text;

        public Card(String id, boolean text) {
            this.id = id;
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public boolean isText() {
            return text;
        }
    }

    private static class Hider extends Thread {

        private final Element e1, e2;
        private final Nifty nifty;

        public Hider(Element e1, Element e2, Nifty nifty) {
            this.e1 = e1;
            this.e2 = e2;
            this.nifty = nifty;
        }

        @Override
        public void run() {
            synchronized (this) {
                try {
                    this.wait(2000);
                } catch (InterruptedException ex) {
                }
            }

            final NiftyImage img = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), "interface/pexeso.png", false);
            e1.getRenderer(ImageRenderer.class).setImage(img);
            e1.show();
            e2.getRenderer(ImageRenderer.class).setImage(img);
            e2.show();
        }

        synchronized void hide() {
            notify();
        }
    }
}
