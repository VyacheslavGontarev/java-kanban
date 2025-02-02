import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    InMemoryHistoryManager history = Managers.getDefaultHistory();
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();



    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        ArrayList<Integer> sub = epics.get(subtask.getEpicId()).getSubtask();
        sub.add(subtask.getId());
        Epic epic = new Epic(epics.get(subtask.getEpicId()).getName(),epics.get(subtask.getEpicId()).getDescription(),
                epics.get(subtask.getEpicId()).getStatus());
        epic.setId(subtask.getEpicId());
        epic.setSubtask(sub);
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> quests = new ArrayList<>();
        for (Task task : tasks.values()) {
            quests.add(task);
            history.add(task);
        }
        return quests;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> quests = new ArrayList<>();
        for (Epic epic : epics.values()) {
            quests.add(epic);
            history.add(epic);
        }
        return quests;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> quests = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            quests.add(subtask);
            history.add(subtask);
        }
        return quests;
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            history.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Subtask subtask : subtasks.values()) {
            history.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            history.remove(epic.getId());
        }
        epics.clear();
    }

    @Override
    public Task getTaskByID(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubTaskByID(int id) {
        history.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicByID(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        ArrayList<Integer> sub = epics.get(epic.getId()).getSubtask();
        epics.put(epic.getId(), epic);
        epic.setSubtask(sub);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        if (subtask.getId() != subtask.getEpicId()) {
            if (epics.get(subtask.getEpicId()) != null) {
                updateEpicStatus(epics.get(subtask.getEpicId()));
            }
        }
    }

    @Override
    public void delOneTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void delOneEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Integer> subtask = epics.get(id).getSubtask();
            epics.remove(id);
            for (int i : subtask) {
                subtasks.remove(i);
                history.remove(i);
            }
            history.remove(id);
        }
    }

    @Override
    public void delOneSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            if (epics.containsKey(epicId)) {
                Epic epic = epics.get(epicId);
                epic.getSubtask().remove(epic.getSubtask().indexOf(id));
                updateEpicStatus(epic);
            }
            history.remove(id);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> quests = new ArrayList<>();
        for (int i : epics.get(epicId).getSubtask()) {
            quests.add(subtasks.get(i));
        }
        return quests;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtask = new ArrayList<>();
        for (int i : epic.getSubtask()) {
            if (subtasks.get(i) != null) {
                epicSubtask.add(subtasks.get(i));
            }
        }
        if (epicSubtask == null) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allDone = true;
        boolean allNew = true;
        for (Subtask subtask : epicSubtask) {
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
            return;
        } else if (allNew) {
            epic.setStatus(Status.NEW);
            return;
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public int generateId() {
        return id++;
    }

    @Override
    public List<Task> getStory() {
        return history.getHistory();
    }
}