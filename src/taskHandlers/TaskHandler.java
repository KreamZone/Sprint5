package taskHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler  extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;
    private final TaskManager taskManager;

    public TaskHandler (Gson gson, TaskManager taskManager){
        super(gson, taskManager);
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        switch(method){
            case "GET":
                handleGET(path, exchange);
                break;
            case "POST":
                handlePOST(path, exchange);
                break;
            case "DELETE":
                handleDELETE(path, exchange);
                break;
            default:
                sendBadResponse(exchange, "Unknown request",400);
        }
    }

    public void handleGET(String path, HttpExchange exchange) throws IOException {
        if(Pattern.matches("^/tasks/\\d+$", path)){
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                Task task = taskManager.getTaskByID(id);
                if (task != null) {
                    sendText(exchange, gson.toJson(task), 200);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException n) {
                sendBadResponse(exchange, "Invalid ID format. ID must be a number.", 400);
            }
        } else if (Pattern.matches("^/tasks$", path)) {
            try {
                List<Task> taskList = taskManager.getAllTasks();
                sendText(exchange, gson.toJson(taskList), 200);
            } catch (Exception e){
                sendBadResponse(exchange, "Error receiving tasks: " + e.getMessage(), 400);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    public void handlePOST(String path, HttpExchange exchange) throws IOException {
        if(Pattern.matches("^/tasks/\\d+$", path)){
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);
                if (task != null) {
                    taskManager.updateTask(task.getTaskName(),
                            task.getTaskDescription(),
                            task,
                            task.getTaskStatus());
                    sendText(exchange, gson.toJson(task), 201);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException n) {
                sendBadResponse(exchange, "Invalid ID format. ID must be a number.", 400);
            }
        } else if (Pattern.matches("^/tasks$", path)) {
            try{
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Получен JSON: " + body);

                // Используем JsonParser для отладки
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(body);

                System.out.println("Парсинг JSON: " + element.toString());
                Task task = gson.fromJson(body, Task.class);
                if (task != null) {

                    System.out.println("Задача создана: " + task.getTaskName());

                    // ВАЖНО: Проверяем что duration и startTime не null
                    if (task.getDuration() == null) {
                        task.setDuration(Duration.ZERO);
                    }
                    if (task.getStartTime() == null) {
                        task.setStartTime(LocalDateTime.now());
                    }

                    taskManager.addNewTask(task);
                    sendText(exchange, gson.toJson(task), 201);
                } else {
                    sendNotFound(exchange);
                }
            } catch (Exception exception){
                sendBadResponse(exchange, "Unknown error.",400);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    public void handleDELETE(String path, HttpExchange exchange) throws IOException{
        if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                taskManager.deleteTaskByID(id);
                exchange.sendResponseHeaders(200, 0);
            } catch (NumberFormatException n) {
                sendBadResponse(exchange, "Invalid ID format. ID must be a number.", 400);
            } catch (Exception exception) {
                sendBadResponse(exchange, "Error in delete process.", 400);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}
