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
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class PrioritazedHandlerTest {

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
    public  void testCanGetPrioritazedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("name1","decr1",TaskStatus.NEW);
        Task task2 = new Task("name2","decr2",TaskStatus.NEW);
        LocalDateTime time1 = LocalDateTime.of(2024,11,1,1,0,0);
        LocalDateTime time2 = LocalDateTime.of(2025,11,1,1,0,0);
        task1.setStartTime(time1);
        task2.setStartTime(time2);
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewTask(task2);
        System.out.println(inMemoryTaskManager.getPrioritizedTasks());
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().
                uri(uri).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        TreeSet<LocalDateTime> tasks = gson.fromJson(
                response.body(),
                new TypeToken<TreeSet<LocalDateTime>>(){}.getType()
        );
        assertEquals(2,tasks.size(),"Количество задач не совпадает.");
        assertEquals(time1,tasks.getFirst(),"Порядок задач не совпадает");
        assertEquals(time2,tasks.getLast(),"Порядок задач не совпадает");
    }
}
