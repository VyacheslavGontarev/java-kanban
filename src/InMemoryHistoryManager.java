import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    Map<Integer, Node> history = new HashMap<>();
    int prev = -1;

    @Override
    public void add(Task task) {
        Node node = new Node(task);
        if (history.containsKey(prev)) {
            node.setPrev(history.get(prev));
            history.get(prev).setNext(node);
            history.put(task.getId(), node);
            prev = task.getId();
        } else {
                history.put(task.getId(), node);
                prev = task.getId();
        }
    }
    @Override
    public void remove(int id){
        if (history.containsKey(id)){
            if (history.get(id).getPrev() != null) {
                history.get(id).getPrev().setNext(history.get(id).getNext());
            } if (history.get(id).getNext() != null) {
                history.get(id).getNext().setPrev(history.get(id).getPrev());
            }
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        for (Node node : history.values()) {
            taskHistory.add((Task)node.getData());
        }
        return taskHistory;
    }
    }

