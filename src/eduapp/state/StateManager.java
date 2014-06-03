package eduapp.state;

import eduapp.AppContext;
import eduapp.PlayerAvatar;

/**
 *
 * @author Petr Ječmen
 */
public class StateManager {

    private static final WorldScreen worldScreen;
    private static final StartScreen startScreen;
    private static PlayerAvatar player;

    static {
        worldScreen = new WorldScreen();
        startScreen = new StartScreen();

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
}
