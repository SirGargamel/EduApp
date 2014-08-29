package eduapp;

import eduapp.level.item.ItemParameters;
import eduapp.level.Player;
import com.jme3.app.state.AppState;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiConversion;
import eduapp.gui.GuiDescription;
import eduapp.gui.GuiDictionary;
import eduapp.gui.GuiEquation;
import eduapp.gui.GuiWorld;
import eduapp.gui.GuiQuest;
import eduapp.gui.GuiQuestInput;
import eduapp.level.item.Item;
import eduapp.level.Light;
import eduapp.level.quest.ConversionQuest;
import eduapp.level.quest.EquationQuest;
import eduapp.level.quest.GroupingQuest;
import eduapp.level.quest.HelpQuest;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.trigger.Trigger;
import eduapp.screen.GroupScreen;
import eduapp.screen.StartScreen;
import eduapp.screen.WorldScreen;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

/**
 *
 * @author Petr Ječmen
 */
public class FlowManager implements Observer {

    private static final String SCREEN_CONVERSION = "conversion";
    private static final String SCREEN_DESCRIPTION = "description";
    private static final String SCREEN_DICTIONARY = "dictionary";
    private static final String SCREEN_DRAG = "drag";
    private static final String SCREEN_GROUPS = "groups";
    private static final String SCREEN_PAUSE = "pause";
    private static final String SCREEN_QUEST = "quest";
    private static final String SCREEN_QUEST_INPUT = "questInput";
    private static final String SCREEN_START = "start";
    private static final String SCREEN_WORLD = "game";
    private static final FlowManager instance;
    private final WorldScreen worldScreen;
    private final StartScreen startScreen;
    private final GroupScreen groupScreen;
    private Player player;
    private Nifty nifty;
    private Quest currentQuest;
    private AppState currentState;
    private Stack<String> lastScreens;

    static {
        instance = new FlowManager();
    }

    private FlowManager() {
        worldScreen = new WorldScreen();
        startScreen = new StartScreen();
        groupScreen = new GroupScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
        currentState = startScreen;

        lastScreens = new Stack<>();
    }

    public static FlowManager getInstance() {
        return instance;
    }

    public void loadLevel(String levelName) {
        worldScreen.setLevelName(levelName);

        gotoWorldScreen();
        final GuiWorld control = (GuiWorld) nifty.getScreen(SCREEN_WORLD).getScreenController();
        control.setNotification("-- Ovládání --\n Šipky - pohyb\n Mezerník - akce\n D - slovník pojmů\n Q - seznam úkolů");
    }

    public void handlePause() {
        boolean isEnabled = currentState.isEnabled();
        enableState(!isEnabled);
        if (isEnabled) {
            storeActualScreen();
            nifty.gotoScreen(SCREEN_PAUSE);
        } else {
            displayLastScreen();
        }
    }

    public void enableState(boolean isEnabled) {
        currentState.setEnabled(isEnabled);
    }

    public void displayLastScreen() {
        nifty.gotoScreen(lastScreens.pop());
    }

    public void storeActualScreen() {
        lastScreens.push(nifty.getCurrentScreen().getScreenId());
    }

    public void exitGame() {
        AppContext.getApp().stop();
    }

    public void setNifty(final Nifty nifty) {
        this.nifty = nifty;
    }

    public void gotoWorldScreen() {
        storeActualScreen();
        nifty.gotoScreen(SCREEN_WORLD);
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(worldScreen);
        worldScreen.setEnabled(true);
        currentState = worldScreen;
    }

    public void gotoMainMenu() {
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(startScreen);
        storeActualScreen();
        nifty.gotoScreen(SCREEN_START);
        currentState = startScreen;
    }

    public void gotoGroupScreen(final GroupingQuest group) {
        groupScreen.setGrouping(group.getGroup());
        groupScreen.setItems(group.getItems());

        lastScreens.push(nifty.getCurrentScreen().getScreenId());
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(groupScreen);
        currentState = groupScreen;
        groupScreen.setEnabled(true);

        displayMessage(group.getGroup().getParam(ItemParameters.DESCRIPTION), SCREEN_GROUPS);
        nifty.gotoScreen(SCREEN_GROUPS);
    }

    public void displayQuest(final Quest quest) {
        if (quest != null) {
            enableState(false);
            currentQuest = quest;
            final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
            control.setQuest(quest);

            storeActualScreen();
            nifty.gotoScreen(SCREEN_QUEST);
        }
    }

    public void displayQuestion(final Question question) {
        enableState(false);
        final GuiQuestInput control = (GuiQuestInput) nifty.getScreen(SCREEN_QUEST_INPUT).getScreenController();
        control.setQuestion(question);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST_INPUT);
    }

    public void displayConversionScreen(final ConversionQuest quest) {
        enableState(false);
        final GuiConversion control = (GuiConversion) nifty.getScreen(SCREEN_CONVERSION).getScreenController();
        control.setQuest(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_CONVERSION);
    }

    public void displayEquationScreen(final EquationQuest quest) {
        enableState(false);
        final GuiEquation control = (GuiEquation) nifty.getScreen(SCREEN_DRAG).getScreenController();
        control.setData(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_DRAG);
    }

    public void dictionaryAction() {
        if (nifty.getCurrentScreen().getScreenId().equals(SCREEN_DICTIONARY)) {
            enableState(true);
            displayLastScreen();
        } else {
            storeActualScreen();
            enableState(false);
            final GuiDictionary control = (GuiDictionary) nifty.getScreen(SCREEN_DICTIONARY).getScreenController();
            control.setItemRegistry(AppContext.getItemRegistry());
            nifty.gotoScreen(SCREEN_DICTIONARY);
        }
    }

    public void questAction() {
        if (nifty.getCurrentScreen().getScreenId().equals(SCREEN_QUEST)) {
            enableState(true);
            displayLastScreen();
        } else {
            storeActualScreen();
            displayQuest(currentQuest);
        }
    }

    public void displayDescription(String message) {
        final GuiWorld control = (GuiWorld) nifty.getScreen(SCREEN_WORLD).getScreenController();
        control.displayDescription(message);
    }

    public void displayMessage(final String message, final String returnScreen) {
        final GuiWorld control = (GuiWorld) nifty.getScreen(SCREEN_WORLD).getScreenController();
        control.setNotification(message);
        if (currentState == worldScreen) {
            if (nifty.getCurrentScreen().getScreenId().equals(SCREEN_WORLD)) {
                control.displayNotification();
            } else {
                displayLastScreen();
            }
        } else {
            final GuiDescription descr = (GuiDescription) nifty.getScreen(SCREEN_DESCRIPTION).getScreenController();
            descr.setMessage(message);
            lastScreens.push(returnScreen);
            nifty.gotoScreen(SCREEN_DESCRIPTION);
        }

        enableState(true);
    }

    public void finishGroupScreen() {
        final int[] results = groupScreen.checkGrouping();
        final String message = "Zařadili jste správně " + results[0] + " předmětů z " + results[1];
        gotoWorldScreen();
        displayMessage(message, SCREEN_WORLD);
    }

    public void finishConversion(int good, int total) {
        final String message = "Převedli jste správně " + good + " věcí z " + total;
        displayMessage(message, SCREEN_WORLD);
    }

    public void finishDrag(boolean result) {
        final String message;
        if (result) {
            message = "Úspěšně jste doplnili rovnici.";
        } else {
            message = "Rovnice nebyla doplněna správně.";
        }
        displayMessage(message, SCREEN_WORLD);
    }

    public void finishQuestItem(final String text) {
        displayMessage(text, SCREEN_WORLD);
    }

    public void assignPlayer(final Player player) {
        final GuiWorld control = (GuiWorld) nifty.getScreen(SCREEN_WORLD).getScreenController();
        control.setPlayer(player);
        this.player = player;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof QuestItem) {
            final QuestItem qi = (QuestItem) o;

            if (qi.isFinished()) {
                FlowManager.getInstance().finishQuestItem("Úkol \"".concat(qi.getTask()).concat("\" byl splněn."));
                player.addItemToInventory(qi.getReward());
            } else if (qi.isFailed()) {
                FlowManager.getInstance().finishQuestItem("Úkol \"".concat(qi.getTask()).concat("\" nebyl splněn."));
            } else if (qi instanceof HelpQuest) {
                final HelpQuest hp = (HelpQuest) qi;
                final Question q = hp.getLastQuestion();
                if (q != null && q.isFinished()) {
                    final QuestItem qi2 = currentQuest.getFailedQuestItem();
                    qi2.setFinished(true);
                    FlowManager.getInstance().finishQuestItem("Úkol \"".concat(qi2.getTask()).concat("\" byl splněn za pomoci bonusové otázky."));
                    player.addItemToInventory(qi2.getReward());
                }
            }

            deactiveChildren(qi);
            enableState(true);
        } else if (o instanceof Player) {
            final GuiWorld control = (GuiWorld) nifty.getScreen(SCREEN_WORLD).getScreenController();
            control.refreshInventoryItems();
        }
    }

    private void deactiveChildren(final Item item) {
        final ItemRegistry itemRegistry = AppContext.getItemRegistry();
        Item i;
        for (String s : item.getChildren()) {
            i = itemRegistry.get(s);
            if (i instanceof Light) {
                Light l = (Light) i;
                l.enableLight(false);
            } else if (i instanceof Trigger) {
                Trigger t = (Trigger) i;
                t.setActive(false);
            } else {
                System.err.println("Illegal child for deactivation - " + s);
            }
        }
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }
}
