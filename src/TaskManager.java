import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void createTask(Task task);

    public void createEpic(Epic epic);

    public void createSubtask(Subtask subtask);

    ArrayList<Task> getAllTasks();

    public ArrayList<Epic> getAllEpics();

    public ArrayList<Subtask> getAllSubtasks();

    void deleteAllTasks();

    public void deleteAllSubtasks();

    public void deleteAllEpics();

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