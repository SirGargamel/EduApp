/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eduapp.gui.listbox;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 *
 * @author Petr Ječmen
 */
public class LineListBoxViewConverter implements ListBoxViewConverter<Line> {
    private static final String LINE_TEXT = "#line-text";

    /**
     * Default constructor.
     */
    public LineListBoxViewConverter() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void display(final Element listBoxItem, final Line item) {
        final Element text = listBoxItem.findElementByName(LINE_TEXT);
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        if (item != null) {
            textRenderer.setText(item.getLine());
        } else {
            textRenderer.setText("");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getWidth(final Element listBoxItem, final Line item) {
        final Element text = listBoxItem.findElementByName(LINE_TEXT);
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        return ((textRenderer.getFont() == null) ? 0 : textRenderer.getFont().getWidth(item.getLine()));
    }
    
}
