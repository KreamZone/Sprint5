package interfaces;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    public List<Subtask> getAllSubtasks ();

    public List<Task> getAllTasks();

    public List<Epic> getAllEpics();

    public void addNewTask(Task newTask);

    public void addNewSubtask(Subtask newSubtask, Epic epic);

    public void addNewEpic(Epic newEpic);

    public Task updateTask(String newTaskName, String newTaskDescription, Task task);

    public Epic updateEpic(String newTaskName, String newTaskDescription, Epic epic);

    public Task updateSubtask(String newTaskName, String newTaskDescription, Subtask subtask);

    public Task getTaskByID(int id);

    public Subtask getSubtaskByID(int id);

    public Epic getEpicByID(int id);

    public Subtask deleteSubtaskByID(int id);

    public void deleteEpicByID(int id);

    public void deleteTaskByID(int id);

    public List<Subtask> getSubtaskByEpic (Epic epic);

    public void clearAllSubtasks();

    public void clearAllTasks();

    public void clearAllEpics();

    public List<Task> getHistory();
}
