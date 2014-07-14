package eduapp;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;

/**
 *
 * @author Petr Ječmen
 */
public class JmolUtils {

    private static final String JMOL_PATH = "data\\jmol\\";
    private static final String ICON_PATH = "data\\icon.png";
    private static JmolPanel jmolPanel;
    private static JFrame frame;

    public static void displayModel(final String modelName) {
        if (frame == null) {
            initializeJmolPanel();
        }
        
        frame.setVisible(true);
        jmolPanel.getViewer().openFile(JMOL_PATH.concat(modelName));
    }

    public static void closeViewer() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    private static void initializeJmolPanel() {
        frame = new JFrame();
        try {
            frame.setIconImage(ImageIO.read(new File(ICON_PATH)));
        } catch (IOException ex) {
            System.err.println("Error setting icon to JMol panel.");
            System.err.println(ex);
        }
        Container contentPane = frame.getContentPane();
        jmolPanel = new JmolPanel();

        jmolPanel.setPreferredSize(new Dimension(200, 200));
        contentPane.add(jmolPanel);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    private static class JmolPanel extends JPanel {

        JmolViewer viewer;
        JmolAdapter adapter;

        JmolPanel() {
            adapter = new SmarterJmolAdapter();
            viewer = JmolViewer.allocateViewer(this, adapter);

        }

        public JmolViewer getViewer() {
            return viewer;
        }

        public void executeCmd(String rasmolScript) {
            viewer.evalString(rasmolScript);
        }
        final Dimension currentSize = new Dimension();
        final Rectangle rectClip = new Rectangle();

        @Override
        public void paint(Graphics g) {
            if (isVisible()) {
                getSize(currentSize);
                g.getClipBounds(rectClip);
                viewer.renderScreenImage(g, currentSize, rectClip);
            }
        }
    }
}
