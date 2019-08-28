import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents an Event task.
 */
public class Event extends Task {

    private LocalDateTime when;

    /**
     * Creates an Event task with the associated description and event day and time.
     * @param desc Describes the event.
     * @param when The event day and event time.
     */
    public Event(String desc, String when) {
        super(desc);
        this.when = stringToDateTime(when);
    }

    public Event(String desc, boolean isDone, String when) {
        super(desc, isDone);
        this.when = stringToDateTime(when);
    }

    public LocalDateTime getWhen() {
        return this.when;
    }

    public void setWhen(String when) {
        this.when = stringToDateTime(when);
    }

    private LocalDateTime stringToDateTime(String when) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy Hmm", Locale.ENGLISH);
        return LocalDateTime.parse(when, formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy Hmm", Locale.ENGLISH);
        return String.format("[%s][%s] %s (at: %s)", "E", super.getDoneSymbol(), this.desc, this.when.format(formatter));
    }
}
