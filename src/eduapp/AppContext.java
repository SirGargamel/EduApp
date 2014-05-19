package eduapp;

import com.jme3.app.SimpleApplication;

/**
 *
 * @author Petr Ječmen
 */
public class AppContext {

    private static SimpleApplication app;

    public static SimpleApplication getApp() {
        return app;
    }

    public static void setApp(SimpleApplication app) {
        AppContext.app = app;
    }
}
