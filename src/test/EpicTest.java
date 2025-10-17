package test;

import org.junit.jupiter.api.Test;
import task.Epic;
import taskStatus.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {

    @Test
    public void EpicEuqalById(){
        Epic epic1 = new Epic("epic1","desc1", TaskStatus.NEW);
        Epic epic2 = new Epic("epic2","desc2", TaskStatus.NEW);

        epic1.setTaskID(1);
        epic2.setTaskID(1);

        assertEquals(epic1.getTaskID(), epic2.getTaskID()," must be true if ID is Equal ");
    }

}