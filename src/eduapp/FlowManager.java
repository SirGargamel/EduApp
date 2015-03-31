package eduapp;

import eduapp.level.Player;
import com.jme3.app.state.AppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import de.lessvoid.nifty.Nifty;
import eduapp.gui.GuiAdding;
import eduapp.gui.GuiConversion;
import eduapp.gui.GuiDescription;
import eduapp.gui.GuiDictionary;
import eduapp.gui.GuiEquation;
import eduapp.gui.GuiGroups;
import eduapp.gui.GuiMainMenu;
import eduapp.gui.GuiMatching;
import eduapp.gui.GuiMultiAnswer;
import eduapp.gui.GuiOrdering;
import eduapp.gui.GuiPexeso;
import eduapp.gui.GuiPicking;
import eduapp.gui.GuiWorld;
import eduapp.gui.GuiQuest;
import eduapp.gui.GuiQuestInput;
import eduapp.level.item.Item;
import eduapp.level.Light;
import eduapp.level.quest.QuestConversion;
import eduapp.level.quest.QuestEquation;
import eduapp.level.quest.QuestGrouping;
import eduapp.level.quest.QuestHelp;
import eduapp.level.quest.QuestQuestionJmol;
import eduapp.level.quest.QuestQuestionMultiAnswer;
import eduapp.level.quest.QuestOrdering;
import eduapp.level.quest.QuestPexeso;
import eduapp.level.quest.Quest;
import eduapp.level.quest.QuestAdding;
import eduapp.level.quest.QuestItem;
import eduapp.level.quest.QuestMatching;
import eduapp.level.quest.QuestPicking;
import eduapp.level.quest.QuestQuestion;
import eduapp.level.trigger.Trigger;
import eduapp.loaders.LevelLoader;
import eduapp.loaders.StateLoader;
import eduapp.screen.StartScreen;
import eduapp.screen.WorldScreen;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
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
    private static final String SCREEN_ORDERING = "ordering";
    private static final String SCREEN_PAUSE = "pause";
    private static final String SCREEN_PEXESO = "pexeso";
    private static final String SCREEN_QUEST = "quest";
    private static final String SCREEN_QUEST_ADDING = "add";
    private static final String SCREEN_QUEST_INPUT = "questInput";
    private static final String SCREEN_QUEST_MULTI = "multiAnswer";
    private static final String SCREEN_QUEST_MATCHING = "matching";
    private static final String SCREEN_QUEST_PICKING = "picking";
    private static final String SCREEN_START = "start";
    private static final String SCREEN_WORLD = "game";
    private static final FlowManager instance;
    private final WorldScreen worldScreen;
    private final StartScreen startScreen;
    private final Stack<String> lastScreens;
    private Player player;
    private Nifty nifty;
    private Quest currentQuest;
    private AppState currentState;
    private int finishedLevelCount;
    private String levelName;

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
            setLevelState(levelVal);
        }
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("data\\" + StateLoader.STATE_FILE)))) {
            out.write(finishedLevelCount);
        } catch (IOException ex) {
            Logger.getLogger(FlowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadLevel(String levelName) {
        final AssetManager am = AppContext.getApp().getAssetManager();
        if (this.levelName != null) {
            am.unregisterLocator(this.levelName.concat(".").concat(LevelLoader.EXTENSION_PACKAGE), ZipLocator.class);
            this.levelName = null;
        }

        this.levelName = levelName;
        am.registerLocator("data\\".concat(levelName).concat(".").concat(LevelLoader.EXTENSION_PACKAGE), ZipLocator.class);

        worldScreen.setLevelName(levelName);

        gotoWorldScreen();
        final GuiQuest control = (GuiQuest) nifty.getScreen(SCREEN_QUEST).getScreenController();
        control.setDescriptionControls(
                "-- Ovládání --",
                "Šipky - pohyb\n"
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
        worldScreen.manualCleanup();
        AppContext.getApp().getStateManager().detach(currentState);
        AppContext.getApp().getStateManager().attach(startScreen);
        storeActualScreen();
        nifty.gotoScreen(SCREEN_START);
        currentState = startScreen;
    }

    public void displayGroupScreen(final QuestGrouping group) {
        enableState(false);

        final GuiGroups control = (GuiGroups) nifty.getScreen(SCREEN_GROUPS).getScreenController();
        control.setQuest(group);

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

    public void displayAddingScreen(final QuestAdding quest) {
        enableState(false);
        final GuiAdding control = (GuiAdding) nifty.getScreen(SCREEN_QUEST_ADDING).getScreenController();
        control.setQuest(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST_ADDING);
    }

    public void displayQuestion(final QuestQuestion question) {
        enableState(false);
        final GuiQuestInput control = (GuiQuestInput) nifty.getScreen(SCREEN_QUEST_INPUT).getScreenController();
        control.setQuestion(question);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST_INPUT);
    }

    public void displayQuestionMulti(final QuestQuestionMultiAnswer question) {
        enableState(false);
        final GuiMultiAnswer control = (GuiMultiAnswer) nifty.getScreen(SCREEN_QUEST_MULTI).getScreenController();
        control.setQuestion(question);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST_MULTI);
    }

    public void displayJmolQuestion(final QuestQuestionJmol question) {
        enableState(false);

        Thread t = new Thread(() -> {
            final GuiQuestInput control = (GuiQuestInput) nifty.getScreen(SCREEN_QUEST_INPUT).getScreenController();
            QuestQuestion q;
            int correctCounter = 0;
            final String questionText = question.getQuestion();
            for (Entry<String, String> e : question.getData().entrySet()) {
                if (JmolUtils.displayModel(e.getKey())) {
                    q = new QuestQuestion(questionText, e.getValue(), null, false, true);
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
                    if (!q.isFailed()) {
                        correctCounter++;
                    }
                }
            }
            JmolUtils.hideViewer();
            question.setResult(correctCounter);
            gotoWorldScreen();
        });
        t.start();
    }

    public void displayConversionScreen(final QuestConversion quest) {
        enableState(false);
        final GuiConversion control = (GuiConversion) nifty.getScreen(SCREEN_CONVERSION).getScreenController();
        control.setQuest(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_CONVERSION);
    }

    public void displayEquationScreen(final QuestEquation quest) {
        enableState(false);
        final GuiEquation control = (GuiEquation) nifty.getScreen(SCREEN_DRAG).getScreenController();
        control.setQuest(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_DRAG);
    }

    public void displayPexesoScreen(final QuestPexeso quest) {
        enableState(false);
        final GuiPexeso control = (GuiPexeso) nifty.getScreen(SCREEN_PEXESO).getScreenController();
        control.setData(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_PEXESO);
    }

    public void displayPickingScreen(final QuestPicking quest) {
        enableState(false);
        final GuiPicking control = (GuiPicking) nifty.getScreen(SCREEN_QUEST_PICKING).getScreenController();
        control.setData(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST_PICKING);
    }

    public void displayOrderingScreen(final QuestOrdering quest) {
        enableState(false);
        final GuiOrdering control = (GuiOrdering) nifty.getScreen(SCREEN_ORDERING).getScreenController();
        control.setData(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_ORDERING);
    }

    public void displayMatchingScreen(final QuestMatching quest) {
        enableState(false);
        final GuiMatching control = (GuiMatching) nifty.getScreen(SCREEN_QUEST_MATCHING).getScreenController();
        control.setQuest(quest);

        storeActualScreen();
        nifty.gotoScreen(SCREEN_QUEST_MATCHING);
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
                FlowManager.getInstance().finishQuestItem("Úkol \"".concat(qi.toNiftyString()).concat("\" byl splněn."));
                player.addItemToInventory(qi.getReward());
            } else if (qi.isFailed()) {
                FlowManager.getInstance().finishQuestItem("Úkol \"".concat(qi.toNiftyString()).concat("\" nebyl splněn."));
            } else if (qi instanceof QuestHelp) {
                final QuestHelp hp = (QuestHelp) qi;
                final QuestQuestion q = hp.getLastQuestion();
                if (q != null && q.isFinished()) {
                    final QuestItem qi2 = currentQuest.getFailedQuestItem();
                    if (qi2 != null) {
                        qi2.setFinished(true);
                        FlowManager.getInstance().finishQuestItem("Úkol \"".concat(qi2.toNiftyString()).concat("\" byl splněn za pomoci bonusové otázky."));
                        player.addItemToInventory(qi2.getReward());
                    }
                }
            }

            if (currentQuest.isFinished()) {
                displayQuestFinish();
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
