package eduapp.state;

import eduapp.AppContext;

/**
 *
 * @author Petr Ječmen
 */
public class StateManager {

    private static final GameScreen gameScreen;
    private static final StartScreen startScreen;

    static {
        gameScreen = new GameScreen();
        startScreen = new StartScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
    }

    public static void loadLevel(String levelName) {
        AppContext.getApp().getStateManager().detach(startScreen);        
        gameScreen.setLevelName(levelName);
        gameScreen.setEnabled(true);
        AppContext.getApp().getStateManager().attach(gameScreen);         
    }

    public static void enableGame(boolean isEnabled) {
        gameScreen.setEnabled(isEnabled);
    }

    public static void displayMainMenu() {        
        AppContext.getApp().getStateManager().detach(gameScreen);        
        AppContext.getApp().getStateManager().attach(startScreen);
    }
}
