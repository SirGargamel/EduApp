package eduapp.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import eduapp.level.quest.Question;

/**
 *
 * @author Petr Ječmen
 */
public class GuiQuestInput implements ScreenController {

    private static final String NAME_QUESTION_TEXT = "questText";
    private static final String NAME_QUESTION_INPUT = "questInput";
    private Question question;
    private Element questionText;
    private TextField questionInput;

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
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
                                GuiManager.gotoGameScreen();
                            } else {
                                questionInput.setText("Wrong !!!");
                            }
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
    }

    @Override
    public void onEndScreen() {
    }
}
