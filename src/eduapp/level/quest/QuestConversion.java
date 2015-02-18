package eduapp.level.quest;

import eduapp.level.item.ItemParameter;
import eduapp.level.item.Item;
import static eduapp.level.quest.QuestItem.MAX_ERROR_COUNT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuestConversion extends QuestItem {

    private final String conversionId;
    private final String[] itemList;
    private Item conversion;
    private List<Item> items;

    public QuestConversion(String conversionId, String data, String reward) {
        super(reward);

        this.conversionId = conversionId;
        final String[] split = data.split(ItemParameter.SPLITTER);
        itemList = new String[split.length];
        for (int i = 0; i < itemList.length; i++) {
            itemList[i] = split[i].trim();
        }
    }

    public Item getConversion() {
        initConversion();
        return conversion;
    }

    public Collection<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>(itemList.length);
            for (String s : itemList) {
                items.add(itemRegistry.get(s));
            }
        }
        Collections.shuffle(items);
        return items;
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
        initConversion();

        return conversion.getParam(ItemParameter.DESCRIPTION);
    }

    private void initConversion() {
        if (conversion == null) {
            conversion = itemRegistry.get(conversionId);
        }
    }
}
