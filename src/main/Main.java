package main;

import taskManager.InMemoryTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import taskStatus.TaskStatus;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Переезд", "Что необходимо взять", TaskStatus.NEW);
        //Epic newEpic1 = new Epic("Переехали", "Что то взяли", TaskStatus.IN_PROCESS);
        Epic epic2 = new Epic("Жилье","Нужно найти квартиру",TaskStatus.NEW);
        Task task1 = new Task("Какой то taskName", "Какое то taskDescriptions", TaskStatus.NEW);
        //Task newTask1 = new Task("обновленный таск","обновленнное taskDescriptions", TaskStatus.IN_PROCESS);
        Subtask subtask1 = new Subtask("Одежда","Нужно взять всю одежду",TaskStatus.NEW, epic1);
        Subtask subtask2 = new Subtask("Инструменты","Нужно взять все инструменты",TaskStatus.NEW, epic1);
        Subtask subtask3 = new Subtask("Ул. Тверская 9","Нужно позвонить по номеру",TaskStatus.NEW,epic2);
        Subtask subtask4 = new Subtask("Ул. gergg 9","Нужно dbf по номеру",TaskStatus.NEW,epic2);
        //Subtask newSubtask1 = new Subtask("Не одежда", "не Взяли одежду", TaskStatus.IN_PROCESS,epic1);

        taskManager.addNewSubtask(subtask1, epic1);
        taskManager.addNewSubtask(subtask2, epic1);
        taskManager.addNewSubtask(subtask3, epic2);

        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        taskManager.addNewTask(task1);

        System.out.println("Все Epic.taskName: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskName());
        }
        System.out.println("Все Epic.taskDescriptions: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskDescription());
        }
        System.out.println("Все Epic.taskID: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskID());
        }
        System.out.println("Все Epic.taskStatus: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskStatus());
        }

        System.out.println("Все Subtask.taskName: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskName());
        }
        System.out.println("Все Subtask.taskDescription: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskDescription());
        }
        System.out.println("Все Subtask.taskID: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskID());
        }
        System.out.println("Все Subtask.taskStatus: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskStatus());
        }

        System.out.println("Все Task.taskName: ");
        for (Task task : taskManager.getAllTasks()){
            System.out.println(task.getTaskName());
        }
        System.out.println("Все Task.taskDescription: ");
        for (Task task : taskManager.getAllTasks()){
            System.out.println(task.getTaskDescription());
        }
        System.out.println("Все Task.taskID: ");
        for (Task task : taskManager.getAllTasks()){
            System.out.println(task.getTaskID());
        }
        System.out.println("Все Task.taskStatus: ");
        for (Task task : taskManager.getAllTasks()){
            System.out.println(task.getTaskStatus());
        }

        taskManager.updateEpic("Новое назвние эпика 1","Новое описание эпика 1",epic1);
        taskManager.updateSubtask("Новое назвние саабтаска 1","Новое описание сабтаска 1",subtask1);

        System.out.println("Все Epic.taskName: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskName());
        }
        System.out.println("Все Epic.taskDescriptions: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskDescription());
        }
        System.out.println("Все Epic.taskID: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskID());
        }
        System.out.println("Все Epic.taskStatus: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskStatus());
        }

        System.out.println("Все Subtask.taskName: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskName());
        }
        System.out.println("Все Subtask.taskDescription: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskDescription());
        }
        System.out.println("Все Subtask.taskID: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskID());
        }
        System.out.println("Все Subtask.taskStatus: ");
        for (Subtask subtask : taskManager.getAllSubtasks()){
            System.out.println(subtask.getTaskStatus());
        }

        System.out.println(taskManager.getSubtaskByID(subtask1.getTaskID()));
        System.out.println(subtask1);

        System.out.println(taskManager.getEpicByID(epic1.getTaskID()));
        System.out.println(epic1);

        //System.out.println(taskManager.deleteEpicByID(epic1.getTaskID()));
        taskManager.deleteEpicByID(epic1.getTaskID());
        System.out.println(taskManager.epic.get(epic1.getTaskID()));

        taskManager.clearAllEpics();
        System.out.println("Все Epic.taskName: ");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getTaskName());
        }

    }
}