import duke.exception.InvalidCommandException;
import duke.exception.MissingDescriptionException;
import duke.exception.MissingInputException;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;

import java.util.ArrayList;

/**
 * Represents a Parser object that deals with making sense of the user command.
 */
public class Parser {

    private Ui ui;

    /**
     * An empty constructor for a Parser object.
     */
    public Parser() {
        this.ui = new Ui();
    }

    /**
     * Executes the specified command by the user.
     * @param command The specified command given by the user.
     * @throws InvalidCommandException if an invalid or recognisable command is given by the user.
     * @throws MissingInputException if there are missing inputs when creating a Deadline or Event task, such as the
     * deadline or event time and day.
     * @throws MissingDescriptionException if a description is missing for the task that the user is trying to create.
     */
    public String executeCommand(String command) throws InvalidCommandException, MissingInputException,
            MissingDescriptionException {
        String[] commandWords = command.trim().split(" ");
        String commandType = commandWords[0];
        String output = "";
        TaskList taskList = new TaskList();

        switch (commandType) {
        case "list":
            ArrayList<Task> tasks = taskList.getTaskList();
            //.printList(tasks);
            output = ui.getListResponse(tasks);
            break;
        case "done":
            int taskNumber = Integer.parseInt(commandWords[1]);
            output = taskList.markAsDone(taskNumber);
            break;
        case "delete":
            int taskNumber2 = Integer.parseInt(commandWords[1]);
            output = taskList.deleteTask(taskNumber2);
            break;
        case "find":
            output = taskList.findMatchingTasks(commandWords[1]);
            break;
        case "todo":
            // Fallthrough
        case "deadline":
            // Fallthrough
        case "event":
            output = addTask(command, commandType);
            break;
        case "bye":
            output = this.ui.getByeResponse();
            break;
        default:
            throw new InvalidCommandException("\t☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        return output;
    }

    /**
     * Adds a task to the task list.
     * @param command The specified command given by the user.
     * @param taskType The type of the task that the user wants to add to the tasks list.
     * @throws MissingDescriptionException if a description is missing for the task that the user is trying to create.
     * @throws MissingInputException if there are missing inputs when creating a Deadline or Event task, such as the
     * deadline or event time and day.
     */
    private String addTask(String command, String taskType) throws MissingDescriptionException, MissingInputException {
        String desc = command.substring(taskType.length()).trim();
        Task task = new Task();

        if (desc.isEmpty()) {
            throw new MissingDescriptionException("☹ OOPS!!! The description of " + taskType + " cannot be empty.");
        }

        switch (taskType) {
        case ("todo"):
            task = new Todo(desc);
            break;
        case ("deadline"):
            if (!desc.contains("/by")) {
                throw new MissingInputException("☹ OOPS!!! The deadline cannot be found because /by is missing");
            }

            String[] splitDeadlineDesc = desc.split("/by");
            desc = splitDeadlineDesc[0].trim(); // first element in string array is solely the task description
            // second element in string array is the deadline of the task, unless it is not found

            String deadline;
            try {
                deadline = splitDeadlineDesc[1].trim();
            } catch (ArrayIndexOutOfBoundsException e) {
                // above exception will be thrown when the splitDeadlineDesc only has one element
                // this means that there is nothing after /by
                throw new MissingInputException("☹ OOPS!!! The deadline cannot be found after /by");
            }

            task = new Deadline(desc, deadline);
            break;
        case ("event"):
            if (!desc.contains("/at")) {
                throw new MissingInputException("☹ OOPS!!! The event date and time cannot be found because /at is missing");
            }

            String[] splitEventDesc = desc.split("/at"); // first element is simply the string description
            // second element would be the event time/day, unless it is not found
            desc = splitEventDesc[0].trim();

            String when;
            try {
                when = splitEventDesc[1].trim();
            } catch (ArrayIndexOutOfBoundsException e) {
                // above exception will be thrown when the splitEventDesc only has one element
                // this means that there is nothing after /at
                throw new MissingInputException("☹ OOPS!!! The event date and time cannot be found after /at");
            }

            task = new Event(desc, when);
            break;
        default:
            break;
        }

        TaskList taskList = new TaskList();
        return taskList.addTask(task);
    }

}
