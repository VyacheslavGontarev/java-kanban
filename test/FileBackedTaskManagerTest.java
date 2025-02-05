import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    File file;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @BeforeEach
    void fileCreate() throws IOException {
        file = File.createTempFile("test", ".csv");
    }

    @Test
    void saveSeveralTasks() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Task task = new Task("Name", "Description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task);
        Task task1 = new Task("Name1", "Description1", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task1);
        List<String> lines = Files.readAllLines(file.toPath());
        assertNotNull(lines, "Файл пуст");
        assertTrue(file.exists(), "Файл не существует");
        assertEquals(lines.size(), 3, "Количество строк не совпадает");
    }

    @Test
    void loadSeveralTasks() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Task task = new Task("Name", "Description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task);
        Task task1 = new Task("Name1", "Description1", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task1);
        FileBackedTaskManager newManager = manager.loadFromFile(file);
        List<Task> tasks = newManager.getAllTasks();
        assertNotNull(tasks, "Файл пуст");
        assertEquals(tasks.size(), 2, "Количество задач не совпадает");
    }

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Task task = new Task("Name", "Description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task);
        manager.deleteAllTasks();
        List<String> lines = Files.readAllLines(file.toPath());
        assertNotNull(lines, "Файл пуст");
        assertTrue(file.exists(), "Файл не существует");
        assertEquals(lines.size(), 1, "Количество строк не совпадает");
        FileBackedTaskManager newManager = manager.loadFromFile(file);
        List<Task> tasks = newManager.getAllTasks();
        assertNotNull(tasks, "Файл пуст");
        assertEquals(tasks.size(), 0, "Задачи есть");
    }

    @Test
    void testUpdateEpicStatus_AllSubtasksNew() {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Epic epic = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask0);
        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "id 0", 0, Status.NEW,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask1);
        manager.updateEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testUpdateEpicStatus_AllSubtasksDone() {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Epic epic = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.DONE,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask0);
        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "id 0", 0, Status.DONE,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask1);
        manager.updateEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void testUpdateEpicStatus_MixedSubtasks() {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Epic epic = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask0);
        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "id 0", 0, Status.DONE,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask1);
        manager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testUpdateEpicStatus_SubtasksInProgress() {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Epic epic = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.IN_PROGRESS,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask0);
        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "id 0", 0, Status.IN_PROGRESS,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask1);
        manager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testTimeValidator() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Task task = new Task("Name", "Description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task);
        Task task1 = new Task("Name1", "Description1", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        manager.createTask(task1);
        List<String> lines = Files.readAllLines(file.toPath());
        assertNotNull(lines, "Файл пуст");
        assertTrue(file.exists(), "Файл не существует");
        assertEquals(lines.size(), 2, "Количество строк не совпадает");
        FileBackedTaskManager newManager = manager.loadFromFile(file);
        List<Task> tasks = newManager.getAllTasks();
        assertNotNull(tasks, "Файл пуст");
        assertEquals(tasks.size(), 1, "Задачи есть");
    }

    @Test
    void testException() throws IOException {
        file = File.createTempFile("test", ".csv");
        assertThrows(IOException.class, () -> {
            file.delete();
           int lines = Files.readAllLines(file.toPath()).size();
        }, "Чтение пустого файла должно приводить к исключению");
    }
}