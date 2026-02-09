package taskHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;
    public BaseHttpHandler(Gson gson, TaskManager taskManager){
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        String response = "{\"error\": \"Not Found\"}";
        sendText(h, response, 404);

    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        String response = "{\"error\": \"Time overlap detected\"}";
        sendText(h, response, 406);
    }

    protected void sendBadResponse(HttpExchange exchange, String responseText, int statusCode) throws IOException {
        String jsonResponse = "{\"error\": \"" + responseText + "\"}";
        sendText(exchange, jsonResponse, statusCode);
    }
}
