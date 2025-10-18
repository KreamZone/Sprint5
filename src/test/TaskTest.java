package test;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import taskManager.InMemoryTaskManager;
import taskStatus.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TaskTest {

    @Test
    public void CantTaskWithSameID(){
        Task task1 = new Task("task1","taskDescr1",TaskStatus.NEW);
        Task task2 = new Task("task2","taskDescr2",TaskStatus.NEW);

        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        taskManagerTest.addNewTask(task1);
        taskManagerTest.addNewTask(task2);

        assertFalse(taskManagerTest.setTaskId(task1,task2.getTaskID()));
        assertFalse(taskManagerTest.setTaskId(task2,task1.getTaskID()));
    }

}