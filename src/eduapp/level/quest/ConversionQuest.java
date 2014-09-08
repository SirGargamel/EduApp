package eduapp.level.quest;

import eduapp.level.item.ItemParameter;
import eduapp.level.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConversionQuest extends QuestItem {

    private static final double RATIO_SUCCESS = 0.8;
    private final String conversionId;
    private final String[] itemList;
    private Item conversion;
    private List<Item> items;
    private int correct;

    public ConversionQuest(String conversionId, String data, String reward) {
        super(reward);

        this.conversionId = conversionId;
        final String[] split = data.split(ItemParameter.SPLITTER);
        itemList = new String[split.length];
        for (int i = 0; i < itemList.length; i++) {
            itemList[i] = split[i].trim();
        }

        correct = 0;
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
        initConversion();

        return conversion.getParam(ItemParameter.DESCRIPTION);
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }

    private void initConversion() {
        if (conversion == null) {
            conversion = itemRegistry.get(conversionId);
        }
    }
}
