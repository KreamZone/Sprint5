package interfaces;

import task.Epic;
import task.Subtask;
import task.Task;
import taskStatus.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
     List<Subtask> getAllSubtasks ();

     List<Task> getAllTasks();

     List<Epic> getAllEpics();

     void addNewTask(Task newTask);

     void addNewSubtask(Subtask newSubtask, Epic epic);

     void addNewEpic(Epic newEpic);

     Task updateTask(String newTaskName, String newTaskDescription, Task task, TaskStatus taskStatus);

     Epic updateEpic(String newTaskName, String newTaskDescription, Epic epic, TaskStatus taskStatus);

     Task updateSubtask(String newTaskName, String newTaskDescription, Subtask subtask, TaskStatus taskStatus);

     Task getTaskByID(int id);

     Subtask getSubtaskByID(int id);

     Epic getEpicByID(int id);

     Subtask deleteSubtaskByID(int id);

     void deleteEpicByID(int id);

     void deleteTaskByID(int id);

     List<Subtask> getSubtaskByEpic (Epic epic);

     void clearAllSubtasks();

     void clearAllTasks();

     void clearAllEpics();

     List<Task> getHistory();

     boolean setTaskId(Task task, int newId);

     LocalDateTime getEndTime(Task task);

     TreeSet getPrioritizedTasks();

    <T extends Task> boolean isTimeOverlap(T task1, T task2);
}
