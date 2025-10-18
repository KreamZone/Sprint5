package test;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import taskManager.InMemoryTaskManager;
import taskStatus.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubtaskTest {

    @Test
    public void CantSubtaskWithSameID(){
        Epic epic = new Epic("epic1","epicDescr1",TaskStatus.NEW);
        Subtask subtask1 = new Subtask("subtask1","subtaskDescr1",TaskStatus.NEW,epic);
        Subtask subtask2 = new Subtask("subtask2","subtaskDescr2",TaskStatus.NEW,epic);

        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        taskManagerTest.addNewSubtask(subtask1,epic);
        taskManagerTest.addNewSubtask(subtask2,epic);

        assertFalse(taskManagerTest.setTaskId(subtask1,subtask2.getTaskID()));
        assertFalse(taskManagerTest.setTaskId(subtask2,subtask1.getTaskID()));
    }

}