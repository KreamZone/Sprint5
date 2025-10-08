package Manager;

import java.util.*;

import task.*;
import interfaces.TaskManager;
import taskStatus.TaskStatus;

public class InMemoryTaskManager implements TaskManager {
    private Integer ID = 0;
    public Map<Integer, Subtask> subtask = new HashMap<>();
    public Map<Integer, Task> task = new HashMap<>();
    public Map<Integer , Epic> epic = new HashMap<>();
    public  InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public List<Subtask> getAllSubtasks () {
        List<Subtask> subtasks = new ArrayList<>();
        for(Subtask newSubtask : subtask.values()){
            subtasks.add(newSubtask);
        }
        return  subtasks;
    }

    @Override
    public List<Task> getAllTasks () {
        return new ArrayList<>(task.values());
    }

    @Override
    public List<Epic> getAllEpics () {
        return new ArrayList<>(epic.values());
    }

    @Override
    public void addNewTask(Task newTask){
        newTask.setTaskID(ID);
        task.put(newTask.getTaskID(), newTask);
        ID++;
    }

    @Override
    public void addNewSubtask(Subtask newSubtask, Epic epic) {
        newSubtask.setTaskID(ID);
        subtask.put(newSubtask.getTaskID(), newSubtask);
        ID++;
        epic.addNewSubtask(newSubtask);
    }

    @Override
    public void addNewEpic(Epic newEpic) {
        newEpic.setTaskID(ID);
        epic.put(newEpic.getTaskID(), newEpic);
        ID++;
    }

    @Override
    public Task updateTask(String newTaskName, String newTaskDescription, Task task){
        task.setTaskName(newTaskName);
        task.setTaskDescription(newTaskDescription);
        task.setTaskStatus(TaskStatus.IN_PROCESS);
        return task;
    }

    @Override
    public Epic updateEpic(String newTaskName, String newTaskDescription, Epic epic){
        epic.setTaskName(newTaskName);
        epic.setTaskDescription(newTaskDescription);
        epic.setTaskStatus(TaskStatus.IN_PROCESS);
        return epic;
    }

    @Override
    public Task updateSubtask(String newTaskName, String newTaskDescription, Subtask subtask){
        subtask.setTaskName(newTaskName);
        subtask.setTaskDescription(newTaskDescription);
        subtask.setTaskStatus(TaskStatus.IN_PROCESS);
        return subtask;
    }

    @Override
    public Task getTaskByID(int id){
        return task.get(id);
    }

    @Override
    public Subtask getSubtaskByID(int id){
        return subtask.get(id);
    }

    @Override
    public Epic getEpicByID(int id){
        return epic.get(id);
    }


    @Override
    public Subtask deleteSubtaskByID(int id){
        Subtask newSubtask = subtask.remove(id);
        newSubtask.getMasterTask().removeSubtask(newSubtask);
        return newSubtask;
    }

    @Override
    public void deleteEpicByID(int id) {
        epic.remove(getEpicByID(id).getTaskID());
    }

    @Override
    public void deleteTaskByID(int id) {
        task.remove(getTaskByID(id).getTaskID());
    }

    @Override
    public List<Subtask> getSubtaskByEpic (Epic epic){
        return epic.getSubtasks();
    }

    @Override
    public void clearAllSubtasks(){
        subtask.clear();
        for (Epic newEpic : epic.values()){
            newEpic.removeAllSubtasks();
        }
    }

    @Override
    public void clearAllTasks(){
        task.clear();
    }

    @Override
    public void clearAllEpics() {
        clearAllSubtasks();
        epic.clear();
    }
}
