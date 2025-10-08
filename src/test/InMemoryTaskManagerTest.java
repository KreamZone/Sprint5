package test;

import Manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import taskStatus.TaskStatus;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryTaskManagerTest {

    @Test
    public void EpicCanNotBeSubtask(){


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


}