package serializators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import task.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class Serialozator implements JsonSerializer<Task> {

    @Override
    public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonTask = new JsonObject();
        jsonTask.addProperty("taskID", task.getTaskID());
        jsonTask.addProperty("taskName", task.getTaskName());
        jsonTask.addProperty("taskStatus", task.getTaskStatus().toString());
        jsonTask.addProperty("taskDescription", task.getTaskDescription());
        jsonTask.add("duration", context.serialize(task.getDuration(), Duration.class));
        jsonTask.add("startTime", context.serialize(task.getStartTime(), LocalDateTime.class));
        return jsonTask;
    }
}
