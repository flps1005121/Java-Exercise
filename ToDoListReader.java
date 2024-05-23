import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ToDoListReader {
    public static ToDoList readToDoList(String fileName) throws IOException {
        ToDoList toDoList = new ToDoList();
        Scanner input = null;

        try {
            input = new Scanner(Paths.get(fileName));

            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String description = parts[1];
                    LocalDate deadline = LocalDate.parse(parts[2]);
                    Task task = new Task(name, description, deadline);
                    toDoList.addTask(task);
                }
            }
        } catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }

        return toDoList;
    }
}
