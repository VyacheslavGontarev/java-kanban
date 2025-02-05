import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Task getTaskByID(int id);

    Subtask getSubTaskByID(int id);

    Epic getEpicByID(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void delOneTask(int id);

    void delOneEpic(int id);

    void delOneSubtask(int id);

    List<Subtask> getSubtasksByEpicId(int epicId);

    void updateEpicStatus(Epic epic);

    List<Task> getStory();

    void updateEpicStartTime(Epic epic);

    void updateEpicDuration(Epic epic);

    List<Task> getPrioritizedTasks();

    boolean timeValidator(Task task);

    void epicEndTime(Epic epic);
}