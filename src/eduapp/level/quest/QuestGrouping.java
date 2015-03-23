package eduapp.level.quest;

import eduapp.level.item.ItemParameter;
import eduapp.level.item.Item;
import eduapp.level.item.VirtualItem;

public class QuestGrouping extends QuestItem {

    private final String groupId;
    private final String[] itemList;
    private Item group;
    private Item[] items;

    public QuestGrouping(String groupId, String data, String reward) {
        super(reward);

        this.groupId = groupId;
        final String[] split = data.split(ItemParameter.SPLITTER);
        itemList = new String[split.length];
        for (int i = 0; i < itemList.length; i++) {
            itemList[i] = split[i].trim();
        }
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

    public void setResult(int correct) {
        if (itemList.length - correct <= MAX_ERROR_COUNT) {
            finished = true;
        } else {
            failed = true;
        }
        finish();
    }

    @Override
    public String toNiftyString() {
        initGroup();
        if (group != null && !(group instanceof VirtualItem)) {
            return group.getParam(ItemParameter.DESCRIPTION);
        } else {
            return "Rozřaďtě prvky dle toho, jestli mají danou vlastnost - " + itemRegistry.get("description").getParam(groupId);
        }
    }

    private void initGroup() {
        if (group == null) {
            group = itemRegistry.get(groupId);
        }
    }
}
