package eduapp.level.quest;

/**
 *
 * @author Petr Ječmen
 */
public class QuestFinal extends QuestItem {

    private final String questId;
    private final int itemCount;

    public QuestFinal(final String[] data) {
        super(null);
        finished = false;
        this.questId = data[0];
        this.itemCount = Integer.valueOf(data[1]);
    }    

    public String getQuestId() {
        return questId;
    }

    @Override
    public String toNiftyString() {
        return "Seřaďte nasbírané předměty tak, aby rovnice platila.";
    }    

    public int getItemCount() {
        return itemCount;
    }
}
