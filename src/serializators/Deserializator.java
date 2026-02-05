package serializators;

import com.google.gson.*;
import task.Task;
import taskStatus.TaskStatus;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class Deserializator implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int id = jsonObject.get("taskID").getAsInt();
        String name = jsonObject.get("taskName").getAsString();
        String description = jsonObject.get("taskDescription").getAsString();
        TaskStatus taskStatus = TaskStatus.valueOf(jsonObject.get("taskStatus").getAsString());
        Duration duration = context.deserialize(jsonObject.get("duration"), Duration.class);
        LocalDateTime localDateTime = context.deserialize(jsonObject.get("startTime"), LocalDateTime.class);
        Task task = new Task(name, description, taskStatus);
        task.setTaskID(id);
        task.setDuration(duration);
        task.setStartTime(localDateTime);
        return task;
    }
}
