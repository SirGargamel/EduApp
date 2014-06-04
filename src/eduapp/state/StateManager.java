package eduapp.state;

import eduapp.AppContext;
import eduapp.ItemRegistry;
import eduapp.PlayerAvatar;

/**
 *
 * @author Petr Ječmen
 */
public class StateManager {

    private static final WorldScreen worldScreen;
    private static final StartScreen startScreen;
    private static final GroupScreen groupScreen;
    private static PlayerAvatar player;

    static {
        worldScreen = new WorldScreen();
        startScreen = new StartScreen();
        groupScreen = new GroupScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
    }

    public static void assignPlayerAvatar(final PlayerAvatar player) {
        StateManager.player = player;
    }

    public static void loadLevel(String levelName) {
        AppContext.getApp().getStateManager().detach(startScreen);
        worldScreen.setLevelName(levelName);
        worldScreen.setEnabled(true);
        AppContext.getApp().getStateManager().attach(worldScreen);
    }

    public static void enableGame(boolean isEnabled) {
        worldScreen.setEnabled(isEnabled);
        if (player != null) {
            player.setIsRunning(isEnabled);
        }
    }

    public static void displayMainMenu() {
        AppContext.getApp().getStateManager().detach(worldScreen);
        AppContext.getApp().getStateManager().attach(startScreen);
    }

    public static void exitGame() {
        AppContext.getApp().stop();
    }

    public static void debug() {
        AppContext.getApp().getStateManager().detach(startScreen);
        AppContext.getApp().getStateManager().detach(worldScreen);

        final ItemRegistry ir = AppContext.getItemRegistry();
        groupScreen.setGrouping(ir.get("stav"));
        groupScreen.setItems(ir.get("h2so4"), ir.get("v2o5"), ir.get("s"), ir.get("sio2"), ir.get("so2"));

        AppContext.getApp().getStateManager().attach(groupScreen);
    }
}
