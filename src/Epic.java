import java.util.HashMap;
public class Epic extends Task {
   static HashMap<Integer, Subtask> subtask = new HashMap<>();

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }
    public static void putSubtask(Integer name, Subtask quest) {
        subtask.put(name, quest);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public void changeEpicStatusDone() {
        if (!subtask.isEmpty()) {
            boolean check = false;
            for (Subtask tusk : subtask.values()) {
                if (!(tusk.getStatus() == Status.DONE)) {
                    check = true;
                }
            }
                if (check == false) {
                    setStatus(Status.DONE);
                    System.out.println("Эпик завершён!");
            }
        }
    }
    public void deleteAllSubtasks(){
        if (!subtask.isEmpty()) {
            for (int key : subtask.keySet()){
                subtask.remove(key);
            }
        }
    }
    public void removeSubtask(int id) {
        this.subtask.remove(id);
    }
    public boolean subtaskIsEmpty(){
        return this.subtask.isEmpty();
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


