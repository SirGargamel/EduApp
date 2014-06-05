package eduapp.level.quest;

import eduapp.ItemParameters;
import eduapp.level.Item;

public class ConversionQuest extends QuestItem {

    private static final double RATIO_SUCCESS = 0.8;
    private final String conversionId;
    private final String[] itemList;
    private Item conversion;
    private Item[] items;
    private int correct;
    private boolean finished;

    public ConversionQuest(String conversionId, String data) {
        this.conversionId = conversionId;        
        final String[] split = data.split(ItemParameters.SPLITTER);
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

    public Item[] getItems() {
        if (items == null) {
            items = new Item[itemList.length];
            for (int i = 0; i < items.length; i++) {
                items[i] = itemRegistry.get(itemList[i]);
            }
        }
        return items;
    }

    public void setResult(int value) {
        this.correct = value;
        if (correct / (double) itemList.length >= RATIO_SUCCESS) {
            finished = true;
            finish();
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toNiftyString() {
        initConversion();
        
        final StringBuilder sb = new StringBuilder();        
        sb.append(conversion.getParam(ItemParameters.NAME));
        return sb.toString();
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
