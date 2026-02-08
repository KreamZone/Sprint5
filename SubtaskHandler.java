package taskHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;
    private final TaskManager taskManager;

    public SubtaskHandler(Gson gson, TaskManager taskManager){
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
        if(Pattern.matches("^/subtasks/\\d+$", path)){
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                Subtask subTask = taskManager.getSubtaskByID(id);
                if (subTask != null) {
                    sendText(exchange, gson.toJson(subTask), 200);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException n) {
                sendBadResponse(exchange, "Invalid ID format. ID must be a number.", 400);
            }
        } else if (Pattern.matches("^/subtasks$", path)) {
            try {
                List<Subtask> taskList = taskManager.getAllSubtasks();
                sendText(exchange, gson.toJson(taskList), 200);
            } catch (Exception e){
                sendBadResponse(exchange, "Error receiving subtasks: " + e.getMessage(), 400);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    public void handlePOST(String path, HttpExchange exchange) throws IOException {
        if(Pattern.matches("^/subtasks/\\d+$", path)){
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subTask = gson.fromJson(body, Subtask.class);
                if (subTask != null) {
                    taskManager.updateTask(subTask.getTaskName(),
                            subTask.getTaskDescription(),
                            subTask,
                            subTask.getTaskStatus());
                    sendText(exchange, gson.toJson(subTask), 201);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException n) {
                sendBadResponse(exchange, "Invalid ID format. ID must be a number.", 400);
            }
        } else if (Pattern.matches("^/subtasks$", path)) {
            try{
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subTask = gson.fromJson(body, Subtask.class);
                if (subTask != null) {
                    taskManager.addNewTask(subTask);
                    sendText(exchange, gson.toJson(subTask), 201);
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
        if (Pattern.matches("^/subtasks/\\d+$", path)) {
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
