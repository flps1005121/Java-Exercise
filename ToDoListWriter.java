import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ToDoListWriter {
    public static void writeToDoList(String fileName, ToDoList toDoList) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName));

            for (Task task : toDoList.getTasks()) {
                writer.write(task.getName() + "," + task.getDescription() + "," + task.getDeadline());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
