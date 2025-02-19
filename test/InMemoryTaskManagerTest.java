import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void createTaskTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        taskManager.deleteAllTasks();
        final List<Task> deletedTasks = taskManager.getAllTasks();
        assertTrue(deletedTasks.isEmpty(), "Задачи не удаляются.");
    }

    @Test
    public void createEpicTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW,
                null, Duration.ofMinutes(0));
        taskManager.createEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicByID(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();

        Subtask subtask = new Subtask("Test addSubtask", "Subtask subtask", epicId, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        taskManager.createSubtask(subtask);
        final int subtaskId = subtask.getId();
        final Subtask savedSubtask = taskManager.getSubTaskByID(subtaskId);


        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
        taskManager.deleteAllEpics();
        final List<Epic> deletedEpics = taskManager.getAllEpics();
        assertTrue(deletedEpics.isEmpty(), "Задачи не удаляются.");
        final List<Subtask> deletedSubtasks = taskManager.getAllSubtasks();
        assertTrue(deletedSubtasks.isEmpty(), "Задачи не удаляются.");
    }

    @Test
    public void createSubtaskTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW,
                null, Duration.ofMinutes(0));
        taskManager.createEpic(epic);
        final int epicId = epic.getId();

        Subtask subtask = new Subtask("Test addSubtask", "Subtask subtask", epicId, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        taskManager.createSubtask(subtask);
        final int subtaskId = subtask.getId();
        final Subtask savedSubtask = taskManager.getSubTaskByID(subtaskId);
        final List<Subtask> subtaskByEpicId = taskManager.getSubtasksByEpicId(epicId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        assertNotNull(subtaskByEpicId, "Задача не найдена.");
        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");

        taskManager.deleteAllSubtasks();
        final List<Subtask> deletedSubtasks = taskManager.getAllSubtasks();
        assertTrue(deletedSubtasks.isEmpty(), "Задачи не удаляются.");
    }

    @Test
    public void taskStatisTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        final Status status = task.getStatus();
        task = new Task("Test addNewTask", "Test addNewTask description", Status.IN_PROGRESS,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        taskManager.updateTask(task);
        assertNotEquals(status, task.getStatus(), "Статус не изменяется");
    }

    @Test
    public void subtaskNEpicsStatisTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW,
                null, Duration.ofMinutes(0));
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        Subtask subtask = new Subtask("Test addSubtask", "Subtask subtask", epicId, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        taskManager.createSubtask(subtask);
        final Status epicStatus = epic.getStatus();
        final Status subtaskStatus = subtask.getStatus();
        epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW,
                null, Duration.ofMinutes(0));
        taskManager.updateEpic(epic);
        assertEquals(epicStatus, epic.getStatus(), "Статус изменяется");
        subtask = new Subtask("Test addSubtask", "Subtask subtask", epicId, Status.IN_PROGRESS,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(1));
        taskManager.updateSubtask(subtask);
        assertNotEquals(subtaskStatus, subtask.getStatus(), "Статус не изменяется");
        taskManager.updateEpicStatus(epic);
        assertEquals(epicStatus, epic.getStatus(), "Статус изменяется");
    }

    @Test
    public void taskCreateStability(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        task = taskManager.getTaskByID(task.getId());
        assertEquals("Test addNewTask", task.getName());
        assertEquals("Test addNewTask description", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
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

    @Test
    void testSubtaskHaveExistedEpic() {
        Epic epic = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        taskManager.createEpic(epic);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.IN_PROGRESS,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        taskManager.createSubtask(subtask0);
        assertNotNull(subtask0.getEpicId(), "id Эпика отсутствует");
        assertEquals(subtask0.getEpicId(), epic.getId(), "id не совпадает");
        assertEquals(taskManager.getEpicByID(subtask0.getEpicId()), epic, "Эпики не совпадают");
    }
}