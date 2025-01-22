import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    File file;

    @BeforeEach
    void fileCreate() throws IOException {
        file = File.createTempFile("test", ".csv");
    }

    @Test
    void saveSeveralTasks() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Task task = new Task("Name", "Description", Status.NEW);
        manager.createTask(task);
        Task task1 = new Task("Name1", "Description1", Status.NEW);
        manager.createTask(task1);
        List<String> lines = Files.readAllLines(file.toPath());
        assertNotNull(lines, "Файл пуст");
        assertTrue(file.exists(), "Файл не существует");
        assertEquals(lines.size(), 3, "Количество строк не совпадает");
    }

    @Test
    void loadSeveralTasks() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        Task task = new Task("Name", "Description", Status.NEW);
        manager.createTask(task);
        Task task1 = new Task("Name1", "Description1", Status.NEW);
        manager.createTask(task1);
        FileBackedTaskManager newManager = manager.loadFromFile(file);
        List<Task> tasks = newManager.getAllTasks();
        assertNotNull(tasks, "Файл пуст");
        assertEquals(tasks.size(), 2, "Количество задач не совпадает");
    }

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());
        manager.save();
        List<String> lines = Files.readAllLines(file.toPath());
        assertNotNull(lines, "Файл пуст");
        assertTrue(file.exists(), "Файл не существует");
        assertEquals(lines.size(), 1, "Количество строк не совпадает");
        FileBackedTaskManager newManager = manager.loadFromFile(file);
        ArrayList<Task> tasks = newManager.getAllTasks();
        assertNotNull(tasks, "Файл пуст");
        assertEquals(tasks.size(), 0, "Задачи есть");
    }
}