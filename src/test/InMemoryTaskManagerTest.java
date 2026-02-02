package test;

import taskManager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import taskStatus.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.testng.Assert.assertTrue;


class InMemoryTaskManagerTest {


    @Test
    public void CanRemoveByID(){
        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        Epic epic = new Epic("epic","epicDescr",TaskStatus.NEW);
        Subtask subtask = new Subtask("subtask","subtaskDecr",TaskStatus.NEW, epic);

        List<Subtask> listSubtask = new ArrayList<>();
        List<Epic> listEpic = new ArrayList<>();
        List<Task> listTask = new ArrayList<>();

        taskManagerTest.addNewTask(task);
        taskManagerTest.addNewEpic(epic);
        taskManagerTest.addNewSubtask(subtask,epic);

        taskManagerTest.deleteSubtaskByID(subtask.getTaskID());
        assertEquals(listSubtask,taskManagerTest.getAllSubtasks(),"Must be [] if Subtask is empty");

        taskManagerTest.deleteEpicByID(epic.getTaskID());
        assertEquals(listEpic,taskManagerTest.getAllEpics(),"Must be [] if Epic is empty");

        taskManagerTest.deleteTaskByID(task.getTaskID());
        assertEquals(listTask,taskManagerTest.getAllEpics(),"Must be [] if Task is empty");

    }

    @Test
    public void CanRemoveTask(){
        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        Epic epic = new Epic("epic","epicDescr",TaskStatus.NEW);
        Subtask subtask = new Subtask("subtask","subtaskDecr",TaskStatus.NEW, epic);

        taskManagerTest.addNewTask(task);
        taskManagerTest.addNewEpic(epic);
        taskManagerTest.addNewSubtask(subtask,epic);

        List<Subtask> listSubtask = new ArrayList<>();
        taskManagerTest.clearAllSubtasks();
        assertEquals(listSubtask,taskManagerTest.getAllSubtasks(),"Must be [] if Subtask is empty");


        List<Epic> listEpic = new ArrayList<>();
        taskManagerTest.clearAllEpics();
        assertEquals(listEpic,taskManagerTest.getAllEpics(),"Must be [] if Epic is empty");

        List<Task> listTask = new ArrayList<>();
        taskManagerTest.clearAllTasks();
        assertEquals(listTask,taskManagerTest.getAllEpics(),"Must be [] if Task is empty");
    }

    @Test
    public void CanUpdate(){

        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        Epic epic = new Epic("epic","epicDescr",TaskStatus.NEW);
        Subtask subtask = new Subtask("subtask","subtaskDecr",TaskStatus.NEW, epic);

        task.setTaskName("newname");
        task.setTaskDescription("newdescription");
        task.setTaskStatus(TaskStatus.IN_PROCESS);
        Integer taskID = task.getTaskID();
        task.setTaskID(9);

        assertNotEquals(task.getTaskName(),"task");
        assertNotEquals(task.getTaskDescription(),"taskDescr");
        assertNotEquals(task.getTaskStatus(),TaskStatus.NEW);
        assertNotEquals(task.getTaskID(),taskID);

        epic.setTaskName("newname");
        epic.setTaskDescription("newdescription");
        epic.setTaskStatus(TaskStatus.IN_PROCESS);
        Integer epicID = task.getTaskID();
        epic.setTaskID(10);

        assertNotEquals(epic.getTaskName(),"task");
        assertNotEquals(epic.getTaskDescription(),"taskDescr");
        assertNotEquals(epic.getTaskStatus(),TaskStatus.NEW);
        assertNotEquals(epic.getTaskID(),epicID);

        subtask.setTaskName("newname");
        subtask.setTaskDescription("newdescription");
        subtask.setTaskStatus(TaskStatus.IN_PROCESS);
        Integer subtaskID = task.getTaskID();
        subtask.setTaskID(11);

        assertNotEquals("task", subtask.getTaskName());
        assertNotEquals("taskDescr",subtask.getTaskDescription());
        assertNotEquals(TaskStatus.NEW, subtask.getTaskStatus());
        assertNotEquals(subtaskID, subtask.getTaskID());
    }

    @Test
    public void TaskMustRemainConstant(){
        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        String taskName = task.getTaskName();
        String taskDescription = task.getTaskDescription();
        task.setTaskID(0);
        Integer taskId = task.getTaskID();
        TaskStatus taskStatus = task.getTaskStatus();

        taskManagerTest.addNewTask(task);

        assertEquals(task.getTaskID(),taskId);
        assertEquals(task.getTaskDescription(), taskDescription);
        assertEquals(task.getTaskStatus(), taskStatus);
        assertEquals(task.getTaskName(), taskName);
    }

    @Test
    public void CanAddTask(){

        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        Epic epic = new Epic("epic","epicDescr",TaskStatus.NEW);
        Subtask subtask = new Subtask("subtask","subtaskDecr",TaskStatus.NEW, epic);

        taskManagerTest.addNewTask(task);
        taskManagerTest.addNewEpic(epic);
        taskManagerTest.addNewSubtask(subtask,epic);

        assertEquals(task,taskManagerTest.getTaskByID(task.getTaskID()));
        assertEquals(epic,taskManagerTest.getEpicByID(epic.getTaskID()));
        assertEquals(subtask,taskManagerTest.getSubtaskByID(subtask.getTaskID()));
    }

    @Test
    public void TestTaskMustBeEndedAndReturnEndTime() {
        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        Epic epic = new Epic("epic","epicDescr",TaskStatus.NEW);
        Subtask subtask = new Subtask("subtask","subtaskDecr",TaskStatus.NEW, epic);

        taskManagerTest.addNewTask(task);
        taskManagerTest.addNewEpic(epic);
        taskManagerTest.addNewSubtask(subtask,epic);

        try {
            Thread.sleep(2000); // остановка на 2 секунды
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        taskManagerTest.updateTask("task","taskDescr",task, TaskStatus.DONE);
        taskManagerTest.updateTask("epic","epicDescr",epic, TaskStatus.DONE);
        taskManagerTest.updateTask("subtask","subtaskDecr",subtask, TaskStatus.DONE);

        assertEquals(2,taskManagerTest.getTaskByID(task.getTaskID()).getDuration().getSeconds());
        assertEquals(2,taskManagerTest.getEpicByID(epic.getTaskID()).getDuration().getSeconds());
        assertEquals(2,taskManagerTest.getSubtaskByID(subtask.getTaskID()).getDuration().getSeconds());
    }

    @Test
    public void TestMustReturnrioritizedTasksTreeSet() {
        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        Task task = new Task("task","taskDescr", TaskStatus.NEW);
        try {
            Thread.sleep(2000); // остановка на 2 секунды
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Epic epic = new Epic("epic","epicDescr",TaskStatus.NEW);
        try {
            Thread.sleep(2000); // остановка на 2 секунды
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Subtask subtask = new Subtask("subtask","subtaskDecr",TaskStatus.NEW, epic);

        taskManagerTest.addNewTask(task);
        taskManagerTest.addNewEpic(epic);
        taskManagerTest.addNewSubtask(subtask,epic);

        List<LocalDateTime> sortedList = new ArrayList<>(taskManagerTest.getPrioritizedTasks());

        for (int i = 0; i < sortedList.size() - 1; i++) {
            assertTrue(sortedList.get(i).isAfter(sortedList.get(i + 1)),
                    "Элементы должны быть отсортированы от новых к старым");
        }
    }
}