package eduapp.level.quest;

public class WebQuestion extends Question {

    private final String web;

    public WebQuestion(String question, String answer, String web, String reward) {
        super(question, answer, reward, false, false);

        this.web = web;
    }

    public String getWeb() {
        return web;
    }
}
