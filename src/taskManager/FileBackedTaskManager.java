package taskManager;

import task.Epic;
import task.Subtask;
import task.Task;
import taskStatus.TaskStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private  String filePath;

    public  FileBackedTaskManager(String filePath) {
        super();
        this.filePath = filePath;
        save();
    }

    public void save(){
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath)) ) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for(Task tasks : task.values()){
                writer.write(taskToLine(tasks));
                writer.newLine();
            }

            for(Epic epics : epic.values()){
                writer.write(epicToLine(epics));
                writer.newLine();
            }

            for(Subtask subtasks : subtask.values()){
                writer.write(subtaskToLine(subtasks));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    public List<String> read(){
        try (FileReader reader = new FileReader(filePath); BufferedReader br = new BufferedReader(reader)) {
            List<String> fileInfo = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                fileInfo.add(line);
            }
            br.close();
            return fileInfo;
        } catch (IOException e){
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return null;
        }
    }

    private String taskToLine(Task task){
        return String.format("%d,TASK,%s,%s,%s", task.getTaskID(), task.getTaskName(),task.getTaskStatus(), task.getTaskDescription());
    }

    private String epicToLine(Epic task){
        return String.format("%d,EPIC,%s,%s,%s", task.getTaskID(),  task.getTaskName(), task.getTaskStatus(), task.getTaskDescription());
    }

    private String subtaskToLine(Subtask task){
        return String.format("%d,SUBTASK,%s,%s,%s,%d", task.getTaskID(),  task.getTaskName(), task.getTaskStatus(), task.getTaskDescription(),task.getMasterTask().getTaskID());
    }

    @Override
    public void addNewTask(Task newTask) {
        super.addNewTask(newTask);
        save();
    }

    @Override
    public void addNewEpic(Epic newEpic) {
        super.addNewEpic(newEpic);
        save();
    }

    @Override
    public void addNewSubtask(Subtask newSubtask, Epic epic) {
        super.addNewSubtask(newSubtask, epic);
        save();
    }

    @Override
    public Task updateTask(String newTaskName, String newTaskDescription, Task task) {
        Task taskTMP = super.updateTask(newTaskName, newTaskDescription, task);
        save();
        return taskTMP;
    }

    @Override
    public Epic updateEpic(String newTaskName, String newTaskDescription, Epic epic) {
        Epic epicTMP = super.updateEpic(newTaskName, newTaskDescription, epic);
        save();
        return epicTMP;
    }

    @Override
    public Subtask updateSubtask(String newTaskName, String newTaskDescription, Subtask subtask) {
        Subtask subtaskTMP = super.updateSubtask(newTaskName, newTaskDescription, subtask);
        save();
        return subtaskTMP;
    }
}
