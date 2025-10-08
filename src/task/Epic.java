package task;

import taskStatus.TaskStatus;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(String taskName, String taskDescription, TaskStatus taskStatus ) {
        super(taskName, taskDescription, taskStatus);
    }

    public void addNewSubtask(Subtask newSubtask) {
        subtasks.add(newSubtask);
        updateStatus(newSubtask.taskStatus);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    private TaskStatus subStatisChek() {
        for (int i = 1; i < subtasks.size(); i++) {
            if(subtasks.get(i).taskStatus != subtasks.get(i-1).taskStatus) {
                return TaskStatus.IN_PROCESS;
            }
        }
        if(!subtasks.isEmpty()){
            return subtasks.getFirst().taskStatus;
        }
        else{
            return TaskStatus.NEW;
        }
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void updateStatus(TaskStatus taskStatus){
        this.taskStatus = subStatisChek();
    }
}
