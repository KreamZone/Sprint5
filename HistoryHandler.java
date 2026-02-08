package taskHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;
    private final TaskManager taskManager;

    public HistoryHandler(Gson gson, TaskManager taskManager) {
        super(gson, taskManager);
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            handleGET(path, exchange);
        } else {
            sendBadResponse(exchange, "Unknown request", 400);
        }
    }

    private void handleGET(String path ,HttpExchange exchange) throws IOException {
        try {
            if (Pattern.matches("^/history$", path)) {
                List<Task> taskHistory = taskManager.getHistory();
                sendText(exchange, gson.toJson(taskHistory), 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (IOException exception) {
            throw exception;
        } catch (NullPointerException  nullPointerException){
            sendBadResponse(exchange,"NullPointerException.",400);
        } catch (Exception exception){
            sendBadResponse(exchange,"Internal server error", 500);
        }
    }
}
