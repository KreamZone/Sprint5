package handlersTests;

import adapter.DuraptionAdapter;
import adapter.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serializators.Deserializator;
import serializators.Serialozator;
import task.Task;
import taskManager.InMemoryTaskManager;
import taskStatus.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(8080,
            "/tasks",
            "/subtasks",
            "/epics",
            "/history",
            "/prioritized",
            inMemoryTaskManager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(Duration.class, new DuraptionAdapter())
            .registerTypeAdapter(Task.class, new Deserializator())
            .registerTypeAdapter(Task.class, new Serialozator())
            .create();

    @BeforeEach
    public void startServer() throws IOException {
        inMemoryTaskManager.clearAllSubtasks();
        inMemoryTaskManager.clearAllEpics();
        inMemoryTaskManager.clearAllTasks();
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() throws IOException {
        httpTaskServer.stop();
    }

    @Test
    public void testCanDeleteTask() throws IOException, InterruptedException {
        Task task1 = new Task("name1","description1", TaskStatus.NEW);
        inMemoryTaskManager.addNewTask(task1);
        int id = task1.getTaskID();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                DELETE().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertEquals(0,tasks.size(),"Задачи не удалены");
    }

    @Test
    public void testCanAddTask()throws IOException, InterruptedException  {
        Task task = new Task("name","description", TaskStatus.NEW);
        String taskToJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                POST(HttpRequest.BodyPublishers.ofString(taskToJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //assertEquals(201, response.statusCode());

        Task task1 = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getTaskName(),task1.getTaskName());
        assertEquals(task.getTaskDescription(),task1.getTaskDescription());
        assertEquals(task.getTaskID(),task1.getTaskID());
        assertEquals(task.getDuration(),task1.getDuration());
        assertEquals(task.getStartTime(),task1.getStartTime());
        assertEquals(task.getTaskID(),task1.getTaskID());
    }

    @Test
    public void testCanGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("name1","description1", TaskStatus.NEW);
        Task task2 = new Task("name2","description2", TaskStatus.NEW);
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertEquals(2,tasks.size(),"Количество задач не совпадает");
    }

    @Test
    public void testCanGetTaskByID() throws IOException, InterruptedException {
        Task task1 = new Task("name1","description1", TaskStatus.NEW);
        inMemoryTaskManager.addNewTask(task1);
        int id = task1.getTaskID();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getTaskName(),task1.getTaskName());
        assertEquals(task.getTaskDescription(),task1.getTaskDescription());
        assertEquals(task.getTaskID(),task1.getTaskID());
        assertEquals(task.getDuration(),task1.getDuration());
        assertEquals(task.getStartTime(),task1.getStartTime());
        assertEquals(task.getTaskID(),task1.getTaskID());
    }

    @Test
    public void testCanUdpadeTask() throws IOException,InterruptedException {
        Task task1 = new Task("name1","description1", TaskStatus.NEW);
        inMemoryTaskManager.addNewTask(task1);
        int id = task1.getTaskID();
        task1.setTaskName("newName");
        String taskToJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                POST(HttpRequest.BodyPublishers.ofString(taskToJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task updatedTask = inMemoryTaskManager.getTaskByID(id);
        assertEquals("newName", updatedTask.getTaskName(), "Задача не обновлена");
    }

}
