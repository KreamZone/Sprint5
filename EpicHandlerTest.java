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

public class EpicHandlerTest {
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
            .registerTypeAdapter(Epic.class, new Deserializator())
            .registerTypeAdapter(Epic.class, new Serialozator())
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
    public void testCanDeleteEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name1","description1", TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        int id = epic1.getTaskID();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                DELETE().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        assertEquals(0,epics.size(),"Задачи не удалены");
    }

    @Test
    public void testCanAddEpic()throws IOException, InterruptedException  {
        Epic epic = new Epic("name","description", TaskStatus.NEW);
        String taskToJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                POST(HttpRequest.BodyPublishers.ofString(taskToJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //assertEquals(201, response.statusCode());

        Epic epic1 = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic.getTaskName(),epic1.getTaskName());
        assertEquals(epic.getTaskDescription(),epic1.getTaskDescription());
        assertEquals(epic.getTaskID(),epic1.getTaskID());
        assertEquals(epic.getDuration(),epic1.getDuration());
        assertEquals(epic.getStartTime(),epic1.getStartTime());
        assertEquals(epic.getTaskID(),epic1.getTaskID());
    }

    @Test
    public void testCanGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name1","description1", TaskStatus.NEW);
        Epic epic2 = new Epic("name2","description2", TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        assertEquals(2,epics.size(),"Количество задач не совпадает");
    }

    @Test
    public void testCanGetEpicByID() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name1","description1", TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        int id = epic1.getTaskID();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic epic = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic.getTaskName(),epic1.getTaskName());
        assertEquals(epic.getTaskDescription(),epic1.getTaskDescription());
        assertEquals(epic.getTaskID(),epic1.getTaskID());
        assertEquals(epic.getDuration(),epic1.getDuration());
        assertEquals(epic.getStartTime(),epic1.getStartTime());
        assertEquals(epic.getTaskID(),epic1.getTaskID());
    }

    @Test
    public void testCanUdpadeEpic() throws IOException,InterruptedException {
        Epic epic1 = new Epic("name1","description1", TaskStatus.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        int id = epic1.getTaskID();
        epic1.setTaskName("newName");
        String epicToJson = gson.toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                POST(HttpRequest.BodyPublishers.ofString(epicToJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Epic updatedEpic = inMemoryTaskManager.getEpicByID(id);
        assertEquals("newName", updatedEpic.getTaskName(), "Задача не обновлена");
    }

}
