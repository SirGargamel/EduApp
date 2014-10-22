package eduapp;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;

/**
 *
 * @author Petr Ječmen
 */
public class JmolUtils {

    private static final String PATH_JMOL = "data\\jmol\\";
    private static final String ICON_JMOL = "data\\icon.png";
    private static final Path PATH_LOCAL = new File(PATH_JMOL + "model").toPath();
    private static JmolPanel jmolPanel;
    private static JFrame frame;

    public static boolean displayModel(final String modelName) {
        final File model = new File(PATH_JMOL.concat(modelName));
        boolean result = false;
        if (model.exists()) {
            if (frame == null) {
                initializeJmolPanel();
            }
            try {
                java.nio.file.Files.copy(model.toPath(), PATH_LOCAL, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(JmolUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            jmolPanel.getViewer().openFile(PATH_LOCAL.toString());
            frame.setVisible(true);
            result = true;
        } else if (frame != null && frame.isVisible()) {
            frame.setVisible(false);
        }
        return result;
    }

    public static void closeViewer() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public static void hideViewer() {
        if (frame != null) {
            frame.setVisible(false);
        }
    }

    public static void initializeJmolPanel() {
        frame = new JFrame();
        try {
            frame.setIconImage(ImageIO.read(new File(ICON_JMOL)));
        } catch (IOException ex) {
            System.err.println("Error setting icon to JMol panel.");
            System.err.println(ex);
        }
        frame.setAutoRequestFocus(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        jmolPanel = new JmolPanel();
        jmolPanel.setPreferredSize(new Dimension(200, 200));

        final Container contentPane = frame.getContentPane();
        contentPane.add(jmolPanel);

        frame.pack();
        frame.setVisible(true);
        frame.setVisible(false);
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
