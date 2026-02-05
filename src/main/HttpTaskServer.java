package main;


import adapter.DuraptionAdapter;
import adapter.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import serializators.Deserializator;
import serializators.Serialozator;
import task.Task;
import taskHandlers.*;
import taskManager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final int port;
    private final String taskPath;
    private final String subtaskPath;
    private final String epicPath;
    private final String historyPath;
    private final String prioritizedPath;

    private final Gson gson;
    private final TaskManager taskManager;
    private HttpServer server;

    public HttpTaskServer(int port, String taskPath, String subtaskPath, String epicPath, String historyPath, String prioritizedPath, TaskManager taskManager) {
        this.port = port;
        this.taskPath = taskPath;
        this.subtaskPath = subtaskPath;
        this.epicPath = epicPath;
        this.historyPath = historyPath;
        this.prioritizedPath = prioritizedPath;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(Duration.class, new DuraptionAdapter())
                .registerTypeAdapter(Task.class, new Deserializator())
                .registerTypeAdapter(Task.class, new Serialozator())
                .create();
        this.taskManager = taskManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(taskPath, new TaskHandler (gson, taskManager));
        server.createContext(subtaskPath, new SubtaskHandler(gson, taskManager));
        server.createContext(epicPath, new EpicHandler(gson, taskManager));
        server.createContext(historyPath, new HistoryHandler(gson, taskManager));
        server.createContext(prioritizedPath, new PrioritizedHandler(gson, taskManager));
        server.start();
    }

    public void stop() {
        if(server != null){
            server.stop(0);
        }
    }

    public static void main(String[] args) throws Exception {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(
                8080,
                "/tasks",
                "/subtasks",
                "/epics",
                "/history",
                "/prioritized",
                taskManager
        );
        server.start();
    }
}
