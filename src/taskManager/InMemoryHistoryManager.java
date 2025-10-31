package taskManager;

import interfaces.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private final Map<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;


    @Override
    public void add(Task task){
        if (task == null){
            return;
        }
        //remove(task.getTaskID());
        Node node = new Node(task);
        linkLast(node);
        history.put(task.getTaskID(),node);
        size++;
    }

    @Override
    public void remove(int id){
        Node node = history.get(id);
        if(node != null){
            removeNode(node);
            history.remove(id);
            size--;
        }

    }

    @Override
    public List<Task> getHistory() {
        List<Task> localHistory = new ArrayList<>();
        Node currentNode = head;
        while(currentNode != null) {
            localHistory.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return  localHistory;
    }

    private void removeNode(Node node){
        if(node.prev != null){
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if(node.next != null){
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        node.prev = null;
        node.next = null;
    }

    private void linkLast(Node node) {
        if(tail == null){
            tail = node;
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }
}
