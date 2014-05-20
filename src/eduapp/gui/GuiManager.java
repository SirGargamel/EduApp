package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
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
    private static final String SCREEN_QUEST_INPUT = "questInput";
    private static Nifty nifty;
    private static Quest currentQuest;

    public static void setNifty(final Nifty nifty) {
        GuiManager.nifty = nifty;
    }

    public static void gotoGameScreen() {
        StateManager.enableGame(true);
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
        currentQuest = quest;
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        final StringBuilder sb = new StringBuilder(currentQuest.getId());
        for (QuestItem s : currentQuest.getData()) {
            sb.append("\n");
            sb.append(s.toNiftyString());
        }
        control.setQuestText(sb.toString());
        StateManager.enableGame(false);
        gotoQuestScreen();
    }
    
    public static void displayQuestion(final Question question) {
        final GuiQuestInput control = (GuiQuestInput) nifty.getScreen(SCREEN_QUEST_INPUT).getScreenController();
        control.setQuestion(question);        
        StateManager.enableGame(false);
        nifty.gotoScreen(SCREEN_QUEST_INPUT);
    }
    
    public static void questAction() {
        if (nifty.getCurrentScreen().getScreenId().equals(SCREEN_QUEST)) {
            gotoGameScreen();
        } else {
            displayQuest(currentQuest);
        }
    }
}
