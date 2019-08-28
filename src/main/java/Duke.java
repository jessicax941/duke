import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents Duke, a personal chatbot assistant.
 */
public class Duke {

    private ArrayList<Task> list;

    private Duke() {
        this.list = new ArrayList<>();
    }

    public ArrayList<Task> getList() {
        return this.list;
    }

    public void setList(ArrayList<Task> list) {
        this.list = list;
    }

    public static void main(String[] args) {
        Duke duke = new Duke();
        duke.hello();
        try {
            duke.readFile("src/main/java/tasklists.txt");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        //take in user input and print according to user command
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.nextLine();
            command = command.trim();
            if (command.equals("bye")) { // user wants to exit
                duke.printExitMessage();
                return; // program terminates
            } else {
                try {
                    duke.execCommand(command);
                } catch (InvalidCommandException e) {
                    System.out.println("\t" + e.getMessage());
                }
            }
        }
    }

    /**
     * Prints out all the tasks in a numbered list.
     */
    private void printList() {
        System.out.println("\tHere are the tasks in your list:");
        for (int i = 0; i < this.list.size(); i++) {
            System.out.println(String.format("\t%d.%s", i + 1, this.list.get(i)));
        }
    }

    /**
     * Prints hello message.
     */
    private void hello() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        //System.out.println("Hello from\n" + logo);
        String openingMessage = "\tHello! I'm Duke\n"
                + "\tWhat can I do for you?\n";
        System.out.println(openingMessage);
    }

    // execute command given depending on what command it 

    /**
     * Executes the command given by the user. 
     * @param command The command input by the user.
     * @throws InvalidCommandException This exception is thrown when the command is invalid or not recognisable.
     */
    private void execCommand(String command) throws InvalidCommandException {
        String[] commandStringArray = command.trim().split(" "); //split by words
        String firstWord = commandStringArray[0];
        if (command.equals("list")) {
            printList();
        } else if (firstWord.equals("done") && commandStringArray.length == 2) {
            // when the command is done and followed by task number
            int taskNo = Integer.parseInt(commandStringArray[1]);
            markTaskAsDone(taskNo);
        } else if (firstWord.equals("todo") || firstWord.equals("deadline") || firstWord.equals("event")) {
            // check if the taskType is a valid command before adding the task to the tasks list
            try {
                addTask(command, firstWord);
            } catch (MissingDescriptionException | MissingInputException e) {
                // taskType is valid but missing arguments
                System.out.println("\t" + e.getMessage());
            }
        } else if (firstWord.equals("delete") && commandStringArray.length == 2) {
            // when the command is delete and is followed by task number
            int taskNo = Integer.parseInt(commandStringArray[1]);
            deleteTask(taskNo);
        } else {
            // taskType is not a valid command, throw IllegalArgumentException
            throw new InvalidCommandException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Marks a task as done (completed).
     * @param taskNo The position of the task in the list that is printed out
     *               (not the index of the task in the stored ArrayList).
     */
    private void markTaskAsDone(int taskNo) {
        Task taskDone = this.list.get(taskNo - 1);
        taskDone.markAsDone();
        System.out.println("\tNice! I've marked this task as done:\n\t\t" + taskDone.toString());
    }

    /**
     * Adds a task to the tasks list according to the user's command.
     * @param command The command that was input by the user.
     * @param taskType The type of the task that the user wants to add to the list.
     * @throws MissingDescriptionException This exception is thrown when the type of the task is valid but its
     * description is not to be found.
     * @throws MissingInputException This exception is thrown when a certain type of input is not found,
     * namely, the deadline of a Deadline task, and the event time/day for an Event task.
     */
    private void addTask(String command, String taskType) throws MissingDescriptionException, MissingInputException {
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
        this.list.add(task);
        printAddedTask(task);
    }

    /**
     * Prints the necessary lines when a task is added to the tasks list.
     * @param task The task that is being added to the tasks list.
     */
    private void printAddedTask(Task task) {
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t  " + task.toString());
        System.out.println(String.format("\tNow you have %d tasks in the list.", this.list.size()));
    }

    /**
     * Deletes a task from the tasks list.
     * @param taskNo The position of the task in the list when it is printed out
     *               (not the index of the task in the stored ArrayList).
     */
    private void deleteTask(int taskNo) {
        Task removedTask = this.list.remove(taskNo - 1);
        System.out.println("\tNoted. I've removed this task:");
        System.out.println("\t  " + removedTask.toString());
        System.out.println(String.format("\tNow you have %d tasks in the list.", this.list.size()));
    }

    private void printExitMessage() {
        System.out.println("\tBye. Hope to see you again soon!");
    }

    private void readFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.nextLine(); // eg. T|0|read book
            String[] taskDetails = line.split(",");
            String taskType = taskDetails[0];
            int booleanInt = Integer.parseInt(taskDetails[1]);
            switch (taskType) {
            case "T":
                Todo todo = new Todo(taskDetails[2], booleanInt == 0);
                this.list.add(todo);
                break;
            case "D":
                Deadline deadline = new Deadline(taskDetails[2], booleanInt == 0, taskDetails[3]);
                this.list.add(deadline);
                break;
            case "E":
                Event event = new Event(taskDetails[2], booleanInt == 0, taskDetails[3]);
                this.list.add(event);
                break;
            default:
                break;
            }
        }

    }
}
