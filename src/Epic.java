import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtask;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.subtask = new ArrayList<>();
        taskType = TaskTypes.EPIC;
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
                "subtask=" + subtask +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", taskType=" + taskType +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public void setEndTime() {
        if (startTime != null) {
            this.endTime = startTime.plus(duration);
        }
    }
}