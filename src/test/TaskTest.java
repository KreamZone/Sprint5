package test;

import org.junit.jupiter.api.Test;
import task.Task;
import taskStatus.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    @Test
    public void TaskEuqalById(){
        Task task1 = new Task("task1","desc1", TaskStatus.NEW);
        Task task2 = new Task("task2","desc2", TaskStatus.NEW);

        task1.setTaskID(0);
        task2.setTaskID(0);

        assertTrue(task1.getTaskID() == task2.getTaskID()," must be true if ID is Equal ");
    }

}