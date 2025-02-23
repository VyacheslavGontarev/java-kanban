import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    InMemoryHistoryManager history = Managers.getDefaultHistory();
@Override
    public int getId() { //TODO
        return id;
    }

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
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        System.out.println("Method is on");
        if (!timeValidator(subtask)) {
            System.out.println("Thickl is on");
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
            System.out.println(epic.getStartTime());
            updateEpicDuration(epic);
            System.out.println(epic.getDuration());
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .peek(task -> history.add(task))
                .collect(Collectors.toList());
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.values().stream()
                .peek(task -> history.add(task))
                .collect(Collectors.toList());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return subtasks.values().stream()
                .peek(task -> history.add(task))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().stream()
                .peek(task -> history.remove(task.getId()))
                .peek(task -> prioritizedTasks.remove(task));
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().stream()
                .peek(task -> history.remove(task.getId()))
                .peek(task -> prioritizedTasks.remove(task));
        subtasks.clear();
        epics.values().stream()
                .peek(task -> updateEpicStatus(task))
                .peek(task -> updateEpicStartTime(task))
                .peek(task -> updateEpicDuration(task));
    }

    @Override
    public void deleteAllEpics() {
        subtasks.values().stream()
                .peek(task -> history.remove(task.getId()))
                .peek(task -> prioritizedTasks.remove(task));
        subtasks.clear();
        epics.values().stream()
                .peek(epic -> history.remove(epic.getId()));
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
    public void updateEpicStatus(Epic epic) {
        int completedTasks = epic.getSubtask().stream()
                .mapToInt(id -> {
                    if (subtasks.get(id).getStatus().equals(Status.DONE)) {
                        return 1;
                    } else if (subtasks.get(id).getStatus().equals(Status.NEW)) {
                        return 0;
                    }
                    return -1;
                }).sum();

        if (completedTasks == epic.getSubtask().size()) {
            epic.setStatus(Status.DONE);
        } else if (epic.getSubtask().isEmpty() || (epic.getSubtask().size() - completedTasks) == epic.getSubtask().size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return epics.get(epicId).getSubtask().stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getStory() {
        return history.getHistory();
    }

    @Override
    public void updateEpicStartTime(Epic epic) {
        List<Subtask> subTaskList = subtasks.keySet().stream()
                .filter(key -> subtasks.containsKey(key))
                .map(key -> subtasks.get(key))
                .collect(Collectors.toList());
        if (subTaskList == null) {
            return;
        }
        LocalDateTime startTime = subTaskList.stream()
                .map(subTask -> subTask.getStartTime())
                .min(LocalDateTime::compareTo).get();
        epic.setStartTime(startTime);
    }

    @Override
        public void updateEpicDuration(Epic epic) {
            Duration duration = Duration.ofMinutes(0);
            for (int indexIdSubtask : epic.getSubtask()) {
                Duration durationSubTask = subtasks.get(indexIdSubtask).getDuration();
                if (durationSubTask != null) {
                    duration = duration.plus(durationSubTask);
                }
            }
            epic.setDuration(duration);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean timeValidator(Task task) {
        return prioritizedTasks.stream().anyMatch(exTask ->
                (task.getStartTime().isBefore(exTask.getEndTime()) &&
                        task.getEndTime().isAfter(exTask.getStartTime())));
    }

    public Optional<Task> findMbTask(int id) {
        if (tasks.containsKey(id)) {
            return Optional.of(tasks.get(id));
        } else if (subtasks.containsKey(id)) {
            return Optional.of(subtasks.get(id));
        } else if (epics.containsKey(id)) {
            return Optional.of(epics.get(id));
        } else {
            return Optional.empty();
        }
    }

    public Task convertMbTask(int id) {
        Optional<Task> task = findMbTask(id);
        return task.orElse(null);
    }

    public int generateId() {
        return id++;
    }
}