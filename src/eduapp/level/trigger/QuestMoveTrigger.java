package eduapp.level.trigger;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.ColorRGBA;
import eduapp.level.quest.Quest;


public class QuestMoveTrigger extends MoveTrigger<Quest> {        

    @Override
    public void onEnter() {
        target.makeActive();
    }

    @Override
    public void onLeave() {        
    }

    public QuestMoveTrigger(BoundingVolume volume, Quest target, String action, boolean once) {
        super(volume, target, action, once);                
    }
    
}
