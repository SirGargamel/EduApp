package eduapp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Petr Jecmen
 */
public class EduApp {

    public static void main(String[] args) {
        try {
            SimpleApplication app = new Game();
            AppSettings settings = new AppSettings(true);
            settings.setRenderer(AppSettings.LWJGL_OPENGL_ANY);
            settings.setWidth(1024);
            settings.setHeight(768);
            settings.setTitle("EduApp");
            settings.setIcons(new BufferedImage[] {ImageIO.read(new File("data/icon.png"))});
            
            app.setShowSettings(false);
            app.setSettings(settings);

            app.setDisplayStatView(false);
            app.setDisplayFps(false);

            app.start();            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
