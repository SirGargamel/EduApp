package eduapp.gui;

import de.lessvoid.nifty.Nifty;

/**
 *
 * @author Petr Ječmen
 */
public class GuiManager {

    private static final String SCREEN_GAME = "game";
    private static final String SCREEN_MAIN = "start";
    private static final String SCREEN_PAUSE = "pause";
    private static Nifty nifty;

    public static void setNifty(final Nifty nifty) {
        GuiManager.nifty = nifty;
    }

    public static void gotoGameScreen() {
        nifty.gotoScreen(SCREEN_GAME);
    }

    public static void gotoMainMenu() {
        nifty.gotoScreen(SCREEN_MAIN);
    }

    public static void enableGame(boolean isRunning) {
        if (isRunning) {
            nifty.gotoScreen(SCREEN_GAME);
        } else {
            nifty.gotoScreen(SCREEN_PAUSE);
        }
    }
}
