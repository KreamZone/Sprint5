package task;

import taskStatus.TaskStatus;

public class Subtask extends Task{
    private final Epic masterTask;

    public Subtask(String taskName, String taskDescription, TaskStatus taskStatus, Epic masterTask) {
        super(taskName, taskDescription, taskStatus);
        this.masterTask = masterTask;
    }
    @Override
    public void updateStatus(TaskStatus newTaskStatus) {
        taskStatus = newTaskStatus;
        masterTask.updateStatus(newTaskStatus);
    }

    public Epic getMasterTask() {
        return masterTask;
    }

}
