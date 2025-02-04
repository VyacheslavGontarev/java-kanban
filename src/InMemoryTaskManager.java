import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    InMemoryHistoryManager history = Managers.getDefaultHistory();
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskTimeComparator());


    @Override
    public void createTask(Task task) {
        if (!timeValidator(task)) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
            System.out.println("Task Created"); //TODO
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (!timeValidator(subtask)) {
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            ArrayList<Integer> sub = epics.get(subtask.getEpicId()).getSubtask();
            sub.add(subtask.getId());
            Epic epic = new Epic(epics.get(subtask.getEpicId()).getName(), epics.get(subtask.getEpicId()).getDescription(),
                    epics.get(subtask.getEpicId()).getStatus(), epics.get(subtask.getEpicId()).getStartTime(),
                    epics.get(subtask.getEpicId()).getDuration());
            epic.setId(subtask.getEpicId());
            epic.setSubtask(sub);
            updateEpicStatus(epic);
            updateEpicStartTime(epic);
            updateEpicDuration(epic);

            System.out.println("Subtask Created"); //TODO
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks.values().stream().
                peek(task -> history.add(task)).
                collect(Collectors.toList());
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.values().stream().
                peek(task -> history.add(task)).
                collect(Collectors.toList());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return subtasks.values().stream().
                peek(task -> history.add(task)).
                collect(Collectors.toList());
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().stream().
                peek(task -> history.remove(task.getId())).
                peek(task -> prioritizedTasks.remove(task));
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().stream().
                peek(task -> history.remove(task.getId())).
                peek(task -> prioritizedTasks.remove(task));
        subtasks.clear();
        epics.values().stream().
            peek(task -> updateEpicStatus(task)).
            peek(task  -> updateEpicStartTime(task)).
            peek(task -> updateEpicDuration(task));
    }

    @Override
    public void deleteAllEpics() {
        subtasks.values().stream().
                peek(task -> history.remove(task.getId())).
                peek(task -> prioritizedTasks.remove(task));
        subtasks.clear();
        epics.values().stream().
            peek(epic -> history.remove(epic.getId()));
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
        if (timeValidator(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.remove(task);
            prioritizedTasks.add(task);
        }
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
        if (timeValidator(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.remove(subtask);
            prioritizedTasks.add(subtask);
            if (subtask.getId() != subtask.getEpicId()) {
                if (epics.get(subtask.getEpicId()) != null) {
                    updateEpicStatus(epics.get(subtask.getEpicId()));
                    updateEpicStartTime(epics.get(subtask.getEpicId()));
                    updateEpicDuration(epics.get(subtask.getEpicId()));
                }
            }
        }
    }

    @Override
    public void delOneTask(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
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
            prioritizedTasks.remove(subtasks.get((id)));
            int epicId = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            if (epics.containsKey(epicId)) {
                Epic epic = epics.get(epicId);
                epic.getSubtask().remove(epic.getSubtask().indexOf(id));
                updateEpicStatus(epic);
                updateEpicStartTime(epic);
                updateEpicDuration(epic);
            }
            history.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return epics.get(epicId).getSubtask().stream().
                map(subtaskId -> subtasks.get(subtaskId)).
                collect(Collectors.toList());
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        List<Subtask> epicSubtasks = subtasks.keySet().stream().
                filter(key -> epic.getSubtask().contains(key)).
                map(key -> subtasks.get(key)).
                collect(Collectors.toList());
        if (epicSubtasks == null) {
            epic.setStatus(Status.NEW);
            return;
        }
        epicSubtasks.stream().
                peek(subtask -> {

                    boolean allDone = true;
                    boolean allNew = true;
                    if (subtask.getStatus() != Status.DONE) {
                        allDone = false;
                    }
                    if (subtask.getStatus() != Status.NEW) {
                        allNew = false;
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
                });
    }

    public int generateId() {
        return id++;
    }

    @Override
    public List<Task> getStory() {
        return history.getHistory();
    }

    public void updateEpicStartTime(Epic epic) {
        ArrayList<Integer> sub = epics.get(epic.getId()).getSubtask();
        List<Subtask> epicSubtasks = subtasks.keySet().stream().
                filter(key -> sub.contains(key)).
                map(key -> subtasks.get(key)).
                collect(Collectors.toList());
        if (epicSubtasks == null) {
            epic.setStartTime(null);
            return;
        }
        int id = epic.getId();
        LocalDateTime earliestStartTime = epicSubtasks.stream().
                map(subtask -> subtask.getStartTime()).
                min(LocalDateTime::compareTo).
                orElseThrow(NoSuchElementException::new);
        epic = new Epic(epic.getName(), epic.getDescription(), epic.getStatus(), earliestStartTime, epic.getDuration());
        epic.setId(id);
        epic.setEndTime();
        epics.put(id, epic);
    }

    public void updateEpicDuration(Epic epic) {
        ArrayList<Integer> sub = epics.get(epic.getId()).getSubtask();
        int id = epic.getId();
        Duration duration = subtasks.keySet().stream().
                filter(key -> sub.contains(key)).
                map(key -> subtasks.get(key)).
                map(subtask -> subtask.getDuration()).
                reduce(Duration.ZERO, Duration::plus);
        epic = new Epic(epic.getName(), epic.getDescription(), epic.getStatus(), epic.getStartTime(), duration);
        epic.setId(id);
        epic.setEndTime();
        epics.put(id, epic);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean timeValidator(Task task) {
        return prioritizedTasks.stream().anyMatch(exTask ->
                    (task.getStartTime().isBefore(exTask.getEndTime()) &&
                            task.getEndTime().isAfter(exTask.getStartTime())));
    }
}