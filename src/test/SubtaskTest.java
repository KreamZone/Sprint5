package test;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import taskStatus.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SubtaskTest {

    @Test
    public void EpicEuqalById(){
        Epic masterTask = new Epic("masterTask","masterTaskDesc",TaskStatus.NEW);
        Subtask subtask1 = new Subtask("subtask1","desc1", TaskStatus.NEW,masterTask);
        Subtask subtask2 = new Subtask("subtask2","desc2", TaskStatus.NEW,masterTask);

        subtask1.setTaskID(2);
        subtask2.setTaskID(2);

        assertTrue(subtask1.getTaskID() == subtask2.getTaskID()," must be true if ID is Equal ");
    }

}