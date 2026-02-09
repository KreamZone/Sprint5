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
import taskManager.InMemoryHistoryManager;
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

public class HistoryHandlerTest {

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
    public void testCanGetHistory() throws IOException,InterruptedException {
        Task task = new Task("name", "description",TaskStatus.NEW);
        inMemoryTaskManager.addNewTask(task);
        Task gettedTask = inMemoryTaskManager.getTaskByID(task.getTaskID());
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertEquals(1,tasks.size(),"Количество задач в истории не совпадает");

    }
}
