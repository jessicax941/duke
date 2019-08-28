package duke.task;

/**
 * Represents a Task in the list.
 */
public class Task {

    protected String desc;
    protected boolean isDone;
    protected String doneSymbol;

    public Task() {}

    public Task(String desc) {
        this.desc = desc;
        this.isDone = false;
        setDoneSymbol();
    }

    public Task(String desc, boolean isDone) {
        this.desc = desc;
        this.isDone = isDone;
        setDoneSymbol();
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void setTask(String desc) {
        this.desc = desc;
    }

    protected String getDoneSymbol() {
        return this.isDone ? "✓" : "x";
    }

    protected void setDoneSymbol() {
        this.doneSymbol = isDone ? "✓" : "x";
    }

    /**
     * Marks a task as done, and changes the doneSymbol to a check mark.
     */
    public void markAsDone() {
        this.isDone = true;
        setDoneSymbol();
    }

}
