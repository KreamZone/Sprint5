package test;

import org.junit.jupiter.api.Test;
import task.Epic;
import taskManager.InMemoryTaskManager;
import taskStatus.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void CantEpicWithSameID(){
        Epic epic1 = new Epic("epic1","epicDescr1",TaskStatus.NEW);
        Epic epic2 = new Epic("epic2","epicDescr2",TaskStatus.NEW);

        InMemoryTaskManager taskManagerTest = new InMemoryTaskManager();
        taskManagerTest.addNewEpic(epic1);
        taskManagerTest.addNewEpic(epic2);

        assertFalse(taskManagerTest.setTaskId(epic1,epic2.getTaskID()));
        assertFalse(taskManagerTest.setTaskId(epic2,epic1.getTaskID()));
    }

}