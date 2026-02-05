package taskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import task.*;
import interfaces.TaskManager;
import taskStatus.TaskStatus;



public class InMemoryTaskManager implements TaskManager {
    private Integer ID = 0;
    public Map<Integer, Subtask> subtask = new HashMap<>();
    public static Map<Integer, Task> task = new HashMap<>();
    public Map<Integer , Epic> epic = new HashMap<>();
    public  InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private TreeSet<LocalDateTime> prioritizedTasks = new TreeSet<>(Comparator.reverseOrder());

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
        return new ArrayList<>(subtask.values());
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
        if(newTask.getStartTime()!= null){
            prioritizedTasks.add(newTask.getStartTime());
        }
        ID++;
    }

    @Override
    public void addNewSubtask(Subtask newSubtask, Epic epic) {
        newSubtask.setTaskID(ID);
        subtask.put(newSubtask.getTaskID(), newSubtask);
        if(newSubtask.getStartTime()!= null){
            prioritizedTasks.add(newSubtask.getStartTime());
        }
        ID++;
        epic.addNewSubtask(newSubtask);
    }

    @Override
    public void addNewEpic(Epic newEpic) {
        newEpic.setTaskID(ID);
        epic.put(newEpic.getTaskID(), newEpic);
        if(newEpic.getStartTime()!= null){
            prioritizedTasks.add(newEpic.getStartTime());
        }
        ID++;
    }

    @Override
    public Task updateTask(String newTaskName, String newTaskDescription, Task task, TaskStatus taskStatus){
        task.setTaskName(newTaskName);
        task.setTaskDescription(newTaskDescription);
        task.setTaskStatus(taskStatus);
        if(taskStatus == TaskStatus.DONE){
            task.setDuration(Duration.between(task.getStartTime(), LocalDateTime.now()));
        }
        return task;
    }

    @Override
    public Epic updateEpic(String newTaskName, String newTaskDescription, Epic epic, TaskStatus taskStatus){
        epic.setTaskName(newTaskName);
        epic.setTaskDescription(newTaskDescription);
        epic.setTaskStatus(taskStatus);
        if(epic.getSubtasks().stream().allMatch(subtask -> subtask.getTaskStatus() == TaskStatus.DONE)){
            long totalDuraption = epic.getSubtasks().stream().mapToLong(subtask -> subtask.getDuration().getSeconds()).sum();
            epic.setDuration(Duration.ofSeconds(totalDuraption));
        }
        return epic;
    }

    @Override
    public Subtask updateSubtask(String newTaskName, String newTaskDescription, Subtask subtask, TaskStatus taskStatus){
        subtask.setTaskName(newTaskName);
        subtask.setTaskDescription(newTaskDescription);
        subtask.setTaskStatus(taskStatus);
        if(taskStatus == TaskStatus.DONE){
            subtask.setDuration(Duration.between(subtask.getStartTime(), LocalDateTime.now()));
        }
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
        epic.values().stream()
                .forEach(Epic::removeAllSubtasks);
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

    @Override
    public LocalDateTime getEndTime(Task task){
        try {
            return task.getStartTime().plusSeconds(task.getDuration().toSeconds());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreeSet<LocalDateTime> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public <T extends Task> boolean isTimeOverlap(T task1, T task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());

        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());

        return start1.isBefore(end2) && start2.isBefore(end1);
    }


}

