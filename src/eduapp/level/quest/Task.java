package eduapp.level.quest;

public class Task extends QuestItem {

    private final String task;
    private boolean done;

    public Task(String task) {
        this.task = task;
    }

    public void setDone(boolean done) {
        this.done = done;
        level.deactivateTrigger(getId());
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public String toNiftyString() {
        final String result;
        if (done) {
            result = task.concat("  OK");
        } else {
            result = task;
        }
        return result;
    }
}
