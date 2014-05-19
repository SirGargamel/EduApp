package eduapp.state;

import eduapp.AppContext;

/**
 *
 * @author Petr Ječmen
 */
public class StateManager {

    private static StateManager instance;
    private final GameScreen gameScreen;
    private final StartScreen startScreen;
    private String levelName;

    static {
        instance = new StateManager();
    }

    public static StateManager getInstance() {
        return instance;
    }

    private StateManager() {
        gameScreen = new GameScreen();
        startScreen = new StartScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;

        AppContext.getApp().getStateManager().detach(startScreen);
        gameScreen.setLevelName(levelName);
        AppContext.getApp().getStateManager().attach(gameScreen);
    }
}
