package task;

import taskStatus.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected TaskStatus taskStatus;
    protected Integer taskID;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String taskName, String taskDescription, TaskStatus taskStatus) {
        this.taskDescription = taskDescription;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.startTime = LocalDateTime.now();
    }

    protected void updateStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
