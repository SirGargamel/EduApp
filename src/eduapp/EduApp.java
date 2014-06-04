package eduapp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

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
