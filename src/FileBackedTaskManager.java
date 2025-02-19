import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import exception.ManagerSaveException;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    String file;

    public FileBackedTaskManager(String file) {

        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader buf = new BufferedReader(fileReader)) {
            String line = buf.readLine();
            while (buf.ready()) {
                line = buf.readLine();
                if (line.contains("EPIC")) {
                    Task epic = fromString(line);
                    manager.createEpic((Epic) epic);
                } else if (line.contains("SUBTASK")) {
                    Task subtask = fromString(line);
                    manager.createSubtask((Subtask) subtask);
                } else {
                    Task task = fromString(line);
                    manager.createTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения");
        }
        return manager;
    }

    public static void main(String[] args) {
        File file = new File("SavedTasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Task task = new Task("Сходить в магазин", "Пятёрочка топ", Status.NEW,
                LocalDateTime.parse("04.02.2025 01:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task);
        Task task1 = new Task("Получить баллы на карту x5 клуба", "Карту нужно активировать", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task1);
        Epic epic = new Epic("Купить арбуз", "Нужен самый сладкий", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Понюхать хвостик", "Будет вкусно пахнуть", 2, Status.DONE,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask);
        manager.updateSubtask(new Subtask("Понюхать хвостик", "Будет вкусно пахнуть", 2,
               Status.IN_PROGRESS, LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1)));
        Subtask subtask1 = new Subtask("Постучать по арбузу", "Должен глухо звучать", 2, Status.DONE,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(30));
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Спросить совет продавца", "Нужно чтоб сказал ДА СПЕЛЫЙ ОН",
                2, Status.DONE, LocalDateTime.parse("05.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createSubtask(subtask2);
        Epic epic1 = new Epic("Купить молоко", "Нужно свежее", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic1);
        FileBackedTaskManager newManager = loadFromFile(file);
        Task task2 = new Task("Сходить за прессой", "Где Союз Печать?", Status.NEW,
                LocalDateTime.parse("03.02.2025 00:31", formatter), Duration.ofMinutes(20));
        newManager.createTask(task2);
        System.out.println(newManager.convertMbTask(2));
        newManager.printTasks();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void delOneTask(int id) {
        super.delOneTask(id);
        save();
    }

    @Override
    public void delOneEpic(int id) {
        super.delOneEpic(id);
        save();
    }

    @Override
    public void delOneSubtask(int id) {
        super.delOneSubtask(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
       return super.getPrioritizedTasks();
    }

    @Override
    public void updateEpicStartTime(Epic epic) {
        super.updateEpicStartTime(epic);
        save();
    }

    @Override
    public void updateEpicDuration(Epic epic) {
        super.updateEpicDuration(epic);
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter buf = new BufferedWriter(fileWriter)) {
            buf.write("id,type,name,status,description,startTime,duration,epic\n");
            buf.write(getAllTasks().stream()
                    .map(task -> toString(task)  + "\n")
                    .collect(Collectors.joining()));
            buf.write(getAllEpics().stream()
                    .map(task -> toString(task) + "\n")
                    .collect(Collectors.joining()));
            buf.write(getAllSubtasks().stream()
                    .map(task -> toString(task) + "\n")
                    .collect(Collectors.joining()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи");
        }
    }

    private void printTasks() {
        System.out.println(getPrioritizedTasks());
    }

    private int getEpicId(Task task) {
        return ((Subtask) task).getEpicId();
    }

    private String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String str;
        if (task.getStartTime() == null) {
            str = null;
        } else {
            str = task.getStartTime().format(formatter);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId() + "," + task.getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription() + "," + str +
                "," + task.getDuration().toMinutes() + ",");
        if (task.getType() == TaskTypes.SUBTASK) {
            builder.append(getEpicId(task));
        }
        return builder.toString();
    }

    private static Task fromString(String value) throws NumberFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String[] args = value.split(",");

        if (TaskTypes.valueOf(args[1]) == TaskTypes.TASK) {
            Task task = new Task(args[2], args[4], Status.valueOf(args[3]), LocalDateTime.parse(args[5], formatter),
                    Duration.ofMinutes(Integer.parseInt(args[6])));
            task.setId(Integer.parseInt(args[0]));
            return task;
        } else if (TaskTypes.valueOf(args[1]) == TaskTypes.EPIC) {
            Epic epic;
            if (args[5].equals("null")) {
                epic = new Epic(args[2], args[4], Status.valueOf(args[3]), null,
                        Duration.ofMinutes(Integer.parseInt((args[6]))));
            } else {
                epic = new Epic(args[2], args[4], Status.valueOf(args[3]), LocalDateTime.parse(args[5], formatter),
                        Duration.ofMinutes(Integer.parseInt(args[6])));
            }
            epic.setId(Integer.parseInt(args[0]));
            return epic;
        } else {
            Subtask subtask = new Subtask(args[2], args[4], Integer.parseInt(args[7]), Status.valueOf(args[3]),
                        LocalDateTime.parse(args[5], formatter), Duration.ofMinutes(Integer.parseInt(args[6])));
            subtask.setId(Integer.parseInt(args[0]));
            return subtask;
        }
    }
}
