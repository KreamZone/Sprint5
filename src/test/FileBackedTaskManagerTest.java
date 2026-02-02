package test;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import taskManager.FileBackedTaskManager;
import taskManager.InMemoryTaskManager;
import taskStatus.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoadEmptyFile() throws IOException {

        File testFile = File.createTempFile("test", ".txt");
        FileBackedTaskManager emptyManager = new FileBackedTaskManager(testFile.getAbsolutePath());

        assertTrue(testFile.exists(), "Файл должен быть создан");

        List<String> lines = emptyManager.read();

        assertEquals("id,type,name,status,description,epic", lines.get(0));
    }

    @Test
    public void testSaveAndLoadNotEmptyFile(){
        try {
            File file = File.createTempFile("test", ".txt");
            FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
            Task task = new Task("task","taskDecr", TaskStatus.NEW);
            Epic epic = new Epic("epic","epicDecr", TaskStatus.NEW);
            Subtask subtask = new Subtask("subtask","subtaskDecr", TaskStatus.NEW,epic);

            manager.addNewTask(task);
            manager.addNewEpic(epic);
            manager.addNewSubtask(subtask, epic);

            List<String> lines = manager.read();
            assertEquals(task.getTaskID().toString() + ",TASK,task,NEW,taskDecr", lines.get(1));
            assertEquals(epic.getTaskID().toString() + ",EPIC,epic,NEW,epicDecr", lines.get(2));
            assertEquals(subtask.getTaskID().toString() + ",SUBTASK,subtask,NEW,subtaskDecr," +subtask.getMasterTask().getTaskID().toString(), lines.get(3));

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
