package eduapp.level.quest;

/**
 *
 * @author Petr Ječmen
 */
public class QuestFinal extends QuestItem {

    private final String questId;
    private final int itemCount;
    private QuestItem quest;

    public QuestFinal(final String[] data) {
        super(null);
        finished = false;
        this.questId = data[0].toUpperCase();
        this.itemCount = Integer.valueOf(data[1]);
    }    

    public void setQuest(QuestItem quest) {
        this.quest = quest;
    }

    public String getQuestId() {
        return questId;
    }

    @Override
    public String toNiftyString() {
        return quest.toNiftyString();
    }    

    public int getItemCount() {
        return itemCount;
    }
}
