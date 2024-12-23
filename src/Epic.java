import java.util.ArrayList;

public class Epic extends Task {
    //for commit
    private ArrayList<Integer> subtask;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtask = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtask() {
        return subtask;
    }
    public void setSubtask(ArrayList<Integer> subtask) {
        this.subtask = subtask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtasks=" + subtask +
                '}';
    }
}

