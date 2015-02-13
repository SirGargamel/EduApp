package eduapp.level.quest;

public class QuestQuestionWeb extends QuestQuestion {

    private final String web;

    public QuestQuestionWeb(String question, String answer, String web, String reward) {
        super(question, answer, reward, false, false);

        this.web = web;
    }

    public String getWeb() {
        return web;
    }
}
