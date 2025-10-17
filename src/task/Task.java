package task;

import taskStatus.TaskStatus;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected TaskStatus taskStatus;
    protected Integer taskID;

    public Task(String taskName, String taskDescription, TaskStatus taskStatus) {
        this.taskDescription = taskDescription;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    protected void updateStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
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
