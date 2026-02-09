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
import task.*;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskHandlerTest {
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
    public void testCanDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName","epicDescr",TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("name1","description1", TaskStatus.NEW, epic);
        inMemoryTaskManager.addNewSubtask(subtask,epic);
        int id = subtask.getTaskID();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                DELETE().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> tasks = inMemoryTaskManager.getAllSubtasks();
        assertEquals(0,tasks.size(),"Задачи не удалены");
    }

    @Test
    public void testCanAddSubtask()throws IOException, InterruptedException  {
        Epic epic = new Epic("epicName","epicDescr",TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("name","description", TaskStatus.NEW, epic);
        String taskToJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                POST(HttpRequest.BodyPublishers.ofString(taskToJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Subtask subtask1 = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.getTaskName(),subtask1.getTaskName());
        assertEquals(subtask.getTaskDescription(),subtask1.getTaskDescription());
        assertEquals(subtask.getTaskID(),subtask1.getTaskID());
        assertEquals(subtask.getDuration(),subtask1.getDuration());
        assertEquals(subtask.getStartTime(),subtask1.getStartTime());
        assertEquals(subtask.getTaskID(),subtask1.getTaskID());
    }

    @Test
    public void testCanGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName","epicDescr",TaskStatus.NEW);
        Epic epic2 = new Epic("epicName2","epicDescr2",TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewEpic(epic2);
        Subtask subtask1 = new Subtask("name1","description1", TaskStatus.NEW, epic);
        Subtask subtask2 = new Subtask("name2","description2", TaskStatus.NEW, epic2);
        inMemoryTaskManager.addNewSubtask(subtask1, epic);
        inMemoryTaskManager.addNewSubtask(subtask2, epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> tasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {}.getType());
        assertEquals(2,tasks.size(),"Количество задач не совпадает");
    }

    @Test
    public void testCanGetSubtaskByID() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName","epicDescr",TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("name1","description1", TaskStatus.NEW, epic);
        inMemoryTaskManager.addNewSubtask(subtask , epic);;
        int id = subtask.getTaskID();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask subtasks = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.getTaskName(),subtasks.getTaskName());
        assertEquals(subtask.getTaskDescription(),subtasks.getTaskDescription());
        assertEquals(subtask.getTaskID(),subtasks.getTaskID());
        assertEquals(subtask.getDuration(),subtasks.getDuration());
        assertEquals(subtask.getStartTime(),subtasks.getStartTime());
        assertEquals(subtask.getTaskID(),subtasks.getTaskID());
    }

    @Test
    public void testCanUdpadeSubtask() throws IOException,InterruptedException {
        Epic epic = new Epic("epicName","epicDescr",TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("name1","description1", TaskStatus.NEW, epic);
        inMemoryTaskManager.addNewSubtask(subtask, epic);
        int id = subtask.getTaskID();
        subtask.setTaskName("newName");
        String taskToJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                POST(HttpRequest.BodyPublishers.ofString(taskToJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Subtask updatedTask = gson.fromJson(response.body(), Subtask.class);
        assertEquals("newName", updatedTask.getTaskName(), "Задача не обновлена");
    }
}
