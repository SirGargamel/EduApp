package eduapp.state;

import eduapp.AppContext;
import eduapp.PlayerAvatar;

/**
 *
 * @author Petr Ječmen
 */
public class StateManager {

    private static final GameScreen gameScreen;
    private static final StartScreen startScreen;
    private static PlayerAvatar player;

    static {
        gameScreen = new GameScreen();
        startScreen = new StartScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
    }

    public static void assignPlayerAvatar(final PlayerAvatar player) {
        StateManager.player = player;
    }

    public static void loadLevel(String levelName) {
        AppContext.getApp().getStateManager().detach(startScreen);
        gameScreen.setLevelName(levelName);
        gameScreen.setEnabled(true);
        AppContext.getApp().getStateManager().attach(gameScreen);
    }

    public static void enableGame(boolean isEnabled) {        
        gameScreen.setEnabled(isEnabled);
        if (player != null) {
            player.setIsRunning(isEnabled);
        }
    }

    public static void displayMainMenu() {
        AppContext.getApp().getStateManager().detach(gameScreen);
        AppContext.getApp().getStateManager().attach(startScreen);        
    }
    
    public static void exitGame() {
        AppContext.getApp().stop();
    }
}
