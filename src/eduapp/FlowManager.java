package eduapp;

import eduapp.level.Player;
import com.jme3.app.state.AppState;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiConversion;
import eduapp.gui.GuiDescription;
import eduapp.gui.GuiDictionary;
import eduapp.gui.GuiEquation;
import eduapp.gui.GuiGroups;
import eduapp.gui.GuiMainMenu;
import eduapp.gui.GuiPexeso;
import eduapp.gui.GuiWorld;
import eduapp.gui.GuiQuest;
import eduapp.gui.GuiQuestInput;
import eduapp.level.item.Item;
import eduapp.level.Light;
import eduapp.level.quest.ConversionQuest;
import eduapp.level.quest.EquationQuest;
import eduapp.level.quest.GroupingQuest;
import eduapp.level.quest.HelpQuest;
import eduapp.level.quest.JmolQuestion;
import eduapp.level.quest.PexesoQuest;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.Question;
import eduapp.level.trigger.Trigger;
import eduapp.screen.StartScreen;
import eduapp.screen.WorldScreen;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final String SCREEN_PEXESO = "pexeso";
    private static final String SCREEN_QUEST = "quest";
    private static final String SCREEN_QUEST_INPUT = "questInput";
    private static final String SCREEN_START = "start";
    private static final String SCREEN_WORLD = "game";
    private static final FlowManager instance;
    private final WorldScreen worldScreen;
    private final StartScreen startScreen;
    private Player player;
    private Nifty nifty;
    private Quest currentQuest;
    private AppState currentState;
    private Stack<String> lastScreens;
    private int finishedLevelCount;

    static {
        instance = new FlowManager();
    }

    private FlowManager() {
        worldScreen = new WorldScreen();
        startScreen = new StartScreen();

        AppContext.getApp().getStateManager().attach(startScreen);
        currentState = startScreen;

        lastScreens = new Stack<>();
        finishedLevelCount = 0;
    }

    public static FlowManager getInstance() {
        return instance;
    }

    public void setLevelState(final int state) {
        finishedLevelCount = state;
        GuiMainMenu control = (GuiMainMenu) nifty.getScreen(SCREEN_START).getScreenController();
        control.setLevelCount(finishedLevelCount);
    }

    public void saveLevelState(final String levelName) {
        final String[] split = levelName.split(" ");
        final int levelVal = Integer.parseInt(split[0].trim());
        if (levelVal > finishedLevelCount) {
            finishedLevelCount = levelVal;
        }
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("data\\state.lvl")))) {
            out.write(finishedLevelCount);
        } catch (IOException ex) {
            Logger.getLogger(FlowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadLevel(String levelName) {
        worldScreen.setLevelName(levelName);

        gotoWorldScreen();
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        control.setDescriptionControls(""
                + "-- Ovládání --\n"
                + "Šipky - pohyb\n"
                + "Mezerník - akce\n"
                + "S - slovník pojmů\n"
                + "Q - seznam úkol\n"
                + "Esc - návrat");
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

    public void displayGroupScreen(final GroupingQuest group) {
        enableState(false);

        final GuiGroups control = (GuiGroups) nifty.getScreen(SCREEN_GROUPS).getScreenController();
        control.setData(group);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_GROUPS);
    }

    public void displayQuestInfo() {
        enableState(false);
        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST);
    }

    public void displayQuestFinish() {
        enableState(false);
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        control.displayEnding();
        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST);
        saveLevelState(worldScreen.getLevelName());
    }

    public void makeQuestActive(final Quest quest) {
        if (quest != null) {
            enableState(false);
            if (quest != currentQuest) {
                final GuiWorld control = (GuiWorld) nifty.getScreen(SCREEN_WORLD).getScreenController();
                control.setItemCout(quest.getFinalQuest().getItemCount());
            }

            currentQuest = quest;
            final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
            control.setQuest(quest);            
            control.displayDescription();
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

    public void displayJmolQuestion(final JmolQuestion question) {
        enableState(false);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final GuiQuestInput control = (GuiQuestInput) nifty.getScreen(SCREEN_QUEST_INPUT).getScreenController();
                Question q;
                boolean allCorrect = true;
                for (String model : question.getModelNames()) {
                    if (JmolUtils.displayModel(model)) {
                        q = new Question("Zadejte vzorec molekuly.", model, null, false);
                        control.setQuestion(q);
                        storeActualScreen();
                        nifty.gotoScreen(SCREEN_QUEST_INPUT);

                        while (!q.isFinished() && !q.isFailed()) {
                            synchronized (FlowManager.this) {
                                try {
                                    FlowManager.this.wait(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(FlowManager.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        allCorrect &= !q.isFailed();
                    }
                }
                question.setFailed(!allCorrect);
                question.setFinished(allCorrect);
                gotoWorldScreen();
                question.finish();
            }
        });
        t.start();
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

    public void displayPexesoScreen(final PexesoQuest quest) {
        enableState(false);
        final GuiPexeso control = (GuiPexeso) nifty.getScreen(SCREEN_PEXESO).getScreenController();
        control.setData(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_PEXESO);
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
            displayQuestInfo();
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

            JmolUtils.closeViewer();
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
