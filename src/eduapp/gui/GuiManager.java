package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import eduapp.level.Quest;
import eduapp.state.StateManager;

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
    
    public static void gotoQuestScreen() {
        nifty.gotoScreen(SCREEN_QUEST);
    }
    
    public static void gotoPauseScreen() {
        nifty.gotoScreen(SCREEN_PAUSE);
    }

    public static void enableGame(boolean isRunning) {
        if (isRunning) {
            gotoGameScreen();
        } else {
            gotoPauseScreen();
        }
    }

    public static void displayQuest(final Quest quest) {
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        final StringBuilder sb = new StringBuilder(quest.getId());
        for (String s : quest.getData()) {
            sb.append("\n");
            sb.append(s);
        }
        control.setQuestText(sb.toString());
        StateManager.enableGame(false);
        gotoQuestScreen();
    }
    
    public static void questAction() {
        if (nifty.getCurrentScreen().getScreenId().equals(SCREEN_QUEST)) {
            gotoGameScreen();
        } else {
            gotoQuestScreen();
        }
    }
}
