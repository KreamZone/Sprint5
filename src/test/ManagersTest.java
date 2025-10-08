package test;

import Manager.Managers;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    public void ManagersGetDefault(){
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    public void ManagersHistoryGetDefault(){
        HistoryManager taskManager = Managers.getDefaultHistory();
        assertNotNull(taskManager);
    }
}