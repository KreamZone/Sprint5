package test;

import taskManager.InMemoryHistoryManager;
import interfaces.HistoryManager;
import org.junit.jupiter.api.Test;
import task.Task;
import taskStatus.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    public void HistoryManagerAddAndSafeTest(){
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("task1","descr1", TaskStatus.NEW);
        historyManager.add(task1);
        List<Task> historyList = historyManager.getHistory();

        assertEquals(historyList.size(),1);

        Task updatedTask1 = new Task(task1.getTaskName(),task1.getTaskDescription(),task1.getTaskStatus());
        updatedTask1.setTaskDescription("descr2");
        updatedTask1.setTaskName("task1.2");
        updatedTask1.setTaskStatus(TaskStatus.IN_PROCESS);
        historyManager.add(updatedTask1);

        assertEquals("descr1",historyManager.getHistory().getFirst().getTaskDescription());
        assertEquals("task1",historyManager.getHistory().getFirst().getTaskName());
        assertEquals(TaskStatus.NEW,historyManager.getHistory().getFirst().getTaskStatus());
    }
}