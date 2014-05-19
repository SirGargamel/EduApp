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
    private static final String SCREEN_QUEST = "quest";
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

    public static void displayQuest(final String questName) {
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        control.setQuestText(questName);
        nifty.gotoScreen(SCREEN_QUEST);
    }
}
