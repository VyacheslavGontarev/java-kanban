import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

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

    ArrayList<Subtask> getSubtasksByEpicId(int epicId);

    void updateEpicStatus(Epic epic);

    List<Task> getStory();

}