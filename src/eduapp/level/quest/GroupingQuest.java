package eduapp.level.quest;

import eduapp.level.item.ItemParameter;
import eduapp.level.item.Item;

public class GroupingQuest extends QuestItem {

    private static final double RATIO_SUCCESS = 0.8;
    private final String groupId;
    private final String[] itemList;
    private Item group;
    private Item[] items;
    private int correct;

    public GroupingQuest(String groupId, String data, String reward) {
        super(reward);

        this.groupId = groupId;
        final String[] split = data.split(ItemParameter.SPLITTER);
        itemList = new String[split.length];
        for (int i = 0; i < itemList.length; i++) {
            itemList[i] = split[i].trim();
        }

        correct = 0;
    }

    public Item getGroup() {
        initGroup();
        return group;
    }

    public Item[] getItems() {
        if (items == null) {
            items = new Item[itemList.length];
            for (int i = 0; i < items.length; i++) {
                items[i] = itemRegistry.get(itemList[i]);
            }
        }
        return items;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setResult(int value) {
        this.correct = value;
        if (correct / (double) itemList.length >= RATIO_SUCCESS) {
            finished = true;
        } else {
            failed = true;
        }
        finish();
    }

    @Override
    public String toNiftyString() {
        initGroup();
        if (group != null) {
            return group.getParam(ItemParameter.DESCRIPTION);
        } else {
            return "Rozřaďtě prvky podle toho, jestli danou vlastnost mají nebo ne - " + groupId;
        }
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }

    private void initGroup() {
        if (group == null) {
            group = itemRegistry.get(groupId);
        }
    }
}
