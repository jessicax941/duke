import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a Deadline task.
 */
public class Deadline extends Task {

    private LocalDateTime deadline;

    /**
     * Creates a Deadline task with the associated description and deadline.
     * @param desc Describes the deadline.
     * @param deadline The deadline that the task is due by.
     */
    public Deadline(String desc, String deadline) {
        super(desc);
        this.deadline = stringToDateTime(deadline);
    }

    public Deadline(String desc, boolean isDone, String deadline) {
        super(desc, isDone);
        this.deadline = stringToDateTime(deadline);
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public void setWhen(String deadline) {
        this.deadline = stringToDateTime(deadline);
    }

    private LocalDateTime stringToDateTime(String deadline) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy Hmm", Locale.ENGLISH);
        return LocalDateTime.parse(deadline, formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy Hmm", Locale.ENGLISH);
        return String.format("[%s][%s] %s (by: %s)", "D", super.getDoneSymbol(), this.desc, this.deadline.format(formatter));
    }

}
