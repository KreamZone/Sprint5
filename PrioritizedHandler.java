package taskHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson;
    private final TaskManager taskManager;

    public PrioritizedHandler(Gson gson, TaskManager taskManager) {
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
            if (Pattern.matches("^/prioritized$", path)) {
                TreeSet<LocalDateTime> taskPrioritazed = taskManager.getPrioritizedTasks();
                sendText(exchange, gson.toJson(taskPrioritazed), 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (IOException exception) {
            throw exception;
        } catch (NullPointerException  nullPointerException){
            sendBadResponse(exchange,"NullPointerException.",400);
        } catch (Exception exception){
            sendBadResponse(exchange, "Internal server error", 500);
        }
    }
}
