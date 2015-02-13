package eduapp.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.ColorBar;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import eduapp.FlowManager;
import eduapp.level.quest.QuestQuestion;
import java.util.Stack;

/**
 *
 * @author Petr Ječmen
 */
public class GuiQuestInput implements ScreenController {

    private static final String NAME_QUESTION_PANEL = "panelQuest";
    private static final String NAME_QUESTION_TEXT = "questText";
    private static final String NAME_QUESTION_INPUT = "questInput";
    private QuestQuestion question;
    private Element questionText;
    private TextField questionInput;
    private Element panelQuest, inputField;

    public void setQuestion(QuestQuestion question) {
        this.question = question;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        panelQuest = nifty.getCurrentScreen().findElementByName(NAME_QUESTION_PANEL);
        questionText = nifty.getCurrentScreen().findElementByName(NAME_QUESTION_TEXT);
        questionInput = nifty.getCurrentScreen().findNiftyControl(NAME_QUESTION_INPUT, TextField.class);
        inputField = questionInput.getElement().getElements().get(0);
        inputField.getEffects(EffectEventId.onFocus, ColorBar.class).get(0).getParameters().put("color", "#00000000");

        questionInput.getElement().addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(NiftyInputEvent nie) {
                if (nie != null) {
                    switch (nie) {
                        case SubmitText:
                            question.setUserInput(questionInput.getDisplayedText());

                            if (question.isFinished()) {
                                inputField.getEffects(EffectEventId.onFocus, ColorBar.class).get(0).getParameters().put("color", "#00ff00ff");
                                inputField.startEffect(EffectEventId.onFocus);
                                inputField.getEffects(EffectEventId.onFocus, ColorBar.class).get(0).getParameters().put("color", "#00000000");
                                panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                                    @Override
                                    public void perform() {
                                        FlowManager.getInstance().displayLastScreen();
                                    }
                                }, "Ok");
                            } else {
                                inputField.getEffects(EffectEventId.onFocus, ColorBar.class).get(0).getParameters().put("color", "#ff0000ff");
                                inputField.startEffect(EffectEventId.onFocus);
                                inputField.getEffects(EffectEventId.onFocus, ColorBar.class).get(0).getParameters().put("color", "#00000000");
                                if (question.isMustBeCorrect()) {
                                    panelQuest.startEffect(EffectEventId.onCustom, null, "Wrong");
                                } else {
                                    panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                                        @Override
                                        public void perform() {
                                            panelQuest.startEffect(EffectEventId.onCustom, new EndNotify() {
                                                @Override
                                                public void perform() {
                                                    FlowManager.getInstance().displayLastScreen();
                                                }
                                            }, "Ok");
                                        }
                                    }, "Wrong");
                                }
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
        String text = question.getUserInput();
        if (text == null) {
            text = "";
        }
        questionInput.setText(text);
        panelQuest.startEffect(EffectEventId.onCustom, null, "Start");
    }

    @Override
    public void onEndScreen() {
    }
}
