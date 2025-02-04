import java.util.Comparator;
import java.time.LocalDateTime;

public class TaskTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return t1.getStartTime().compareTo(t2.getStartTime());
    }
}
