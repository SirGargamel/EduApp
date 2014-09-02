package eduapp.level.quest;

import java.util.*;

/**
 *
 * @author Petr Ječmen
 */
public class PexesoQuest extends QuestItem {

    private final List<String> items;

    public PexesoQuest(String[] items, String reward) {
        super(reward);

        this.items = Arrays.asList(items);
        Collections.shuffle(this.items);
    }

    public Collection<String> getItemNames() {
        return items;
    }

    @Override
    public String toNiftyString() {
        return "Vyhrajte pexeso.";
    }

    @Override
    public String getTask() {
        return toNiftyString();
    }
}
