package eduapp;

import com.jme3.app.SimpleApplication;
import eduapp.level.Item;

/**
 *
 * @author Petr Ječmen
 */
public class AppContext {

    private static final ItemRegistry itemRegistry;
    private static SimpleApplication app;

    static {
        itemRegistry = ItemRegistry.getInstance();
    }

    public static SimpleApplication getApp() {
        return app;
    }

    public static void setApp(SimpleApplication app) {
        AppContext.app = app;
    }

    public static ItemRegistry getItemRegistry() {
        return itemRegistry;
    }
}
