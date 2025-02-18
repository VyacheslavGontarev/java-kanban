import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {

    @Test
    void testSubtaskHaveExistedEpic() {
        System.out.println("yyyyyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        manager.createEpic(epic);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.IN_PROGRESS,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        manager.createSubtask(subtask0);
        assertNotNull(subtask0.getEpicId(), "id Эпика отсутствует");
        assertEquals(subtask0.getEpicId(), epic.getId(), "id не совпадает");
        assertEquals(manager.getEpicByID(subtask0.getEpicId()), epic, "Эпики не совпадают");
    }

    @Test
    void createTask() {
    }

    @Test
    void createEpic() {
    }

    @Test
    void createSubtask() {

    }

    @Test
    void getAllTasks() {
    }

    @Test
    void getAllEpics() {
    }

    @Test
    void getAllSubtasks() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void deleteAllSubtasks() {
    }

    @Test
    void deleteAllEpics() {
    }

    @Test
    void getTaskByID() {
    }

    @Test
    void getSubTaskByID() {
    }

    @Test
    void getEpicByID() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void delOneTask() {
    }

    @Test
    void delOneEpic() {
    }

    @Test
    void delOneSubtask() {
    }

    @Test
    void getSubtasksByEpicId() {
    }

    @Test
    void updateEpicStatus() {
    }

    @Test
    void getStory() {
    }

    @Test
    void updateEpicStartTime() {
    }

    @Test
    void updateEpicDuration() {
    }

    @Test
    void getPrioritizedTasks() {
    }

    @Test
    void timeValidator() {
    }

    @Test
    void epicEndTime() {
    }
}
