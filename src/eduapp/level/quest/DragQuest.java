package eduapp.level.quest;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Ječmen
 */
public class DragQuest extends QuestItem {

    private final List<DragItem> items;
    private final List<String> extra;
    private boolean finished;

    public DragQuest(String reward) {
        super(reward);
        
        items = new LinkedList<>();
        extra = new LinkedList<>();
        finished = false;
    }

    public void addItem(final DragItem item) {
        items.add(item);
    }
    
    public void addExtra(final String item) {
        extra.add(item);
    }

    public List<DragItem> getItems() {
        return items;
    }

    public List<String> getExtra() {
        return extra;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
        if (finished) {
            this.finish();
        }
    }

    @Override
    public String toNiftyString() {
        return "TODO NiftyString";
    }

    @Override
    public String getTask() {
        return "TODO task";
    }

    public static class DragItem {

        private final DragItemType type;
        private final String text;

        public DragItemType getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public DragItem(DragItemType type, String text) {
            this.type = type;
            this.text = text;
        }
    }

    public static enum DragItemType {

        DRAG,
        STATIC
    }
}
