package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.FlowManager;
import eduapp.level.quest.Question;

/**
 *
 * @author Petr Ječmen
 */
public class GuiQuestInput implements ScreenController {

    private static final String NAME_QUESTION_PANEL = "panelQuest";
    private static final String NAME_QUESTION_TEXT = "questText";
    private static final String NAME_QUESTION_INPUT = "questInput";
    private Question question;
    private Element questionText;
    private TextField questionInput;
    private Element panelQuest;

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        panelQuest = nifty.getCurrentScreen().findElementByName(NAME_QUESTION_PANEL);
        questionText = nifty.getCurrentScreen().findElementByName(NAME_QUESTION_TEXT);
        questionInput = nifty.getCurrentScreen().findNiftyControl(NAME_QUESTION_INPUT, TextField.class);
        questionInput.getElement().addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(NiftyInputEvent nie) {
                if (nie != null) {
                    switch (nie) {
                        case SubmitText:
                            question.setUserInput(questionInput.getDisplayedText());
                            if (question.isFinished()) {
                                panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                                    @Override
                                    public void perform() {
                                        FlowManager.getInstance().displayLastScreen();
                                    }
                                }, "Ok");
                            } else {
                                panelQuest.startEffect(EffectEventId.onCustom, null, "Wrong");
                            }
                            break;
                        case Escape:
                            question.setUserInput(questionInput.getDisplayedText());
                            FlowManager.getInstance().displayLastScreen();
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onStartScreen() {
        questionText.getRenderer(TextRenderer.class).setText(question.getQuestion());
        questionInput.setText(question.getUserInput());
        panelQuest.startEffect(EffectEventId.onCustom, null, "Start");
    }

    @Override
    public void onEndScreen() {
    }
}
