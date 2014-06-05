package eduapp;

import com.jme3.app.state.AppState;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiGame;
import eduapp.gui.GuiQuest;
import eduapp.gui.GuiQuestInput;
import eduapp.gui.GuiNotify;
import eduapp.level.quest.Quest;
import eduapp.level.quest.Question;
import eduapp.state.GroupScreen;
import eduapp.state.StartScreen;
import eduapp.state.WorldScreen;

/**
 *
 * @author Petr Ječmen
 */
public class FlowManager {

    private static final String SCREEN_WORLD = "game";
    private static final String SCREEN_GROUPS = "groups";
    private static final String SCREEN_NOTIFY = "notify";
    private static final String SCREEN_PAUSE = "pause";
    private static final String SCREEN_QUEST = "quest";
    private static final String SCREEN_QUEST_INPUT = "questInput";
    private static final String SCREEN_START = "start";
    private static final WorldScreen worldScreen;
    private static final StartScreen startScreen;
    private static final GroupScreen groupScreen;
    private static PlayerAvatar player;
    private static Nifty nifty;
    private static Quest currentQuest;
    private static AppState currentState;
    private static String lastScreen;

    static {
        worldScreen = new WorldScreen();
        startScreen = new StartScreen();
        groupScreen = new GroupScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
        currentState = startScreen;
    }

    public static void assignPlayerAvatar(final PlayerAvatar player) {
        FlowManager.player = player;
    }

    public static void loadLevel(String levelName) {
        AppContext.getApp().getStateManager().detach(currentState);
        worldScreen.setLevelName(levelName);
        worldScreen.setEnabled(true);
        AppContext.getApp().getStateManager().attach(worldScreen);
        nifty.gotoScreen(SCREEN_WORLD);
        currentState = worldScreen;
    }

    public static void handlePause() {
        boolean isEnabled = currentState.isEnabled();
        enableState(!isEnabled);
        if (isEnabled) {
            nifty.gotoScreen(SCREEN_PAUSE);
        } else {
            displayLastScreen();
        }
    }

    public static void enableState(boolean isEnabled) {
        currentState.setEnabled(isEnabled);
        if (currentState == worldScreen) {
            if (player != null) {
                player.setIsRunning(isEnabled);
            }
        }
    }

    public static void displayLastScreen() {
        nifty.gotoScreen(lastScreen);
    }

    public static void exitGame() {
        AppContext.getApp().stop();
    }

    public static void setNifty(final Nifty nifty) {
        FlowManager.nifty = nifty;
    }

    public static void gotoWorldScreen() {
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(worldScreen);
        currentState = worldScreen;
        lastScreen = SCREEN_WORLD;
        nifty.gotoScreen(SCREEN_WORLD);
    }

    public static void gotoMainMenu() {
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(startScreen);
        nifty.gotoScreen(SCREEN_START);
        currentState = startScreen;
    }

    public static void gotoGroupScreen() {
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(groupScreen);
        lastScreen = SCREEN_GROUPS;
        nifty.gotoScreen(SCREEN_GROUPS);
        currentState = groupScreen;
    }

    public static void displayQuest(final Quest quest) {
        enableState(false);
        currentQuest = quest;
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        control.setQuest(quest);
        nifty.gotoScreen(SCREEN_QUEST);
    }

    public static void displayQuestion(final Question question) {
        enableState(false);
        final GuiQuestInput control = (GuiQuestInput) nifty.getScreen(SCREEN_QUEST_INPUT).getScreenController();
        control.setQuestion(question);
        nifty.gotoScreen(SCREEN_QUEST_INPUT);
    }

    public static void questAction() {
        if (nifty.getCurrentScreen().getScreenId().equals(SCREEN_QUEST)) {
            enableState(true);
            displayLastScreen();
        } else {
            displayQuest(currentQuest);
        }
    }

    public static void showTriggerMarker(boolean show) {
        final GuiGame control = (GuiGame) nifty.getScreen(SCREEN_WORLD).getScreenController();
        control.enableQuestMarker(show);
    }

    public static void displayMessage(final String message) {
        final GuiNotify control = (GuiNotify) nifty.getScreen(SCREEN_NOTIFY).getScreenController();
        control.setMessage(message);
        nifty.gotoScreen(SCREEN_NOTIFY);
        enableState(true);
    }

    public static void finishGroupScreen() {
        final int[] results = groupScreen.checkGrouping();
        final String message = "Zařadili jste správně " + results[0] + " předmětů z " + results[1];
        displayMessage(message);
        gotoWorldScreen();
    }

    // -------
    public static void debug() {
        AppContext.getApp().getStateManager().detach(currentState);

        final ItemRegistry ir = AppContext.getItemRegistry();
        groupScreen.setGrouping(ir.get("skup"));
        groupScreen.setItems(ir.get("h2so4"), ir.get("v2o5"), ir.get("s"), ir.get("sio2"), ir.get("so2"));

        worldScreen.setLevelName("Empty");

        gotoGroupScreen();
    }
}
