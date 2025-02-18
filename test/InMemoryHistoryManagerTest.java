import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Test
    void addEmpty() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        taskManager.getTaskByID(0);
        final List<Task> history = taskManager.getStory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addSameTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        taskManager.getTaskByID(0);
        final List<Task> history = taskManager.getStory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        taskManager.getTaskByID(0);
        final List<Task> history1 = taskManager.getStory();
        assertEquals(history, history1, "История изменилась");
    }

    @Test
    void unique(){
        List<Task> list = new ArrayList<>();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        list.add(task);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task1);
        list.add(task1);
        list.add(task);
        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(0);
        final List<Task> history = taskManager.getStory();
        assertNotSame(list, history, "Объекты не равны");
    }
    @Test
    void remove(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task1);
        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        final List<Task> list = taskManager.getStory();
        taskManager.delOneTask(1);
        final List<Task> history = taskManager.getStory();
        assertNotEquals(list.size(), history.size(), "Объекты не равны");
    }

    @Test
    void removeFromMiddle(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task1);
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 05:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task2);
        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        final List<Task> list = taskManager.getStory();
        taskManager.delOneTask(1);
        final List<Task> history = taskManager.getStory();
        assertNotEquals(list.size(), history.size(), "Объекты не равны");
    }

    @Test
    void removeFromEnd(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task1);
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 05:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task2);
        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        final List<Task> list = taskManager.getStory();
        taskManager.delOneTask(2);
        final List<Task> history = taskManager.getStory();
        assertNotEquals(list.size(), history.size(), "Объекты не равны");
    }

    @Test
    public void getHystoryTest(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        final int taskId = task.getId();
        taskManager.getTaskByID(taskId);
        List<Task> hystory = taskManager.getStory();
        assertFalse(hystory.isEmpty());
        assertEquals(1, hystory.size());
    }
}