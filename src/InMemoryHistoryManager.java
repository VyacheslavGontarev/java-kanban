import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node> inMemoryHistory = new HashMap<>();
    int head = -1;
    int tail = -1;

    ArrayList<Task> history = new ArrayList<>();


    @Override
    public void add(Task task) {
        if (inMemoryHistory.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (inMemoryHistory.containsKey(id)) {
            removeNode(inMemoryHistory.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        getTasks();
        return history;
    }

    private void linkLast(Task task) {
        Node node = new Node(task);
        if (head < 0) {
            head = task.getId();
        }
        if (inMemoryHistory.containsKey(tail)) {
            inMemoryHistory.get(tail).setNext(task.getId());
            node.setPrev(tail);
        }
        inMemoryHistory.put(task.getId(), node);
        tail = task.getId();
    }

    private void removeNode(Node node) {
        if (inMemoryHistory.containsValue(node)) {
            if (node.getPrev() >= 0  && inMemoryHistory.containsKey(node.getPrev())) {
                inMemoryHistory.get(node.getPrev()).setNext(node.getNext());
            } else {
                head = node.getNext();
            }
            if (node.getNext() >= 0 && inMemoryHistory.containsKey(node.getNext())) {
                inMemoryHistory.get(node.getNext()).setPrev(node.getPrev());
            } else {
                tail = node.getPrev();
            }
            inMemoryHistory.remove(node);
        }
    }

    private void getTasks() {
        if (!inMemoryHistory.isEmpty()) {
            history = new ArrayList<>();
            Node node = inMemoryHistory.get(head);
            while (node.getNext() != -1) {
                history.add(node.getData());
                node = inMemoryHistory.get(node.getNext());
            }
            history.add(inMemoryHistory.get(tail).getData());
        }
    }
}