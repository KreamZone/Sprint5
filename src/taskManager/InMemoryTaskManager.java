package taskManager;

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
    public boolean setTaskId(Task taskToUpdate, int newId) {
        if (isIdExists(newId)) {
            return false;
        }

        Integer oldId = taskToUpdate.getTaskID();

        taskToUpdate.setTaskID(newId);

        if (taskToUpdate instanceof Subtask && subtask.containsKey(oldId)) {
            Subtask subtaskObj = subtask.remove(oldId);
            subtask.put(newId, subtaskObj);
        } else if (taskToUpdate instanceof Epic && epic.containsKey(oldId)) {
            Epic epicObj = epic.remove(oldId);
            epic.put(newId, epicObj);
        } else if (task.containsKey(oldId)) {
            Task taskObj = task.remove(oldId);
            task.put(newId, taskObj);
        }

        if (newId >= this.ID) {
            this.ID = newId + 1;
        }

        return true;
    }

    private boolean isIdExists(int id) {
        return task.containsKey(id) || subtask.containsKey(id) || epic.containsKey(id);
    }

    public int getCurrentId() {
        return this.ID;
    }

    public void setCurrentId(int newId) {
        if (newId > this.ID) {
            this.ID = newId;
        }
    }

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
        Task task = this.task.get(id);
        if(task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtaskByID(int id){
        Subtask subtask = this.subtask.get(id);
        if(task != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpicByID(int id){
        Epic epic = this.epic.get(id);
        if(task != null) {
            historyManager.add(epic);
        }
        return epic;
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
